package com.ibay.tea.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "wechat.info")
@Component
public class WechatInfoProperties {

    //创建预支付订单地址
    private String createOrderUrl;
    //小程序id
    private String appId;
    //商户id
    private String mchId;
    //签名类型
    private String signType;
    //登陆地址
    private String loginUrl;
    //小程序后台配置的随机字符串
    private String secret;

    private String apiKey;

    private String tradeType;

    private String tokenUrl;

    //订单支付成功模板id
    private String orderPayTemplateId;

    //订单制作完成模板id
    private String orderMakeCompleteTemplateId;

    //订单关闭模板id
    private String orderCloseTemplateId;

    //小程序推送消息地址
    private String sendTemplateMessageUrl;

    private String orderCompleteTips;

    private String orderCloseTips;

    private String clientIp;

    private String notifyUrl;

    public String getCreateOrderUrl() {
        return createOrderUrl;
    }

    public void setCreateOrderUrl(String createOrderUrl) {
        this.createOrderUrl = createOrderUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getOrderPayTemplateId() {
        return orderPayTemplateId;
    }

    public void setOrderPayTemplateId(String orderPayTemplateId) {
        this.orderPayTemplateId = orderPayTemplateId;
    }

    public String getOrderMakeCompleteTemplateId() {
        return orderMakeCompleteTemplateId;
    }

    public void setOrderMakeCompleteTemplateId(String orderMakeCompleteTemplateId) {
        this.orderMakeCompleteTemplateId = orderMakeCompleteTemplateId;
    }

    public String getOrderCloseTemplateId() {
        return orderCloseTemplateId;
    }

    public void setOrderCloseTemplateId(String orderCloseTemplateId) {
        this.orderCloseTemplateId = orderCloseTemplateId;
    }

    public String getSendTemplateMessageUrl() {
        return sendTemplateMessageUrl;
    }

    public void setSendTemplateMessageUrl(String sendTemplateMessageUrl) {
        this.sendTemplateMessageUrl = sendTemplateMessageUrl;
    }



    public String getOrderCloseTips() {
        return orderCloseTips;
    }

    public void setOrderCloseTips(String orderCloseTips) {
        this.orderCloseTips = orderCloseTips;
    }

    public String getOrderCompleteTips() {
        return orderCompleteTips;
    }

    public void setOrderCompleteTips(String orderCompleteTips) {
        this.orderCompleteTips = orderCompleteTips;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
