<template>
    <div class="content-wrapper">
        <div class="content-inner-wrapper">
            <h3>登入以进行订餐操作</h3>
        </div>
        <div class="content-inner-wrapper">
            <el-form :model="loginForm" :rules="rules1" status-icon ref="loginForm1" class="my-form">
                <el-form-item prop="email" label="绑定邮箱" label-width="6em">
                    <el-input placeholder="请注意邮箱格式"
                              v-model="loginForm.email">
                    </el-input>
                </el-form-item>
                <el-form-item prop="verCode" label="验证码" label-width="6em">
                    <el-col :span="16">
                        <el-input placeholder="不区分大小写"
                                  ref="refCheck"
                                  v-model="loginForm.verCode">
                        </el-input>
                    </el-col>
                    <el-col :span="8">
                        <div class="ver-outer-saver">
                            <div :id="myVerifierId"
                                 class="ver-inner-saver"></div>
                        </div>
                    </el-col>
                </el-form-item>
            </el-form>
        </div>
        <div class="content-inner-wrapper">
            <el-form class="my-form" :model="loginForm" :rules="rules2" status-icon ref="loginForm2">
                <el-form-item prop="authnCode" label="登录码" label-width="6em">
                    <el-col :span="14">
                        <el-input :placeholder="`请从邮箱取得(${authnCodeLength}位)`"
                                  v-model="loginForm.authnCode">
                        </el-input>
                    </el-col>
                    <el-col :span="10">
                        <div class="send-authn-outer-saver">
                            <send-authn-code-btn :email="loginForm.email"
                                                 :total-wait-time-in-second="totalWaitTimeInSecond"
                                                 :async-valid-func-getter="asyncValidFuncGetter">
                            </send-authn-code-btn>
                        </div>
                    </el-col>
                </el-form-item>
                <el-form-item v-if="!customerExisted" prop="name" label="姓名" label-width="6em">
                    <el-input placeholder="请输入您的姓名"
                              v-model="loginForm.name">
                    </el-input>
                </el-form-item>
                <el-form-item v-if="!customerExisted" prop="bankAccountId" label="银行卡号" label-width="6em">
                    <el-input placeholder="请输入要绑定的银行卡号（19位）"
                              v-model="loginForm.bankAccountId">
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <div class="login-button-wrapper">
                        <el-button @click="doActualLogin">以顾客身份登入</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import {MyVerifier} from "../../util/MyVerifier";
    import SendAuthnCodeBtn from "./SendAuthnCodeBtn";
    import {getCustLogMeta} from "../../ajax/customerAjax";
    import {setLoginState} from '../../util/LoginStateSetter';
    import {checkCustomerExisted, doActualLogin} from "../../ajax/customerAjax";

    export default {
        name: "CustomerCome",
        components: {SendAuthnCodeBtn},
        data() {
            let validateEmail = (rule, value, callback) => {
                let regExp = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;

                if (!value) {
                    callback(new Error('邮箱不能为空'));
                    this.customerExisted = true;
                    return;
                }

                if (!regExp.test(value)) {
                    callback(new Error('邮箱格式不正确'));
                    this.customerExisted = true;
                    return;
                }

                checkCustomerExisted({email: value}).then(response => {
                    this.customerExisted = response.data['existed']
                });

                callback();
            };

            let validateVercode = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('验证码不能为空'));
                    return;
                }

                if (!this.myVerifier.validate(value)) {
                    callback(new Error('验证码不正确'));
                    return;
                }

                callback();

            };

            let validateAuthnCode = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('登陆码不能为空'));
                    return;
                }

                if (value.length !== 4) {
                    callback(new Error('登陆码位数不对'));
                    return;
                }

                callback();
            };

            let validateName = (rule, value, callback) => {
                if (this.customerExisted) {
                    callback();
                }

                if (!value) {
                    callback(new Error('用户名不能为空'));
                    return;
                }
                callback();
            };

            let validateCardNumber = (rule, value, callback) => {
                if (this.customerExisted) {
                    callback();
                }

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
                customerExisted: true,
                myVerifierId: 'v_container',
                myVerifier: null,
                authnCodeLength: 4,
                totalWaitTimeInSecond: 60,
                loginForm: {
                    email: '',
                    verCode: '',
                    authnCode: '',
                    name: '',
                    bankAccountId: ''
                },
                rules1: {
                    email: [
                        {validator: validateEmail, trigger: 'blur'}
                    ],
                    verCode: [
                        {validator: validateVercode, trigger: 'blur'}
                    ],
                },
                rules2: {
                    authnCode: [
                        {validator: validateAuthnCode, trigger: 'blur'}
                    ],
                    name: [
                        {validator: validateName, trigger: 'blur'}
                    ],
                    bankAccountId: [
                        {validator: validateCardNumber, trigger: 'blur'}
                    ]
                }
            }
        },
        mounted() {
            this.myVerifier = new MyVerifier(this.myVerifierId);
            this.getCustLoginMeta();
        },
        methods: {
            asyncValidFuncGetter() {
                return this.$refs['loginForm1'].validate;
            },
            getCustLoginMeta() {
                getCustLogMeta().then(response => {
                    this.authnCodeLength = response.data['authnCodeLength'];
                    this.totalWaitTimeInSecond = response.data['totalWaitTime'] * 60;
                }).catch(() => {
                    this.$message({
                        message: '初始化失败',
                        type: 'error'
                    })
                })
            },
            doActualLogin() {
                this.$refs['loginForm1'].validate(outerValid => {
                    if (outerValid) {
                        this.$refs['loginForm2'].validate((valid) => {
                            if (valid) {
                                doActualLogin({
                                    email: this.loginForm.email,
                                    authnCode: this.loginForm.authnCode,
                                    name: this.loginForm.name,
                                    bankAccountId: this.loginForm.bankAccountId
                                }).then(response => {
                                    let data = response.data;
                                    if (data.loggedIn === true) {
                                        this.$message({
                                            message: '登录成功',
                                            type: 'success'
                                        });
                                        setLoginState(this, response.data);
                                    } else {
                                        this.$message({
                                            message: '登录失败：' + data.info,
                                            type: 'error'
                                        });
                                    }
                                }).catch(e => {
                                    window.console.log(e)
                                })
                            }
                        });
                    }
                });

            }
        }
    }
</script>

<style scoped>
    .ver-outer-saver, .send-authn-outer-saver, .login-button-wrapper {
        display: flex;
        width: 100%;
        height: 100%;
        min-height: 40px;
        justify-content: center;
        align-items: center;
    }

    .ver-inner-saver {
        width: 65%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .my-form {
        width: 100%;
        max-width: 30em
    }
</style>