import axios from "axios";
import {setLogger} from "./ajaxUtil";

export let customerGetLocation = () => {
    return setLogger(axios.get('api/customer/get/location'))
};
export let customerAddLocation = (data) => {
    return setLogger(axios.post('api/customer/add/location', data))
};
export let customerDeleteLocation = (data) => {
    return setLogger(axios.post('api/customer/delete/location', data))
};
export let getAuthnCode = (data) => {
    return setLogger(axios.post('api/customer/code', data))
};
export let getCustLogMeta = () => {
    return setLogger(axios.get('api/customer/meta'))
};
export let doActualLogin = (data) => {
    return setLogger(axios.post('api/customer/login', data))
};
export let checkCustomerExisted = (data) => {
    return setLogger(axios.post('api/customer/exist', data))
};
export let customerGetRestaurant = (data) => {
    return setLogger(axios.post('api/customer/find/restaurant', data))
};
export let customerFindFood = (data) => {
    return setLogger(axios.post('api/customer/find/food', data))
};
export let addOrderWait = (data) => {
    return setLogger(axios.post('api/customer/add/order/wait', data))
};

export let getAllOrder = () => {
    return setLogger(axios.get('api/customer/all/order'))
};

export let payOrder = (data) => {
    return setLogger(axios.post('api/customer/pay/order', data))
};
export let cancelOrder = (data) => {
    return setLogger(axios.post('api/customer/cancel/order', data))
};
export let confirmOrder = (data) => {
    return setLogger(axios.post('api/customer/confirm/order', data))
};
export let refundOrder = (data) => {
    return setLogger(axios.post('api/customer/refund/order', data))
};

export let getInfo = ()=>{
    return setLogger(axios.get('api/customer/self/info'))
};

export let setInfo = (data) => {
    return setLogger(axios.post('api/customer/revise', data))
};