import Vue from 'vue'

export let logCommon = (response, operation = '操作') => {
    if (!response) {
        Vue.prototype.$message({
            message: '响应未抵达',
            type: 'error'
        });
        return false;
    }
    if (!response.data) {
        Vue.prototype.$message({
            message: '没有数据体',
            type: 'error'
        });
        return false;
    }
    if (!response.data['successful']) {
        Vue.prototype.$message({
            message: `${operation}失败：${response.data['info']}`,
            type: 'error'
        });
        return false;
    }

    Vue.prototype.$message({
        message: `${operation}成功：${response.data['info']}`,
        type: 'success'
    });

    return true;
};