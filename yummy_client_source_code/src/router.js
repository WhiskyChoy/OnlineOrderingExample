import Vue from 'vue'
import Router from 'vue-router'
import ReviseCheck from './views/manager/ReviseCheck'
import AddProduct from "./views/restaurant/AddProduct";
import PersonalInfo from "./views/customer/PersonalInfo";
import AddressManagement from "./views/customer/AddressManagement";
import BookFood from "./views/customer/BookFood";
import SelectFood from "./views/customer/SelectFood";
import HandleOrder from "./views/customer/HandleOrder";
import RestaurantInfo from "./views/restaurant/RestaurantInfo";
import RegisterCheck from "./views/manager/RegisterCheck";
import Statistic from "./views/manager/Statistic";
import RestaurantStatistic from "./views/restaurant/RestaurantStatistic";

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/manager/check/register',
            component: RegisterCheck
        },
        {
            path: '/manager/check/revise',
            component: ReviseCheck
        },
        {
            path: '/manager/statistic',
            component: Statistic
        },
        {
            path: '/restaurant/add/product',
            component: AddProduct
        },
        {
            path: '/restaurant/info',
            component: RestaurantInfo
        },
        {
            path: '/restaurant/order',
            component: RestaurantStatistic
        },
        {
            path: '/customer/personal/info',
            component: PersonalInfo
        },
        {
            path: '/customer/address/manage',
            component: AddressManagement
        },
        {
            path: '/customer/book/food',
            component: BookFood
        },
        {
            path: '/customer/book/food/select',
            name: 'SelectFood',
            component: SelectFood
        },
        {
            path: '/customer/handle/order',
            component: HandleOrder
        }
    ]
})
