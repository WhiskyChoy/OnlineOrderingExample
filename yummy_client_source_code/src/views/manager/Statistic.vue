<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>统计数据</h1>
        </div>
        <div class="pane-wrapper">
            <div class="my-pane">
                <div>
                    <div><label><strong>顾客数量：</strong>{{info.customerNumber}}</label></div>
                    <div><label><strong>餐厅数量：</strong>{{info.restaurantNumber}}</label></div>
                    <div><label><strong>处理订单：</strong>{{info.orderNumber}}</label></div>
                </div>
                <div>
                    <el-button @click="render">刷新</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {getStatistic} from "../../ajax/managerAjax";

    export default {
        name: "Statistic",
        data() {
            return {
                info: {
                    customerNumber: 0,
                    restaurantNumber: 0,
                    orderNumber: 0
                }
            }
        },
        mounted() {
            this.render();
        },
        methods: {
            render() {
                getStatistic().then(response => {
                    if (response.data) {
                        this.info = response.data;
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