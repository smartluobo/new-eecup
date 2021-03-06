package com.ibay.tea.api.service.pay;

import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.factory.XStreamFactory;
import com.ibay.tea.api.request.WechatCreateOrderRequest;
import com.ibay.tea.api.service.wechat.WechatSendService;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.common.utils.*;
import com.ibay.tea.dao.*;
import com.ibay.tea.entity.*;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class ApiPayService {


    @Resource
    private WechatInfoProperties wechatInfoProperties;

    @Resource
    private WechatSendService wechatSendService;

    @Resource
    private TbUserPayRecordMapper tbUserPayRecordMapper;

    @Resource
    private TbStoreGoodsMapper storeGoodsMapper;

    @Resource
    private TbOrderMapper orderMapper;

    @Resource
    private TbOrderItemMapper orderItemMapper;

    @Resource(name = "sendExecutorService")
    private ExecutorService sendExecutorService;

    @Resource
    private PrintService printService;

    @Resource
    private StoreCache storeCache;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    public Map<String, Object> createPayOrderToWechat(TbOrder tbOrder) throws Exception{

        WechatCreateOrderRequest request = buildWechatCreateOrderRequest(tbOrder);
        String sendXml = getSendXml(request);
        Map<String,Object> body = new HashMap<>();
        body.put("body",sendXml);
        String wechatServerReturnResult = wechatSendService.sendDataToWechatServer(sendXml);
        if (StringUtils.isNotEmpty(wechatServerReturnResult)){
            Map<String, Object> resultMap = WechatXmlParser.getMapFromXML(wechatServerReturnResult);
           return resultMap;
        }
        return null;
    }

    public Map<String, Object> payByOrderId(String orderId) {
        Map<String ,Object> resultMap = new HashMap<>();
        TbUserPayRecord tbUserPayRecord = tbUserPayRecordMapper.findPayRecordByOrderId(orderId);
        resultMap.put("nonce_str",tbUserPayRecord.getNonceStr());
        resultMap.put("prepay_id",tbUserPayRecord.getPrepayId());
        resultMap.put("appid",wechatInfoProperties.getAppId());
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        resultMap.put("timeStamp",timeStamp);
        resultMap.put("signType",wechatInfoProperties.getSignType());
        String paySign = secondEncrypt(tbUserPayRecord,timeStamp);
        resultMap.put("paySign",paySign);
        return resultMap;
    }

    public String secondEncrypt(TbUserPayRecord tbUserPayRecord,String timeStamp) {
        String appid = wechatInfoProperties.getAppId();
        String nonceStr = tbUserPayRecord.getNonceStr();
        String prepay_id = tbUserPayRecord.getPrepayId();
        String signType = wechatInfoProperties.getSignType();
        if (org.springframework.util.StringUtils.isEmpty(appid) || org.springframework.util.StringUtils.isEmpty(nonceStr) ||
                org.springframework.util.StringUtils.isEmpty(prepay_id) || org.springframework.util.StringUtils.isEmpty(signType) || org.springframework.util.StringUtils.isEmpty(timeStamp)){
            return null;
        }

        StringBuffer sb = new StringBuffer("appId=");
        sb.append(appid).append("&nonceStr=").append(nonceStr).append("&package=prepay_id=").append(prepay_id)
                .append("&signType=").append(wechatInfoProperties.getSignType()).append("&timeStamp=").append(timeStamp)
                .append("&key=").append(wechatInfoProperties.getApiKey());
        String notSignStr = sb.toString();
        String signStr = Md5Util.encryptMD5(notSignStr).toUpperCase();
        log.info("secondEncrypt notSignStr :{}---signStr : {}",notSignStr,signStr);
        return signStr;
    }

    public boolean payCallbackHandle(HttpServletRequest request) throws Exception{

        log.info("enter wechat pay callback......");
        String orderId = null;
        try {
            String xmlString = getXmlString(request);
            log.info("wechat callback result String" + xmlString);
            // 先解析返回的数据
            Map<String, String> resultMap = WxUtil.xmlToMap(xmlString);
            log.info("wechat callback params : {}" ,resultMap);
            String returnCode = resultMap.get("return_code");
            log.info("wechat pay callback return_code : {}",returnCode);
            // 通信成功
            if ("SUCCESS".equals(returnCode)) {
                String transactionId = resultMap.get("transaction_id");
                orderId = resultMap.get("out_trade_no");
                Map<String,Object> updateMap = new HashMap<>();
                updateMap.put("transactionId",transactionId);
                updateMap.put("orderId",orderId);
                updateMap.put("updateTime",new Date());
                if (resultMap.get("result_code").equals("SUCCESS")) {
                    log.info("current order orderId : {orderId} pay success",orderId);
                    //支付成功更新
                    //1更新订单状态
                    updateMap.put("orderStatus",1);
                    orderMapper.updatePayStatus(updateMap);
                    //2扣减库存
                    TbOrder tbOrder = orderMapper.selectByPrimaryKey(orderId);
                    if (tbOrder == null){
                        return true;
                    }
                    List<TbOrderItem> orderItemList = orderItemMapper.findOrderItemByOrderId(orderId);
                    if (!CollectionUtils.isEmpty(orderItemList)){
                        for (TbOrderItem orderItem : orderItemList) {
                            storeGoodsMapper.updateInventory(orderItem.getItemId(),tbOrder.getStoreId(),orderItem.getNum());
                        }
                    }
                    //3更新支付记录状态
                    updateMap.put("payStatus",1);
                    tbUserPayRecordMapper.updatePayStatus(updateMap);
                    //如果订单使用优惠券 将优惠券的修改为已经使用
                    if (tbOrder.getUserCouponsId() != 0){
                        tbUserCouponsMapper.updateStatusById(tbOrder.getUserCouponsId(),ApiConstant.USER_COUPONS_STATUS_USED);
                    }
                    //分享优惠券赠送
                    if (tbOrder.getIsFirstOrder() == 1){
                        log.info("first order pay success recommend handle");
                        TbApiUser apiUser = tbApiUserMapper.findApiUserByOppenId(tbOrder.getOppenId());
                        String referrerOppenId = apiUser.getReferrerOppenId();
                        if (StringUtils.isNotBlank(referrerOppenId)){
                            log.info("current user is shared handle give coupons");
                            TbUserCoupons referrerCoupons = tbUserCouponsMapper.findReferrerCoupons(referrerOppenId);
                            if (referrerCoupons == null){
                                log.info("recommend user no recommend type coupons");
                                buildCouponsAndInsert(referrerOppenId);
                            }else{
                                //todo
                                log.info("recommend coupons info : {}",referrerCoupons);
                                String couponsRatio = referrerCoupons.getCouponsRatio();
                                log.info("recommend coupons ratio : {}",couponsRatio);
                                String[] split = couponsRatio.split("\\.");
                                int ratio = Integer.valueOf(split[1]);
                                log.info("current user recommend coupons ratio : {}",ratio);
                                if (ratio > 2){
                                    int upgrade = ratio - 2;
                                    String ratioStr = "0."+upgrade;
                                    tbUserCouponsMapper.updateRatio(referrerCoupons.getId(),ratioStr);
                                }else {
                                    tbUserCouponsMapper.updateUpgradeCouponsType(referrerCoupons.getId(),ApiConstant.USER_COUPONS_TYPE_FREE);
                                }
                            }
                            orderMapper.updateShareOrder(tbOrder.getOrderId(), tbOrder.getOppenId());
                        }
                    }
                    //异步调用订单打印
                    TbStore store = storeCache.findStoreById(tbOrder.getStoreId());
                    printService.printOrder(tbOrder, store, ApiConstant.PRINT_TYPE_ORDER_ALL);
                    log.info("pay call back print order success orderId : {}",tbOrder.getOrderId());
                } else {
                    //支付失败更新
                    //更新支付记录状态，库存和订单状态不用修改
                    updateMap.put("payStatus",2);
                    tbUserPayRecordMapper.updatePayStatus(updateMap);
                }
                return true;
            }
            return false;
        }catch (Exception e){
            log.error("wechat pay success callback happen exception orderId : {}",orderId,e);
            return false;
        }
    }

    private void buildCouponsAndInsert(String referrerOppenId) {
        TbUserCoupons tbUserCoupons = new TbUserCoupons();
        String yyyyMMdd = DateUtil.getDateYyyyMMdd();
        tbUserCoupons.setOppenId(referrerOppenId);
        tbUserCoupons.setCouponsId(0);
        tbUserCoupons.setCouponsName("分享折扣券");
        tbUserCoupons.setReceiveDate(Integer.valueOf(DateUtil.getDateYyyyMMdd()));
        tbUserCoupons.setCreateTime(new Date());
        tbUserCoupons.setStatus(ApiConstant.USER_COUPONS_STATUS_NO_USE);
        tbUserCoupons.setExpireDate(DateUtil.getExpireDate(Integer.valueOf(yyyyMMdd),30));
        tbUserCoupons.setIsReferrer(1);
        tbUserCoupons.setCouponsRatio("0.6");
        tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_RATIO);
        tbUserCoupons.setUseRules("全场折扣下不能使用优惠券哦");
        tbUserCoupons.setUseScope("任意商品");
        tbUserCoupons.setCouponsSource(ApiConstant.COUPONS_SOURCE_SHARE);
        tbUserCoupons.setSourceName("分享专属");
        tbUserCoupons.setUseWay(ApiConstant.COUPONS_USE_WAY_APPLET);
        tbUserCoupons.setExpireType(ApiConstant.COUPONS_EXPIRE_TYPE_DEFAULT);
        tbUserCouponsMapper.insert(tbUserCoupons);
    }

    public String getXmlString(HttpServletRequest request) {
        BufferedReader reader = null;
        String line = "";
        String xmlString = null;
        try {
            reader = request.getReader();
            StringBuffer inputString = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            xmlString = inputString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return xmlString;
    }

    private WechatCreateOrderRequest buildWechatCreateOrderRequest(TbOrder tbOrder) {
        Random random = new Random();
        WechatCreateOrderRequest request = new WechatCreateOrderRequest();
        request.setAppid(wechatInfoProperties.getAppId());
        request.setMch_id(wechatInfoProperties.getMchId());
        request.setNonce_str(tbOrder.getOrderId()+random.nextInt(100));
        String notifyUrl = wechatInfoProperties.getNotifyUrl();
        log.info("order pay notifyUrl : {}",notifyUrl);
        request.setNotify_url(notifyUrl);
        request.setSign_type(wechatInfoProperties.getSignType());
        request.setTotal_fee(PriceCalculateUtil.intOrderTbPrice(new BigDecimal(String.valueOf(tbOrder.getPayment()))));
        request.setTrade_type(wechatInfoProperties.getTradeType());
        request.setOut_trade_no(tbOrder.getOrderId());
        request.setSpbill_create_ip(wechatInfoProperties.getClientIp());
        request.setBody(tbOrder.getGoodsName());
        request.setOpenid(tbOrder.getOppenId());
        return request;
    }

    private String getSendXml(WechatCreateOrderRequest request) {
        String sign = WechatSignUtil.getSignForObject(request, wechatInfoProperties.getApiKey());
        request.setSign(sign);
        XStream st = XStreamFactory.getRequestXstream();
        String resultXml = st.toXML(request);
        return resultXml;
    }
}
