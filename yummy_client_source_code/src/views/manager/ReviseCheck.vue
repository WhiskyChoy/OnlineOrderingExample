<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>餐厅修改信息审核</h1>
        </div>
        <div class="pane-wrapper">
            <div class="my-pane">
                <el-table
                        :data="tableData"
                        border
                        height="100%"
                        style="width: 100%">
                    <el-table-column
                            prop="name"
                            label="餐厅名"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="email"
                            label="联系邮箱"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            label="绝对地址"
                            width="120">
                        <template slot-scope="scope">
                            <i class="el-icon-location"></i>
                            <span>
                                ({{ scope.row.location.longitude}},
                                {{scope.row.location.latitude}})
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="详细地址"
                            width="300">
                        <template slot-scope="scope">
                            <span style="margin-left: 10px">
                                {{ scope.row.location.province}}
                                {{scope.row.location.province?',':''}}
                                {{scope.row.location.city}}
                                {{scope.row.location.city?',':''}}
                                {{scope.row.location.district}}
                                {{scope.row.location.district?',':''}}
                                {{scope.row.location.street}}
                                {{scope.row.location.street?',':''}}
                                {{scope.row.location.streetNumber}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            fixed="right"
                            label="操作"
                            width="100">
                        <template slot-scope="scope">
                            <el-button @click="handlePass(scope)" type="text" size="small">通过</el-button>
                            <el-button @click="handleReject(scope)" type="text" size="small">拒绝</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>

<script>
    import {approveRevise, disapproveRevise, getReviseList} from "../../ajax/managerAjax";

    export default {
        name: "ReviseCheck",
        methods: {
            handlePass(scope) {
                approveRevise({restaurantId: scope.row.restaurantId}).then(response => {
                    if (response.data['successful']) {
                        this.$message({
                            message: '审批通过成功：' + response.data['info'],
                            type: 'success'
                        });
                        this.tableData.splice(scope.$index, 1)
                    } else {
                        this.$message({
                            message: '审批通过失败：' + response.data['info'],
                            type: 'error'
                        })
                    }
                }).catch(err => {
                    window.console.log(err);
                });
            },
            handleReject(scope) {
                disapproveRevise({restaurantId: scope.row.restaurantId}).then(response => {
                    if (response.data['successful']) {
                        this.$message({
                            message: '审批拒绝成功：' + response.data['info'],
                            type: 'success'
                        });
                        this.tableData.splice(scope.$index, 1)
                    } else {
                        this.$message({
                            message: '审批拒绝失败：' + response.data['info'],
                            type: 'error'
                        })
                    }
                }).catch(err => {
                    window.console.log(err);
                });
            },
            render() {
                getReviseList().then(response => {
                    this.tableData = response.data;
                }).catch(err => {
                    window.console.log(err);
                })
            }
        },
        data() {
            return {
                tableData: [{
                    id: '',
                    registerDate: '',
                    name: '',
                    email: '',
                    location: {
                        longitude: '',
                        latitude: '',
                        province: '',
                        city: '',
                        district: '',
                        street: '',
                        streetNumber: ''
                    }
                },]
            }
        },
        mounted() {
            this.render();
        },
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
        width: 70%;
        flex: 1;
    }

    .my-pane {
        height: 90%;
        border: 1px solid #dcdfe6;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .12), 0 0 6px 0 rgba(0, 0, 0, .04);
    }
</style>