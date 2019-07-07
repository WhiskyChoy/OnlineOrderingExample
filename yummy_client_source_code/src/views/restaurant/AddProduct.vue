<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>发布菜品</h1>
        </div>
        <div class="pane-wrapper">
            <div class="my-pane">
                <el-form class="my-form">
                    <el-form-item label="菜品名" label-width="5em">
                        <el-input v-model="addProductForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="描述" label-width="5em">
                        <el-input v-model="addProductForm.description"
                                  type="textarea"
                                  :rows="2">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="类型" label-width="5em">
                        <el-select v-model="addProductForm.type"
                                   style="width: 100%; max-width: 22em"
                                   placeholder="请选择">
                            <el-option
                                    v-for="item in types"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="时间范围" label-width="5em">
                        <el-date-picker type="daterange"
                                        unlink-panels
                                        v-model="addProductForm.dateRange"
                                        range-separator="~"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                        value-format="yyyy-MM-dd"
                                        style="width: 100%;">
                        </el-date-picker>
                        <!--<span>{{addProductForm.dateRange}}</span>-->
                    </el-form-item>
                    <el-form-item label="价格" label-width="5em">
                        <el-input type="number" v-model="addProductForm.price"></el-input>
                    </el-form-item>
                    <el-form-item label="折扣" label-width="5em">
                        <el-input-number v-model="addProductForm.rate" :precision="1" :step="0.1"
                                         :max="1"></el-input-number>
                    </el-form-item>
                    <el-form-item label="起始库存" label-width="5em">
                        <el-input type="number" v-model="addProductForm.amount"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <div class="my-button-wrapper">
                            <el-button @click="addProduct" type="primary">增加菜品</el-button>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    import {restaurantAddProduct} from "../../ajax/restaurantAjax";
    import {logCommon} from "../../util/CommonMessageLogger";
    import {globalCheckLogin} from "../../ajax/ajaxUtil";

    let convert = (form) => {
        return {
            name: form.name,
            type: form.type,
            rate: form.rate,
            startDate: form.dateRange[0],
            endDate: form.dateRange[1],
            description: form.description,
            price: form.price,
            amount: form.amount
        }
    };

    export default {
        name: "AddProduct",
        data() {
            return {
                types: [
                    {
                        value: 'single',
                        label: '单品'
                    },
                    {
                        value: 'combo',
                        label: '套餐'
                    }
                ],
                addProductForm: {
                    name: '',
                    type: '',
                    dateRange: '',
                    description: '',
                    price: 0,
                    amount: 0,
                    rate: 1.0
                }
            }
        },
        methods: {
            addProduct() {
                let data = convert(this.addProductForm);
                restaurantAddProduct(data).then(response => {
                        let bool = logCommon(response, '增加菜品');
                        if (!bool) {
                            globalCheckLogin(this);
                        }
                    }
                ).catch(err => {
                    window.console.log(err);
                    globalCheckLogin(this);
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
    }

    .my-form {
        width: 70%;
        height: 60%;
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