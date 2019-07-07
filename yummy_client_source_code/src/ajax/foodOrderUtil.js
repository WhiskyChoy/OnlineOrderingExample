export let getStateFoodOrder = (foodOrder) => {
    if (!foodOrder.paid && !foodOrder.actualCanceled) {
        return '待支付/可取消';
    }
    if (foodOrder.systemCanceled) {
        return '超时未支付，系统自动取消';
    }
    if (foodOrder.userCanceled) {
        return '用户已取消';
    }
    if (!foodOrder.actualConfirmed && !foodOrder.refunded) {
        return '已支付/待确认/可退款'
    }
    if (foodOrder.userConfirmed) {
        return '用户已确认'
    }
    if (foodOrder.systemConfirmed) {
        return '超时未确认，系统自动确认'
    }
    if (foodOrder.refunded) {
        return '已退款'
    }
};

export let canPayOrCancel = (foodOrder) => {
    return !foodOrder.paid && !foodOrder.actualCanceled;
};

export let canConfirmOrRefund = (foodOrder) => {
    return foodOrder.paid && !foodOrder.actualConfirmed && !foodOrder.refunded;
};