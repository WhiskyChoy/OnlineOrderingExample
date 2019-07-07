<template>
    <div class="outer-wrapper">
        <div class="menu-area">
            <el-menu
                    router
                    v-if="$store.state.loginState.userType==='Customer'"
                    default-active="1-1"
                    :background-color="menuBackgroundColor"
                    :text-color="textColor"
                    :active-text-color="activeTextColor">
                <el-submenu index="1">
                    <template slot="title">
                        <i class="el-icon-menu"></i>
                        <span slot="title">订购外卖</span>
                    </template>
                    <el-menu-item index="1-1" route="/customer/book/food">生成订单</el-menu-item>
                    <el-menu-item index="1-2" route="/customer/handle/order">处理订单</el-menu-item>
                </el-submenu>
                <!--<el-submenu index="2">-->
                <!--<template slot="title">-->
                <!--<i class="el-icon-document"></i>-->
                <!--<span slot="title">统计数据</span>-->
                <!--</template>-->
                <!--<el-menu-item index="2-1">点餐记录</el-menu-item>-->
                <!--<el-menu-item index="2-2">退订记录</el-menu-item>-->
                <!--<el-menu-item index="2-3">消费记录</el-menu-item>-->
                <!--</el-submenu>-->
                <!--<el-menu-item index="2">-->
                <!--<i class="el-icon-document"></i>-->
                <!--<span slot="title">统计数据</span>-->
                <!--</el-menu-item>-->
                <el-submenu index="3">
                    <template slot="title">
                        <i class="el-icon-setting"></i>
                        <span slot="title">信息管理</span>
                    </template>
                    <el-menu-item index="3-1" route="/customer/address/manage">地址管理</el-menu-item>
                    <el-menu-item index="3-2" route="/customer/personal/info">基本信息</el-menu-item>
                </el-submenu>
                <el-menu-item index="4" @click="logout" route="/login">
                    <i class="el-icon-error"></i>
                    <span slot="title">退出登录</span>
                </el-menu-item>
            </el-menu>
            <el-menu
                    router
                    v-if="$store.state.loginState.userType==='Restaurant'"
                    default-active="1-1"
                    :background-color="menuBackgroundColor"
                    :text-color="textColor"
                    :active-text-color="activeTextColor">
                <el-submenu index="1">
                    <template slot="title">
                        <i class="el-icon-menu"></i>
                        <span slot="title">发布信息</span>
                    </template>
                    <el-menu-item index="1-1" route="/restaurant/add/product">发布菜品</el-menu-item>
                </el-submenu>
                <!--<el-submenu index="2">-->
                <!--<template slot="title">-->
                <!--<i class="el-icon-document"></i>-->
                <!--<span slot="title">统计数据</span>-->
                <!--</template>-->
                <!--<el-menu-item index="2-1">点餐记录</el-menu-item>-->
                <!--<el-menu-item index="2-2">退订记录</el-menu-item>-->
                <!--<el-menu-item index="2-2">财务记录</el-menu-item>-->
                <!--</el-submenu>-->
                <el-menu-item index="2" route="/restaurant/order">
                    <i class="el-icon-document"></i>
                    <span slot="title">统计数据</span>
                </el-menu-item>
                <el-menu-item index="3" route="/restaurant/info">
                    <i class="el-icon-setting"></i>
                    <span slot="title">餐厅信息</span>
                </el-menu-item>
                <el-menu-item index="4" @click="logout" route="/login">
                    <i class="el-icon-error"></i>
                    <span slot="title">退出登录</span>
                </el-menu-item>
            </el-menu>
            <el-menu
                    router
                    v-if="$store.state.loginState.userType==='Manager'"
                    default-active="1"
                    :background-color="menuBackgroundColor"
                    :text-color="textColor"
                    :active-text-color="activeTextColor">
                <el-submenu index="1">
                    <template slot="title">
                        <i class="el-icon-menu"></i>
                        <span slot="title">审核信息</span>
                    </template>
                    <el-menu-item index="1-1" route="/manager/check/register">餐厅注册请求审核</el-menu-item>
                    <el-menu-item index="1-2" route="/manager/check/revise">餐厅信息修改审核</el-menu-item>
                </el-submenu>
                <!--<el-submenu index="2">-->
                <!--<template slot="title">-->
                <!--<i class="el-icon-document"></i>-->
                <!--<span slot="title">统计数据</span>-->
                <!--</template>-->
                <!--<el-menu-item index="2-1">会员统计</el-menu-item>-->
                <!--<el-menu-item index="2-2">餐厅统计</el-menu-item>-->
                <!--<el-menu-item index="2-2">财务记录</el-menu-item>-->
                <!--</el-submenu>-->
                <el-menu-item index="2" route="/manager/statistic">
                    <i class="el-icon-document"></i>
                    <span slot="title">统计数据</span>
                </el-menu-item>
                <el-menu-item index="3" @click="logout" route="/login">
                    <i class="el-icon-error"></i>
                    <span slot="title">退出登录</span>
                </el-menu-item>
            </el-menu>
            <!--<el-button @click="$store.state.loginState.userType='Customer'">1</el-button>-->
            <!--<el-button @click="$store.state.loginState.userType='Restaurant'">2</el-button>-->
            <!--<el-button @click="$store.state.loginState.userType='Manager'">3</el-button>-->
        </div>
        <div class="main-part-area">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    import {logout} from "../ajax/userAjax";
    import {clearLoginState} from "../util/LoginStateSetter";

    export default {
        name: "HomePage",
        data() {
            return {
                menuBackgroundColor: '#545c64',
                textColor: '#fff',
                activeTextColor: '#ffd04b'
            }
        },
        mounted() {
            if (this.$store.state.loginState.userType === 'Customer') {
                this.$router.push('/customer/book/food')
            }
            if (this.$store.state.loginState.userType === 'Restaurant') {
                this.$router.push('/restaurant/add/product')
            }
            if (this.$store.state.loginState.userType === 'Manager') {
                this.$router.push('/manager/check/register')
            }
        },
        methods: {
            logout() {
                logout().then(() => {
                    clearLoginState(this);
                })
            }
        }
    }
</script>

<style scoped>
    .outer-wrapper {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: flex-start;
        align-items: flex-start;
        flex-direction: row;
    }

    .menu-area {
        height: 100%;
        width: 30%;
        flex: 0;
        min-width: 16em;
        display: flex;
        flex-direction: column;
    }

    .menu-area > * {
        height: 80%;
        flex: 1;
    }

    .main-part-area {
        height: 100%;
        width: 70%;
        flex: 1;
        overflow: scroll;
    }


</style>