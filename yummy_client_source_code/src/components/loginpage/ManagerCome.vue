<template>
    <div class="content-wrapper">
        <div class="content-inner-wrapper">
            <h3>登入以进行系统管理</h3>
        </div>
        <div class="content-inner-wrapper">
            <el-form :model="loginForm" :rules="rules1" status-icon ref="loginForm" class="my-form">
                <el-form-item prop="username" label="用户名" label-width="4em">
                    <el-input placeholder="系统预生成的用户名"
                              v-model="loginForm.username">
                    </el-input>
                </el-form-item>
                <el-form-item prop="passcode" label="密码" label-width="4em">
                    <el-input placeholder="系统预生成的密码"
                              type="password"
                              v-model="loginForm.passcode">
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <div class="login-button-wrapper">
                        <el-button @click="doManagerLogin">以经理身份登入</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import {managerLogin} from "../../ajax/managerAjax";
    import {setLoginState} from "../../util/LoginStateSetter";

    export default {
        name: "ManagerCome",
        data() {
            let validateUsername = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('用户名不能为空'));
                    return;
                }
                callback();
            };

            let validatePasscode = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('密码不能为空'));
                    return;
                }
                callback();
            };

            return {
                loginForm: {
                    username: '',
                    passcode: ''
                },

                rules1: {
                    username: [
                        {validator: validateUsername, trigger: 'blur'}
                    ],
                    passcode: [
                        {validator: validatePasscode, trigger: 'blur'}
                    ],
                },
            }
        },
        methods: {
            doManagerLogin() {
                this.$refs['loginForm'].validate(valid => {
                    if (valid) {
                        managerLogin(this.loginForm).then(response => {
                            setLoginState(this, response.data)
                        }).catch(() => {
                            window.console.log(e)
                        })
                    }
                });

            }
        }
    }
</script>

<style scoped>
    .my-form {
        width: 100%;
        max-width: 20em
    }

    .login-button-wrapper {
        display: flex;
        width: 100%;
        height: 100%;
        min-height: 40px;
        justify-content: center;
        align-items: center;
    }
</style>