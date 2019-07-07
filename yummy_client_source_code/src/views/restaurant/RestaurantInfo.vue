<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>餐厅信息</h1>
        </div>
        <div class="pane-wrapper">
            <div class="my-pane">
                <el-form :model="registerForm" class="my-form">
                    <el-form-item prop="name" label="餐厅名字" label-width="6em">
                        <el-input placeholder="请输入您的餐厅名字"
                                  v-model="registerForm.name">
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="email" label="通知邮箱" label-width="6em">
                        <el-input placeholder="请注意邮箱格式"
                                  v-model="registerForm.email">
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="bankAccountId" label="银行卡号" label-width="6em">
                        <el-input placeholder="请输入要绑定的银行卡号（19位）"
                                  v-model="registerForm.bankAccountId">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="地址信息" label-width="6em">
                        <el-row>
                            <el-col :span="11">
                                <el-form-item label="经度" label-width="4em">
                                    <el-input type="number" v-model="registerForm.location.longitude"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col class="line" :span="2">&nbsp;</el-col>
                            <el-col :span="11">
                                <el-form-item label="纬度" label-width="3em">
                                    <el-input type="number" v-model="registerForm.location.latitude"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-form-item label="省" label-width="4em">
                            <el-input v-model="registerForm.location.province"></el-input>
                        </el-form-item>
                        <el-form-item label="市" label-width="4em">
                            <el-input v-model="registerForm.location.city"></el-input>
                        </el-form-item>
                        <el-form-item label="区" label-width="4em">
                            <el-input v-model="registerForm.location.district"></el-input>
                        </el-form-item>
                        <el-form-item label="街道" label-width="4em">
                            <el-input v-model="registerForm.location.street"></el-input>
                        </el-form-item>
                        <el-form-item label="门牌号" label-width="4em">
                            <el-col :span="20">
                                <el-input v-model="registerForm.location.streetNumber"></el-input>
                            </el-col>
                            <el-col :span="4" style="display: flex; justify-content: center">
                                <map-button :write-back="true" :location="registerForm.location"></map-button>
                            </el-col>
                        </el-form-item>
                    </el-form-item>
                    <el-form-item>
                        <div class="my-button-wrapper">
                            <el-button type="primary" @click="reviseRestaurant">修改餐厅信息</el-button>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    import MapButton from "../../components/map/MapButton";
    import {getRestaurantInfo, reviseRestaurant} from "../../ajax/restaurantAjax";
    import {logCommon} from "../../util/CommonMessageLogger";

    export default {
        name: "RestaurantInfo",
        components: {MapButton},
        data() {
            return {
                registerForm: {
                    restaurantId: 0,
                    name: '',
                    email: '',
                    bankAccountId: '',
                    location: {
                        longitude: 0,
                        latitude: 0,
                        province: '',
                        city: '',
                        district: '',
                        street: '',
                        streetNumber: ''
                    }
                },
            }
        },
        methods: {
            render() {
                getRestaurantInfo().then(response => {
                    if (response['data']) {
                        this.registerForm = response['data'];
                    }
                }).catch(err => {
                    window.console.log(err);
                })
            },
            reviseRestaurant() {
                reviseRestaurant(this.registerForm).then(response => {
                    logCommon(response, '修改餐厅信息')
                }).catch(err => {
                    window.console.log(err);
                })
            }
        },
        mounted() {
            this.render();
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
        justify-content: center;
        align-items: center;
        overflow: scroll;
        flex-direction: column;
    }

    .my-form {
        width: 100%;
        max-width: 30em
    }
</style>