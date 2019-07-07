<!--suppress ALL -->
<template>
    <el-popover
            placement="top"
            width="400"
            trigger="click">
        <div v-loading="loading">
            <div :ref="containerId" style="width: 400px; height: 400px"></div>
        </div>
        <el-row style="display: flex; justify-content: center; margin-top: 1rem">
            <el-button @click="locateToCurrent">定位到当前位置</el-button>
        </el-row>
        <el-button type="primary"
                   icon="el-icon-search"
                   slot="reference"
                   @click="render"
                   circle></el-button>
    </el-popover>
</template>

<script>
    let getCurrentPosition = () => {
        return new Promise((resolve, reject) => {
            let geolocation = new BMap.Geolocation();
            geolocation.getCurrentPosition(function (result) {
                if (this.getStatus() === BMAP_STATUS_SUCCESS) {
                    resolve(result.point)
                } else {
                    reject(this.getStatus());
                }
            }, {enableHighAccuracy: true});
        })
    };

    let getDetailPosition = (point) => {
        return new Promise((resolve, reject) => {
            let geoc = new BMap.Geocoder();
            geoc.getLocation(point, function (rs) {
                resolve(rs.addressComponents)
            });
        })
    }

    export default {
        name: "MapButton",
        props: {
            location: {
                type: Object,
            },
            writeBack: {
                type: Boolean,
                default: false
            },
            locateCurrent: {
                type: Boolean,
                default: false
            },
        },
        data() {
            return {
                loading: false,
                map: null,
                isRendered: false,
                containerId: 'all-map' + Date.now(),
                currentOverlay: null
            }
        },
        methods: {
            render() {
                if (!this.isRendered) {
                    this.isRendered = true;
                    let map = new BMap.Map(this.$refs[this.containerId]);
                    this.map = map;
                    map.enableScrollWheelZoom();
                    map.enableInertialDragging();
                    map.enableContinuousZoom();
                    map.addControl(new BMap.CityListControl({
                        anchor: BMAP_ANCHOR_TOP_LEFT,
                        offset: new BMap.Size(10, 20),
                        // 切换城市之间事件
                        // onChangeBefore: function(){
                        // },
                        // 切换城市之后事件
                        // onChangeAfter:function(){
                        // }
                    }));

                    let eventHandler = async (e) => {
                        this.loading = true;
                        let point = e.point;
                        this.map.removeOverlay(this.currentOverlay);
                        let marker = new BMap.Marker(point);
                        this.currentOverlay = marker;
                        this.map.addOverlay(marker);
                        this.writePoint(point);
                        this.loading = false;
                    };

                    map.addEventListener("click", eventHandler);

                    let location = this.location;
                    let initLng = location && location.longitude ? location.longitude : 118.963936;
                    let initLat = location && location.latitude ? location.latitude : 32.119704;
                    let point = new BMap.Point(initLng, initLat);
                    map.centerAndZoom(point, 12);
                    if (this.locateCurrent) {
                        this.locateToCurrent();
                    }
                }
            },

            async locateToCurrent() {
                this.loading = true;
                this.map.removeOverlay(this.currentOverlay);
                let point = await getCurrentPosition();
                let marker = new BMap.Marker(point);
                this.currentOverlay = marker;
                this.map.addOverlay(marker);
                this.map.panTo(point);
                if (this.writeBack) {
                    await this.writePoint(point);
                }
                this.loading = false;
            },

            async writePoint(point) {
                this.location.longitude = point.lng;
                this.location.latitude = point.lat;
                let address = await getDetailPosition(point);
                this.location.province = address.province;
                this.location.city = address.city;
                this.location.district = address.district;
                this.location.street = address.street;
                this.location.streetNumber = address.streetNumber;
            }

        }
    }
</script>

<style scoped>

</style>