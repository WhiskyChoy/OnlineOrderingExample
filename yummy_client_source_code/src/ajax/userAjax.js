import axios from "axios";
import {setLogger} from "./ajaxUtil";

export let checkLogin = () => {
    return setLogger(axios.get('api/user/checkLogin'));
};

export let logout = () => {
    return setLogger(axios.get('api/user/logout'))
};

