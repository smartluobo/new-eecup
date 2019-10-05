package com.ibay.tea.cms.responseVo;

public class OrderStatisticalVo {

    private int noPayOrderCount;

    private int payOrderCount;

    private int timeOutOrderCount;

    private int cancelOrderCount;

    private int proxyOrderCount;

    private int closeOrderCount;

    private int completedCount;

    public int getNoPayOrderCount() {
        return noPayOrderCount;
    }

    public void setNoPayOrderCount(int noPayOrderCount) {
        this.noPayOrderCount = noPayOrderCount;
    }

    public int getPayOrderCount() {
        return payOrderCount;
    }

    public void setPayOrderCount(int payOrderCount) {
        this.payOrderCount = payOrderCount;
    }

    public int getTimeOutOrderCount() {
        return timeOutOrderCount;
    }

    public void setTimeOutOrderCount(int timeOutOrderCount) {
        this.timeOutOrderCount = timeOutOrderCount;
    }

    public int getCancelOrderCount() {
        return cancelOrderCount;
    }

    public void setCancelOrderCount(int cancelOrderCount) {
        this.cancelOrderCount = cancelOrderCount;
    }

    public int getProxyOrderCount() {
        return proxyOrderCount;
    }

    public void setProxyOrderCount(int proxyOrderCount) {
        this.proxyOrderCount = proxyOrderCount;
    }

    public int getCloseOrderCount() {
        return closeOrderCount;
    }

    public void setCloseOrderCount(int closeOrderCount) {
        this.closeOrderCount = closeOrderCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }
}
