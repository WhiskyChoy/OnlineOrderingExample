<template>
    <div class="outer-wrapper">
        <div class="title-wrapper">
            <h1>Yummy!登录与注册</h1>
        </div>
        <div class="tab-wrapper">
            <el-tabs type="border-card" class="tab">
                <el-tab-pane label="顾客">
                    <customer-come></customer-come>
                </el-tab-pane>
                <el-tab-pane label="餐厅">
                    <div class="content-wrapper">
                        <restaurant-come></restaurant-come>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="经理">
                    <manager-come></manager-come>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
    import CustomerCome from "../components/loginpage/CustomerCome";
    import ManagerCome from "../components/loginpage/ManagerCome";
    import RestaurantCome from "../components/loginpage/RestaurantCome";

    export default {
        name: "LoginPage",
        components: {RestaurantCome, ManagerCome, CustomerCome},
        mounted() {
            this.$router.push('/login')
        }
    }
</script>

<style>
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