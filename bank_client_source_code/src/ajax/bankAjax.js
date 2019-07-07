import axios from "axios";

let showData = true;

let setLogger = (promise) => {
    if (showData) {
        return promise.then(response => {
            window.console.log(response.data);
            return response;
        });
    } else {
        return promise;
    }
};

export let getAllAccounts = () => {
    return setLogger(axios.get('api/bank/all'));
};

export let addOneAccount = (account) => {
    return setLogger(axios.post('api/bank/add', account));
};