<template>
    <div class="content-wrapper">
        <div v-show="!isRegistering" class="content-wrapper">
            <div class="content-inner-wrapper">
                <h3>登入以进行餐厅管理</h3>
            </div>
            <div class="content-inner-wrapper">
                <el-form :model="loginForm" :rules="rules1" status-icon ref="loginForm" class="my-form">
                    <el-form-item prop="authnCode" label="识别码" label-width="4em">
                        <el-input placeholder="使用系统分配给您的7位识别码"
                                  type="password"
                                  v-model="loginForm.authnCode">
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <div class="my-button-wrapper">
                            <el-button type="primary" @click="restaurantLogin">以餐厅身份登入</el-button>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
            <div class="content-inner-wrapper">
                <div class="my-button-wrapper">
                    <el-button type="text" @click="isRegistering=true">还没有识别码？注册您的餐厅</el-button>
                </div>
            </div>
        </div>
        <div v-show="isRegistering" class="content-wrapper">
            <div class="content-inner-wrapper">
                <h3>在Yummy!注册您的餐厅</h3>
            </div>
            <div class="content-inner-wrapper">
                <el-form :model="registerForm" :rules="rules2" status-icon ref="RegisterForm" class="my-form">
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
                            <el-button type="primary" @click="restaurantRegister">发起餐厅注册申请</el-button>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
            <div class="content-inner-wrapper">
                <div class="my-button-wrapper">
                    <el-button type="text" @click="isRegistering=false">已有识别码？登录您的餐厅</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {registerRestaurant} from "../../ajax/restaurantAjax";
    import {setLoginState} from "../../util/LoginStateSetter";
    import {loginRestaurant} from "../../ajax/restaurantAjax";
    import MapButton from "../map/MapButton";

    export default {
        name: "RestaurantCome",
        components: {MapButton},
        data() {

            let validateAuthnCode = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('识别码不能为空'));
                    return;
                }
                if (value.length !== 7) {
                    callback(new Error('识别码的位数不对'));
                    return;
                }

                callback();
            };


            let validateName = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('餐厅名不能为空'));
                    return;
                }

                callback();
            };

            let validateEmail = (rule, value, callback) => {
                let regExp = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;

                if (!value) {
                    callback(new Error('邮箱不能为空'));
                    return;
                }

                if (!regExp.test(value)) {
                    callback(new Error('邮箱格式不正确'));
                    return;
                }

                callback();
            };

            let validateCardNumber = (rule, value, callback) => {
                let regExp = /^(\d){19}$/;

                if (!value) {
                    callback(new Error('银行卡号不能为空'));
                    return;
                }

                if (!regExp.test(value)) {
                    callback(new Error('银行卡号格式不对'));
                    return;
                }
                callback();
            };


            return {
                loginForm: {
                    authnCode: ''
                },
                registerForm: {
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
                isRegistering: false,
                rules1: {
                    authnCode: [{validator: validateAuthnCode, trigger: 'blur'}],
                },
                rules2: {
                    name: [{validator: validateName, trigger: 'blur'}],
                    email: [
                        {validator: validateEmail, trigger: 'blur'}
                    ],
                    bankAccountId: [
                        {validator: validateCardNumber, trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            restaurantLogin() {
                this.$refs['loginForm'].validate(valid => {
                    if (valid) {
                        loginRestaurant(this.loginForm).then(response => {
                            let data = response.data;
                            if (data['loggedIn']) {
                                this.$message({
                                    message: '登录成功:' + data['info'],
                                    type: 'success'
                                });
                                setLoginState(this, response.data);
                            } else {
                                this.$message({
                                    message: '登录失败:' + data['info'],
                                    type: 'error'
                                })
                            }
                        }).catch(e => {
                            window.console.log(e);
                        })
                    }
                })
            },
            restaurantRegister() {
                this.$refs['RegisterForm'].validate(valid => {
                    if (valid) {
                        registerRestaurant(this.registerForm).then(response => {
                            let data = response.data;
                            if (data['successful']) {
                                this.$message({
                                    message: '注册信息投递成功:' + data['info'],
                                    type: 'success'
                                })
                            } else {
                                this.$message({
                                    message: '注册信息投递失败:' + data['info'],
                                    type: 'error'
                                })
                            }
                        }).catch(e => {
                            window.console.log(e);
                        })
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .my-form {
        width: 100%;
        max-width: 30em
    }

    .my-button-wrapper {
        display: flex;
        width: 100%;
        height: 100%;
        min-height: 40px;
        justify-content: center;
        align-items: center;
    }
</style>