<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>选择餐厅</h1>
        </div>
        <div class="pane-wrapper">
            <div class="my-pane">
                <div class="current-address">
                    <div class="content-wrapper" v-if="hasAddress">
                        <label>送餐地址：</label>
                        <el-select v-model="selectedLocationId" placeholder="请选择地址" style="flex: 1"
                                   @change="handleGetRestaurants">
                            <el-option
                                    v-for="item in locations"
                                    :label="getDetailLocationNoSplit(item)"
                                    :key="item.id"
                                    :value="item.id">
                                <span style="float: left">{{ getAbsoluteLocation(item) }}</span>
                                <span style="float: right; color: #8492a6; font-size: 13px">{{ getDetailLocation(item) }}</span>
                            </el-option>
                        </el-select>
                    </div>
                    <div class="content-wrapper" v-if="!hasAddress" style="display: inline; margin-top: 2rem">
                        <span style="float: left">您还没有设置地址。</span>
                        <router-link to="/customer/address/manage" style="float: right">现在去设置</router-link>
                    </div>
                    <div class="content-wrapper" v-if="hasAddress">
                        <label>送达时间：</label>
                        <el-time-picker
                                @change="handleGetRestaurants"
                                style="flex: 1"
                                v-model="receiveTime"
                                :picker-options="getOption()"
                                :placeholder="`送达时间，距今不超过${maxMinAfter}分钟`">
                        </el-time-picker>
                    </div>
                </div>
                <div class="get-food" v-if="hasSelectedAddressAndTime">
                    <div class="title-wrapper">
                        <h4>{{restaurantHead}}</h4>
                    </div>
                    <div class="content-wrapper">
                        <el-table :data="restaurants" style="width: 100%" height="80%">
                            <el-table-column label="餐厅名" prop="name" min-width="120" fixed="left"></el-table-column>
                            <el-table-column label="地址" prop="location"
                                             min-width="200"
                                             :formatter="(row)=>{return getDetailLocation(row.location)}"></el-table-column>
                            <el-table-column label="距离" prop="distance"
                                             min-width="120"
                                             :formatter="(row)=>{return row.distance.toFixed(0) + '米'}"></el-table-column>
                            <el-table-column label="预估时间" prop="distance"
                                             min-width="120"
                                             :formatter="(row)=>{return row.estimateMin.toFixed(0) + '分钟'}"></el-table-column>
                            <el-table-column label="操作" fixed="right" min-width="80">
                                <template slot-scope="scope">
                                    <el-button type="primary" size="small" @click="goIntoRestaurant(scope.row)">订餐
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="get-food" v-if="!hasSelectedAddressAndTime">
                    <div class="title-wrapper">
                        <h4>{{hasSelectedAddress?'':'地址'}}{{this.receiveTime?'':'时间'}}未选择</h4>
                    </div>
                    <div class="content-wrapper">
                        <div class="warn-wrapper">
                            <h2>在选定时间与地址之前</h2>
                            <h2>无法进行下一步操作</h2>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {customerGetLocation, customerGetRestaurant} from "../../ajax/customerAjax";
    import {globalCheckLogin} from "../../ajax/ajaxUtil";
    import {getAbsoluteLocation, getDetailLocation, getDetailLocationNoSplit} from "../../ajax/locationUtil";

    let dateFtt = (fmt, date) => {
        let o = {
            "M+": date.getMonth() + 1,                 //月份
            "d+": date.getDate(),                    //日
            "h+": date.getHours(),                   //小时
            "m+": date.getMinutes(),                 //分
            "s+": date.getSeconds(),                 //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (let k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };

    export default {
        name: "BookFood",
        data() {
            return {
                receiveTime: null,
                selectedLocationId: null,
                maxMinAfter: 30,
                locations: [{
                    id: 0,
                    longitude: 102,
                    latitude: 18,
                    province: '北京市',
                    city: '北京市',
                    district: '海淀区',
                    street: '新华街道',
                    streetNumber: '113房'
                }],
                restaurants: []
            }
        },
        mounted() {
            this.getAddress();
        },
        computed: {
            hasAddress() {
                return this.locations && this.locations.length > 0;
            },
            hasSelectedAddress() {
                return this.selectedLocationId && this.selectedLocationId >= 0
            },
            hasSelectedAddressAndTime() {
                return this.selectedLocationId && this.selectedLocationId >= 0 && this.receiveTime;
            },
            restaurantHead() {
                return `以下为${dateFtt("hh:mm:ss", this.receiveTime)}可送达的餐厅`
            }
        },
        methods: {
            getOption() {
                let now = new Date();
                let startTime = dateFtt("hh:mm:ss", now);
                let endTimestamp = now.getTime() + this.maxMinAfter * 60 * 1000;
                let endTime = dateFtt("hh:mm:ss", new Date(endTimestamp));
                return {
                    selectableRange: `${startTime} - ${endTime}`
                }
            },
            getAddress() {
                customerGetLocation().then(response => {
                    if (response.data) {
                        this.locations = response.data;
                    }
                }).catch(err => {
                    window.console.log(err);
                    globalCheckLogin(this);
                })
            },
            getDetailLocation,
            getDetailLocationNoSplit,
            getAbsoluteLocation,
            handleGetRestaurants() {
                if (this.hasSelectedAddressAndTime) {
                    customerGetRestaurant({
                        locationId: this.selectedLocationId,
                        timeInMills: this.receiveTime.getTime() - Date.now()
                    }).then(response => {
                        this.restaurants = response.data;
                    }).catch(err => {
                        globalCheckLogin(this);
                        window.console.log(err);
                    })
                }
            },
            goIntoRestaurant(row) {
                this.$router.push({
                    name: 'SelectFood',
                    params: {
                        restaurantId: row.id,
                        restaurantName: row.name,
                        customerLocationId: this.selectedLocationId
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .outer-wrapper {
        display: flex;
        flex-direction: column;
        align-items: center;
        position: relative;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
    }

    .title-wrapper {
        display: flex;
        width: 100%;
        align-items: center;
        flex-direction: column;
    }

    .pane-wrapper {
        max-width: 55rem;
        min-width: 25rem;
        width: 70%;
        flex: 1;
        overflow: scroll;
    }

    .my-pane {
        height: 90%;
        border: 1px solid #dcdfe6;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .12), 0 0 6px 0 rgba(0, 0, 0, .04);
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        align-items: center;
        overflow: scroll;
    }

    .current-address, .get-food {
        width: 80%;
        overflow: scroll;
        display: flex;
        flex-direction: column;
    }

    .current-address {
        height: 25%;
    }

    .get-food {
        height: 75%;
    }

    .content-wrapper {
        flex: 1;
        display: flex;
        flex-direction: row;
        align-items: center;
    }

    .my-button-wrapper {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 100%;
        min-height: 40px;
    }

    .warn-wrapper {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 100%;
        flex-direction: column;
        background-color: lightgray;
        overflow: hidden;
        color: gray;
    }

</style>