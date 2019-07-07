<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>地址管理</h1>
        </div>
        <div class="pane-wrapper">
            <div class="my-pane">
                <div class="current-address">
                    <div class="title-wrapper">
                        <h4>现有地址</h4>
                    </div>
                    <div class="content-wrapper">
                        <el-table
                                :data="locations"
                                border
                                height="100%"
                                style="width: 100%">
                            <el-table-column
                                    label="绝对地址"
                                    width="120">
                                <template slot-scope="scope">
                                    <i class="el-icon-location"></i>
                                    <span>
                                ({{ scope.row.longitude}},
                                {{scope.row.latitude}})
                            </span>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    min-width="300"
                                    label="详细地址">
                                <template slot-scope="scope">
                            <span style="margin-left: 10px">
                                {{ scope.row.province}}
                                {{scope.row.province?',':''}}
                                {{scope.row.city}}
                                {{scope.row.city?',':''}}
                                {{scope.row.district}}
                                {{scope.row.district?',':''}}
                                {{scope.row.street}}
                                {{scope.row.street?',':''}}
                                {{scope.row.streetNumber}}
                            </span>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    fixed="right"
                                    label="操作"
                                    width="60">
                                <template slot-scope="scope">
                                    <el-button @click="handleDelete(scope)" type="text" size="small">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="add-address">
                    <div class="title-wrapper">
                        <h4>增加地址</h4>
                    </div>
                    <div class="content-wrapper">
                        <el-form class="my-form">
                            <el-form-item>
                                <el-row>
                                    <el-col :span="11">
                                        <el-form-item label="经度" label-width="4em">
                                            <el-input type="number" v-model="addLocationForm.longitude"></el-input>
                                        </el-form-item>
                                    </el-col>
                                    <el-col class="line" :span="2">&nbsp;</el-col>
                                    <el-col :span="11">
                                        <el-form-item label="纬度" label-width="3em">
                                            <el-input type="number" v-model="addLocationForm.latitude"></el-input>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                            </el-form-item>
                            <el-form-item label="省" label-width="4em">
                                <el-input v-model="addLocationForm.province"></el-input>
                            </el-form-item>
                            <el-form-item label="市" label-width="4em">
                                <el-input v-model="addLocationForm.city"></el-input>
                            </el-form-item>
                            <el-form-item label="区" label-width="4em">
                                <el-input v-model="addLocationForm.district"></el-input>
                            </el-form-item>
                            <el-form-item label="街道" label-width="4em">
                                <el-input v-model="addLocationForm.street"></el-input>
                            </el-form-item>
                            <el-form-item label="门牌号" label-width="4em">
                                <el-col :span="20">
                                    <el-input v-model="addLocationForm.streetNumber"></el-input>
                                </el-col>
                                <el-col :span="4" style="display: flex; justify-content: center">
                                    <map-button :write-back="true" :location="addLocationForm"></map-button>
                                </el-col>
                            </el-form-item>
                            <el-form-item>
                                <div class="my-button-wrapper">
                                    <el-button type="primary" @click="addAddress">添加地址</el-button>
                                </div>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {customerGetLocation} from "../../ajax/customerAjax";
    import {logCommon} from "../../util/CommonMessageLogger";
    import {customerAddLocation, customerDeleteLocation} from "../../ajax/customerAjax";
    import {globalCheckLogin} from "../../ajax/ajaxUtil";
    import MapButton from "../../components/map/MapButton";


    export default {
        name: "AddressManagement",
        components: {MapButton},
        data() {
            return {
                locations: [],
                addLocationForm: {
                    longitude: '',
                    latitude: '',
                    province: '',
                    city: '',
                    district: '',
                    street: '',
                    streetNumber: ''
                }
            }
        },
        mounted() {
            this.getAddress();
        },
        methods: {
            handleDelete(scope) {
                let address = scope.row;
                customerDeleteLocation(address).then(response => {
                    if (logCommon(response, '删除地址')) {
                        this.getAddress();
                    } else {
                        globalCheckLogin(this);
                    }
                }).catch(err => {
                    window.console.log(err);
                    globalCheckLogin(this);
                })
            },
            addAddress() {
                customerAddLocation(this.addLocationForm).then(response => {
                    if (logCommon(response, '增加地址')) {
                        this.getAddress();
                    } else {
                        globalCheckLogin(this);
                    }
                }).catch(err => {
                    window.console.log(err);
                    globalCheckLogin(this);
                })
            },
            getAddress() {
                customerGetLocation().then(response => {
                    if (response.data) {
                        this.locations = response.data;
                    } else {
                        globalCheckLogin(this);
                    }
                }).catch(err => {
                    window.console.log(err);
                    globalCheckLogin(this);
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

    .current-address, .add-address {
        width: 80%;
        height: 45%;
        overflow: scroll;
        display: flex;
        flex-direction: column;
    }

    .content-wrapper {
        flex: 1;
        overflow: scroll;
    }

    .my-button-wrapper {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 100%;
        min-height: 40px;
    }

</style>