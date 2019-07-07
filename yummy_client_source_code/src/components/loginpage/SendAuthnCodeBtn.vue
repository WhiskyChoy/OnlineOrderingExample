<template>
    <div>
        <el-button v-if="canSend" @click="getAuthnCode" type="primary">获取登录码</el-button>
        <el-button v-if="!canSend" disabled>{{countDownMessage}}</el-button>
    </div>
</template>

<script>
    import {getAuthnCode} from "../../ajax/customerAjax";

    export default {
        name: "SendAuthnCodeBtn",
        props: {
            email: {
                type: String,
                require: true
            },
            asyncValidFuncGetter: {
                type: Function,
                require: true
            },
            totalWaitTimeInSecond:{
                type:Number,
                default: 60
            }
        },
        data() {
            return {
                timeCounter: 0,
                canSend: true
            }
        },
        methods: {
            getAuthnCode() {
                //asyncValidFuncGetter is a closure
                this.asyncValidFuncGetter()((isValid) => {
                    if (isValid) {
                        getAuthnCode({email: this.email}).then(response => {
                            let valid = response.data['successful'];
                            let info = response.data['info'];
                            if (valid) {
                                this.$message({
                                    message: '发送成功:' + info,
                                    type: 'success'
                                });
                                this.startCountDown();
                            } else {
                                this.$message({
                                    message: '发送失败：' + info,
                                    type: 'error'
                                })
                            }
                        })
                    } else {
                        this.$message({
                            message: '请正确填写邮箱及验证码',
                            type: 'error'
                        })
                    }
                });
            },

            startCountDown() {
                this.timeCounter = this.totalWaitTimeInSecond;
                this.canSend = false;
                let i = setInterval(() => {
                    if (this.timeCounter > 1) {
                        this.timeCounter--;
                    } else {
                        window.clearInterval(i);
                        this.canSend = true;
                    }
                }, 1000);
            }
        },
        computed: {
            countDownMessage() {
                return `${this.timeCounter}秒后可发送`
            },
        }
    }
</script>

<style scoped>

</style>