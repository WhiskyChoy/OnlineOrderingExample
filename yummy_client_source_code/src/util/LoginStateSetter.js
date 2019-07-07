export let setLoginState = (pointer, loginState) => {
    pointer.$store.state.loginState = loginState
};

export let clearLoginState = (pointer) => {
    pointer.$store.state.loginState = {loggedIn: false}
};