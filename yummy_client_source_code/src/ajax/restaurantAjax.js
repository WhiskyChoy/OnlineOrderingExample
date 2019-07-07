import axios from "axios";
import {setLogger} from "./ajaxUtil"; // data 不要放错位置！
export let registerRestaurant = (data) => {
    return setLogger(axios.post('api/restaurant/register', data))
};
export let loginRestaurant = (data) => {
    return setLogger(axios.post('api/restaurant/login', data))
};
export let restaurantAddProduct = (data) => {
    return setLogger(axios.post('api/restaurant/add/product', data))
};

export let getRestaurantInfo = () => {
    return setLogger(axios.get('api/restaurant/self/info'))
};

export let reviseRestaurant = (data) => {
    return setLogger(axios.post('api/restaurant/revise', data))
};

export let getRestFoodOrder = () => {
    return setLogger(axios.get('api/restaurant/order'))
};