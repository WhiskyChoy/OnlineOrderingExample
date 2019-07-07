import axios from "axios";
import {setLogger} from "./ajaxUtil";

export let managerLogin = (data) => {
    return setLogger(axios.post('api/manager/login', data))
};
export let approveRestaurant = (data) => {
    return setLogger(axios.post('api/manager/approve', data))
};
export let disapproveRestaurant = (data) => {
    return setLogger(axios.post('api/manager/reject', data))
};
export let getRestaurantWaitRegister = () => {
    return setLogger(axios.get('api/manager/wait/register'))
};

export let getReviseList = () => {
    return setLogger(axios.get('api/manager/wait/revise'))
};

export let approveRevise = (data) => {
    return setLogger(axios.post('api/manager/approve/revise', data))
};

export let disapproveRevise = (data) => {
    return setLogger(axios.post('api/manager/reject/revise', data))
};

export let getStatistic = () => {
    return setLogger(axios.get('api/manager/statistic'))
};