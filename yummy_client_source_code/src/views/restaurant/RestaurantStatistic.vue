<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>处理订单</h1>
        </div>
        <div class="pane-wrapper">
            <div class="my-pane">
                <el-table style="width: 100%" height="100%" :data="orders">
                    <el-table-column label="餐厅名"
                                     fixed="left"
                                     min-width="120"
                                     :formatter="(row)=>{return row.restaurant.name}"></el-table-column>
                    <el-table-column label="送出地点"
                                     min-width="300"
                                     :formatter="(row)=>{return getDetailLocation(row.from)}"></el-table-column>
                    <el-table-column label="目标地点" prop="to"
                                     min-width="300"
                                     :formatter="(row)=>{return getDetailLocation(row.to)}"></el-table-column>
                    <el-table-column label="实际付款" prop="finalCost" min-width="120"></el-table-column>
                    <el-table-column label="订单状态" min-width="300"
                                     :formatter="(row)=>{return getStateFoodOrder(row)}"></el-table-column>
                    <el-table-column label="操作" align="center" fixed="right" min-width="100">
                        <template slot-scope="scope">
                            <el-button @click="()=>{handleDetail(scope.row)}" size="mini">详单</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <el-dialog :visible.sync="dialogVisible" :modal-append-to-body='false' title="订单详情" center>
            <el-table :data="currentOrder.orderItems">
                <el-table-column label="菜品名" min-width="80" sortable
                                 :formatter="(row)=>{return row.foodProduct.name}"></el-table-column>
                <el-table-column label="单价" min-width="80" sortable
                                 :formatter="(row)=>{return row.foodProduct.price}"></el-table-column>
                <el-table-column label="折扣" min-width="80" sortable
                                 :formatter="(row)=>{return row.foodProduct.rate===1?'不打折':row.foodProduct.rate}"></el-table-column>
                <el-table-column label="订购数量"  sortable prop="productNumber" min-width="80"></el-table-column>
            </el-table>
            <div slot="footer">
                <label>总额：{{currentOrder.finalCost}}</label>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {getRestFoodOrder} from "../../ajax/restaurantAjax";
    import {globalCheckLogin} from "../../ajax/ajaxUtil";
    import {getDetailLocation} from "../../ajax/locationUtil";
    import {getStateFoodOrder} from "../../ajax/foodOrderUtil";

    export default {
        name: "RestaurantStatistic",
        data() {
            return {
                dialogVisible: false,
                orders: [],
                currentOrder: {
                    orderItems: [],
                    finalCost: 0
                }
            }
        },
        mounted() {
            this.loadAllOrder();
        },
        methods: {
            loadAllOrder() {
                getRestFoodOrder().then(response => {
                    this.orders = response.data;
                }).catch(err => {
                    window.console.log(err);
                    globalCheckLogin(this);
                })
            },
            getDetailLocation,
            getStateFoodOrder,
            handleDetail(order) {
                this.dialogVisible = true;
                this.currentOrder = order;
            },
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
        overflow: scroll;
    }
</style>