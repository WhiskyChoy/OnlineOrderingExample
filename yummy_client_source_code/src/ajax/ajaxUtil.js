import Vue from "vue"

import {checkLogin} from "./userAjax";
import {setLoginState} from "../util/LoginStateSetter";

let showData = true;

let showNetworkError = () => {
    Vue.prototype.$message({
        message: '网络错误. 若您已登出将返回登录页',
        type: 'error'
    })
};

export let globalCheckLogin = (pointer) => {
    checkLogin().then(response => {
        if (response && response.data && response.data.loginState && response.data.loginState.loggedIn === false) {
            Vue.prototype.$message({
                message: '您已登出',
                type: 'info'
            })
        }
        setLoginState(pointer, response.data)
    }).catch(e => {
        window.console.log(e);
    });
};

export let setLogger = (promise) => {
    if (showData) {
        return promise.then(response => {
            window.console.log(response.data);
            return response;
        }).catch(error => {
            showNetworkError();
            window.console.log(error)
        });
    } else {
        return promise;
    }
};