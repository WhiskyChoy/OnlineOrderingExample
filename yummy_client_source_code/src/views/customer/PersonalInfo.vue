<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>基本信息</h1>
        </div>
        <div class="pane-wrapper">
            <div class="my-pane">
                <el-form style="width: 65%;display: block">
                    <el-form-item label="用户登记">{{info.level}}</el-form-item>
                    <el-form-item label="用户姓名">
                        <el-input v-model="info.name"></el-input>
                    </el-form-item>
                    <el-form-item label="用户邮箱">{{info.email}}</el-form-item>
                    <el-form-item label="银行账户">
                        <el-input v-model="info.bankAccountId"></el-input>
                    </el-form-item>
                </el-form>
                <div>
                    <el-button @click="revise">修改</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {getInfo, setInfo} from "../../ajax/customerAjax";
    import {logCommon} from "../../util/CommonMessageLogger";

    export default {
        name: "PersonalInfo",
        data() {
            return {
                info: {
                    level: 1,
                    name: '',
                    email: '',
                    bankAccountId: ''
                }
            }
        },
        mounted() {
            this.render();
        },
        methods: {
            revise() {
                setInfo(this.info).then(response => {
                    if (logCommon(response, '修改个人信息')) {
                        this.render();
                    }
                })
            },
            render() {
                getInfo().then(response => {
                    if (response['data']) {
                        this.info = response['data'];
                    }
                }).catch(err => {
                    window.console.log(err);
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
        justify-content: center;
        align-items: center;
        overflow: scroll;
        flex-direction: column;
    }
</style>