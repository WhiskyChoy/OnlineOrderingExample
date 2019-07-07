<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>选择菜品</h1>
        </div>
        <div class="pane-wrapper">
            <div class="my-pane">
                <div class="title-wrapper">
                    <h4>{{restaurantName}}可选菜品</h4>
                </div>
                <div class="select-food">
                    <div class="content-wrapper">
                        <el-table :data="foodProducts" style="width: 100%; height: 80%">
                            <el-table-column label="菜品名" prop="name" fixed="left" min-width="80"></el-table-column>
                            <el-table-column label="描述" prop="description" min-width="160"></el-table-column>
                            <el-table-column label="类型" prop="type" min-width="60"
                                             :formatter="(row)=>{if(row.type==='single')return '单品'; if(row.type==='combo') return '套餐'; return '未定义'}"></el-table-column>
                            <el-table-column label="价格" prop="price" min-width="60"></el-table-column>
                            <el-table-column label="折扣" prop="rate" min-width="80"
                                             :formatter="(row)=>{return row.rate===1?'不打折':row.rate*10+'折'}"></el-table-column>
                            <el-table-column label="起始时间" prop="startDate" min-width="120"></el-table-column>
                            <el-table-column label="结束时间" prop="endDate" min-width="120"></el-table-column>
                            <el-table-column label="操作" fixed="right" min-width="140">
                                <template slot-scope="scope">
                                    <el-input-number v-model="scope.row.selectNumber"
                                                     size="mini"
                                                     @change="(value)=>{handleChange(scope.row, value)}" :min="0"
                                                     label="描述文字"></el-input-number>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="my-operation">
                    <el-button @click="goBack">选择餐厅</el-button>
                    <el-popover
                            placement="top"
                            width="400"
                            trigger="click">
                        <el-table :data="order">
                            <el-table-column label="菜品名" prop="productName" min-width="80"></el-table-column>
                            <el-table-column label="订购数量" prop="orderNumber" min-width="80"></el-table-column>
                        </el-table>
                        <el-badge :value="order.length+'种菜品'" slot="reference">
                            <el-button icon="el-icon-goods" type="warning">{{totalPrice}}元</el-button>
                        </el-badge>
                    </el-popover>
                    <el-button type="primary" @click="handleGenerate">生成订单</el-button>

                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {customerFindFood, addOrderWait} from "../../ajax/customerAjax";
    import {globalCheckLogin} from "../../ajax/ajaxUtil";
    import {logCommon} from "../../util/CommonMessageLogger";

    export default {
        name: "SelectFood",
        data() {
            return {
                restaurantName: '',
                restaurantId: 0,
                customerLocationId: 0,
                foodProducts: [],
                order: [],
                totalPrice: 0
            }
        },
        mounted() {
            let restaurantId = this.$route.params['restaurantId'];
            let restaurantName = this.$route.params['restaurantName'];
            let customerLocationId = this.$route.params['customerLocationId'];
            if (!restaurantId || !restaurantName || !customerLocationId) {
                this.goBack();
            }
            this.restaurantId = restaurantId;
            this.restaurantName = restaurantName;
            this.customerLocationId = customerLocationId;
            this.getSelectableFood();
        },
        methods: {
            goBack() {
                this.$router.push('/customer/book/food');
            },
            getSelectableFood() {
                customerFindFood({restaurantId: this.restaurantId}).then(response => {
                    let foodProducts = response.data;
                    for (let foodProduct of foodProducts) {
                        foodProduct.selectNumber = 0;
                    }
                    this.foodProducts = foodProducts;
                }).catch(err => {
                    window.console.log(err);
                    globalCheckLogin(this);
                })
            },
            handleChange(row, value) {
                let found = false;
                for (let i = 0; i < this.order.length; i++) {
                    if (this.order[i].productId === row.id) {
                        if (value > this.order[i].orderNumber) {
                            this.totalPrice += row.price * row.rate;
                        } else {
                            this.totalPrice -= row.price * row.rate;
                        }

                        if (value > 0) {
                            this.order[i].orderNumber = value;
                        }
                        if (value === 0) {
                            this.order.splice(i, 1);
                            i--;
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    this.order.push({productId: row.id, orderNumber: value, productName: row.name});
                    this.totalPrice += row.price * row.rate;
                }
                window.console.log(this.totalPrice);
            },
            handleGenerate() {
                let data = {
                    restaurantId: this.restaurantId,
                    customerLocationId: this.customerLocationId,
                    orderItems: this.order
                };
                // window.console.log(data);
                addOrderWait(data).then(response => {
                    if (!logCommon(response, '生成订单')) {
                        globalCheckLogin(this);
                    } else {
                        this.goHandleOrder();
                    }
                }).catch(err => {
                    window.console.log(err);
                    globalCheckLogin(this);
                })
            },
            goHandleOrder() {
                this.$router.push('/customer/handle/order')
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

    .select-food, .my-operation {
        width: 80%;
        overflow: scroll;
        display: flex;
    }

    .select-food {
        flex-direction: column;
    }

    .my-operation {
        flex-direction: row;
        justify-content: space-around;
        align-items: center;
    }

    .select-food {
        height: 80%;
    }


    .my-operation {
        height: 20%;
    }

    .content-wrapper {
        flex: 1;
        display: flex;
        flex-direction: row;
        align-items: center;
    }
</style>