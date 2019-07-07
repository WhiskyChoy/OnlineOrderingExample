<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>Yummy!对接银行系统</h1>
        </div>
        <div class="tab-wrapper">
            <el-tabs type="border-card" class="tab">
                <el-tab-pane label="账户审查与编辑">
                    <div class="content-wrapper">
                        <div class="content-inner-wrapper">
                            <h3>系统所有账户</h3>
                        </div>
                        <div class="content-inner-wrapper">
                            <el-table :data="bankAccounts" stripe height="400">
                                <el-table-column
                                        prop="id"
                                        label="银行卡账号"
                                        style="width: 50%">
                                </el-table-column>
                                <el-table-column
                                        prop="balance"
                                        label="银行卡余额"
                                        style="width: 50%">
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="账户添加与批量添加">
                    <div class="content-wrapper">
                        <div class="content-inner-wrapper">
                            <el-form label-width="80px" inline>
                                <el-form-item label="银行账号">
                                    <el-input v-model="currentAccount.id"></el-input>
                                </el-form-item>
                                <el-form-item label="初始余额">
                                    <el-input v-model="currentAccount.balance"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <el-button type="primary" @click="onRandomGenerate">随机生成</el-button>
                                </el-form-item>
                                <el-form-item>
                                    <el-button @click="onPutToAdd">加入待提交</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                        <div class="content-inner-wrapper" v-show="accountsToAdd.length>0">
                            <h3>待添加的账户</h3>
                        </div>
                        <div class="content-inner-wrapper" v-show="accountsToAdd.length>0">
                            <el-table :data="accountsToAdd" stripe height="400">
                                <el-table-column
                                        prop="id"
                                        label="银行账号"
                                        style="width: 45%">
                                </el-table-column>
                                <el-table-column
                                        prop="balance"
                                        label="初始余额"
                                        style="width: 45%">
                                </el-table-column>
                                <el-table-column
                                        fixed="right"
                                        prop="balance"
                                        label="初始余额"
                                        style="width: 10%">
                                    <template slot-scope="scope">
                                        <el-button
                                                @click.native.prevent="onDeleteRow(scope.$index)"
                                                type="text">
                                            移除
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                        <div class="content-inner-wrapper">
                            <el-form label-width="80px" inline>
                                <el-form-item>
                                    <el-button type="primary" @click="onClearAll" v-show="accountsToAdd.length>0">全部清除</el-button>
                                </el-form-item>
                                <el-form-item>
                                    <el-button @click="onAddAll" v-show="accountsToAdd.length>0">全部提交</el-button>
                                </el-form-item>
                                <el-form-item>
                                    <label>{{successfulNum}}个账户信息提交成功</label>
                                </el-form-item>
                                <el-form-item>
                                    <label>{{failNum}}个账户信息提交失败</label>
                                </el-form-item>
                            </el-form>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
    import {getAllAccounts, addOneAccount} from "./ajax/bankAjax";

    let account_num = 19;

    let getRandomAccount = () => {
        let prefix = "6220";
        let bound = account_num - prefix.length;
        let id = prefix;
        for (let i = 0; i < bound; i++) {
            id += ("" + Math.floor(Math.random() * 10))
        }
        let balance = (100 + 1000 * Math.random()).toFixed(2);
        return {id, balance}
    };

    export default {
        name: "App",
        data() {
            return {
                isLogin: false,
                bankAccounts: [],
                accountsToAdd: [],
                currentAccount: {
                    id: '',
                    balance: 0
                },
                successfulNum: 0,
                failNum: 0
            }
        },
        mounted() {
            this.getAccounts();
        },
        methods: {
            getAccounts() {
                getAllAccounts().then(response => this.bankAccounts = response.data['bankAccounts'])
            },
            onDeleteRow(index) {
                this.accountsToAdd.splice(index, 1);
            },
            onRandomGenerate() {
                this.currentAccount = getRandomAccount();
            },
            onPutToAdd() {
                if (!(this.currentAccount.id && this.currentAccount.balance > 0)) {
                    this.$message({
                        message: '您尚未输入数据',
                        type: 'warning'
                    });
                    return;
                }
                if (!(this.bankAccounts.indexOf(this.currentAccount) === -1)) {
                    this.$message({
                        message: '在已有账户中检测到该账户',
                        type: 'warning'
                    });
                    return;
                }
                if (!(this.accountsToAdd.indexOf(this.currentAccount) === -1)) {
                    this.$message({
                        message: '账户已在待添加中',
                        type: 'warning'
                    });
                    return;
                }
                if (!(this.currentAccount.id.length === account_num)) {
                    this.$message({
                        message: '账户名长度不对',
                        type: 'warning'
                    });
                    return;
                }

                this.accountsToAdd.push(this.currentAccount);
            },
            onAddAll() {
                this.successfulNum = 0;
                this.failNum = 0;
                for (let account of this.accountsToAdd) {
                    addOneAccount(account).then(response => {
                        if (response.data['successful']) {
                            this.successfulNum++;
                        } else {
                            this.failNum++;
                            this.$message({
                                message: response.data['info'],
                                type: 'error'
                            });
                        }

                    }).catch(err => {
                        window.console.log(err)
                    });
                }
                this.onClearAll();
                this.getAccounts();
                this.currentAccount = {
                    id: '',
                    balance: 0
                };
            },
            onClearAll() {
                this.accountsToAdd = [];
            }
        }
    }
</script>

<style scoped>
    .outer-wrapper {
        display: flex;
        flex-direction: column;
        position: fixed;
        left: 0;
        right: 0;
        bottom: 0;
        top: 0;
        align-items: center;
    }

    .tab-wrapper {
        width: 70%;
        flex: 1;
    }

    .tab {
        height: 90%;
    }

    .content-inner-wrapper {
        display: flex;
        width: 65%;
        justify-content: center;
    }

    .content-wrapper, .title-wrapper {
        display: flex;
        width: 100%;
        align-items: center;
        flex-direction: column;
    }
</style>