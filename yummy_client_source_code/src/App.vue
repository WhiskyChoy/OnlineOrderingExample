<template>
    <div class="global_wrapper">
        <login-page v-if="!$store.state.loginState.loggedIn"></login-page>
        <home-page v-if="$store.state.loginState.loggedIn"></home-page>
    </div>
</template>

<script>
    import LoginPage from "./views/LoginPage";
    import HomePage from "./views/HomePage";
    import {globalCheckLogin} from "./ajax/ajaxUtil";

    export default {
        name: "App",
        components: {HomePage, LoginPage},
        mounted() {
            globalCheckLogin(this);
        }
    }
</script>

<style>
    .global_wrapper {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
    }

    ::-webkit-scrollbar-track-piece {
        -webkit-border-radius: 0;
    }

    ::-webkit-scrollbar {
        position: absolute;
        right: 2px;
        bottom: 2px;
        z-index: 1;
        border-radius: 4px;
        opacity: 0;
        -webkit-transition: opacity 120ms ease-out;
        transition: opacity 120ms ease-out;
    }

    ::-webkit-scrollbar-thumb {
        position: relative;
        display: block;
        width: 0;
        height: 0;
        cursor: pointer;
        border-radius: 4px;
        background-color: rgba(144, 147, 153, .3);
        -webkit-transition: .3s background-color;
        transition: .3s background-color;
    }

    ::-webkit-scrollbar-thumb:hover {
        background-color: rgba(144, 147, 153, .5);
    }
</style>