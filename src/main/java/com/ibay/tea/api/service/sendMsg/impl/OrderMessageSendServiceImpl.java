package com.ibay.tea.api.service.sendMsg.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.LoadingCache;
import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.service.sendMsg.OrderMessageSendService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.HttpUtil;
import com.ibay.tea.dao.TbOrderMapper;
import com.ibay.tea.dao.TbUserPayRecordMapper;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbUserPayRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderMessageSendServiceImpl implements OrderMessageSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMessageSendServiceImpl.class);

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private LoadingCache<String,String> wechatTokenGuavaCache;

    @Resource
    private WechatInfoProperties wechatInfoProperties;

    @Resource
    private TbUserPayRecordMapper tbUserPayRecordMapper;

    @Override
    public void orderMessageSend(String orderId,int sendType) {
        try {
            TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
            TbUserPayRecord payRecord = tbUserPayRecordMapper.findPayRecordByOrderId(orderId);
            if (sendType == ApiConstant.ORDER_MAKE_COMPLETE_MESSAGE_SEND && tbOrder.getMakeCompleteSendStatus() != 1){
                //推送订单制作完成消息给用户
                String token = wechatTokenGuavaCache.get(ApiConstant.WECHAT_ACCESS_TOKEN_GUAVA_KEY);
                String sendTemplateMessageUrl = wechatInfoProperties.getSendTemplateMessageUrl();
                sendTemplateMessageUrl = sendTemplateMessageUrl+token;
                Map<String,Object> params = new HashMap<>();
                buildOrderCompleteMessage(tbOrder, params);
                params.put("form_id",payRecord.getPrepayId());
                LOGGER.info("order make complete message content :{}",params);
                String jsonString = JSONObject.toJSONString(params);
                LOGGER.info("order make complete message json content :{}",params);
                String result = HttpUtil.postHttp(sendTemplateMessageUrl, jsonString);
                LOGGER.info("order make complete message send wechat return result :{}",result);
                Map resultMap = JSON.parseObject(result, Map.class);
                LOGGER.info("order make complete message send wechat return result parse map :{}",resultMap);
                if (CollectionUtils.isEmpty(resultMap)){
                    tbOrderMapper.updateCompleteMessageSendStatus(orderId,2);
                }else {
                    String errcode = String.valueOf(resultMap.get("errcode"));
                    if ("0".equals(errcode)){
                        tbOrderMapper.updateCompleteMessageSendStatus(orderId,1);
                    }else {
                        tbOrderMapper.updateCompleteMessageSendStatus(orderId,2);
                    }
                }
            }if (sendType == ApiConstant.ORDER_CLOSE_MESSAGE_SEND && tbOrder.getCloseSendStatus() != 1){
                //推送订单关闭消息给用户
                String token = wechatTokenGuavaCache.get(ApiConstant.WECHAT_ACCESS_TOKEN_GUAVA_KEY);
                String sendTemplateMessageUrl = wechatInfoProperties.getSendTemplateMessageUrl();
                sendTemplateMessageUrl = sendTemplateMessageUrl+token;
                Map<String,Object> params = new HashMap<>();
                buildOrderCloseMessage(tbOrder, params);
                params.put("form_id",payRecord.getPrepayId());
                LOGGER.info("order close message content :{}",params);
                String jsonString = JSONObject.toJSONString(params);
                LOGGER.info("order close message json content :{}",params);
                String result = HttpUtil.postHttp(sendTemplateMessageUrl, jsonString);
                LOGGER.info("order close message send wechat return result :{}",result);
                Map resultMap = JSON.parseObject(result, Map.class);
                LOGGER.info("order close message send wechat return result parse map :{}",resultMap);
                if (CollectionUtils.isEmpty(resultMap)){
                    tbOrderMapper.updateCloseMessageSendStatus(orderId,2);
                }else {
                    String errcode = String.valueOf(resultMap.get("errcode"));
                    if ("0".equals(errcode)){
                        tbOrderMapper.updateCloseMessageSendStatus(orderId,1);
                    }else {
                        tbOrderMapper.updateCloseMessageSendStatus(orderId,2);
                    }
                }
            }
        }catch (Exception e){
            if (sendType == ApiConstant.ORDER_CLOSE_MESSAGE_SEND){
                tbOrderMapper.updateCloseMessageSendStatus(orderId,2);
            }
            if (sendType == ApiConstant.ORDER_MAKE_COMPLETE_MESSAGE_SEND){
                tbOrderMapper.updateCompleteMessageSendStatus(orderId,2);
            }
            LOGGER.error("orderMessageSend happen exception orderId : {} sendType : {}",orderId,sendType,e);
        }
    }

    private void buildOrderCompleteMessage(TbOrder tbOrder, Map<String, Object> params) {
        params.put("touser",tbOrder.getOppenId());
        params.put("template_id",wechatInfoProperties.getOrderMakeCompleteTemplateId());
        params.put("form_id",tbOrder.getOrderId());
        Map<String,Object> dataMap = new HashMap<>();

        Map<String ,Object> keyword1Map = new HashMap<>();
        Map<String ,Object> keyword2Map = new HashMap<>();
        Map<String ,Object> keyword3Map = new HashMap<>();
        Map<String ,Object> keyword4Map = new HashMap<>();
        Map<String ,Object> keyword5Map = new HashMap<>();

        dataMap.put("keyword1",keyword1Map);
        dataMap.put("keyword2",keyword2Map);
        dataMap.put("keyword3",keyword3Map);
        dataMap.put("keyword4",keyword4Map);
        dataMap.put("keyword5",keyword5Map);

        keyword1Map.put("value",tbOrder.getOrderId());
        keyword2Map.put("value",tbOrder.getGoodsName());
        keyword3Map.put("value",tbOrder.getStoreName());
        keyword4Map.put("value",wechatInfoProperties.getOrderCompleteTips());
        keyword5Map.put("value",tbOrder.getTakeCode());

        params.put("data",dataMap);
        params.put("emphasis_keyword","keyword5.DATA");
    }

    private void buildOrderCloseMessage(TbOrder tbOrder, Map<String, Object> params) {
        params.put("touser",tbOrder.getOppenId());
        params.put("template_id",wechatInfoProperties.getOrderCloseTemplateId());
        params.put("form_id",tbOrder.getOrderId());
        Map<String,Object> dataMap = new HashMap<>();

        Map<String ,Object> keyword1Map = new HashMap<>();
        Map<String ,Object> keyword2Map = new HashMap<>();
        Map<String ,Object> keyword3Map = new HashMap<>();
        Map<String ,Object> keyword4Map = new HashMap<>();

        dataMap.put("keyword1",keyword1Map);
        dataMap.put("keyword2",keyword2Map);
        dataMap.put("keyword3",keyword3Map);
        dataMap.put("keyword4",keyword4Map);

        keyword1Map.put("value",tbOrder.getOrderId());
        keyword2Map.put("value",tbOrder.getGoodsName());
        keyword3Map.put("value",tbOrder.getStoreName());
        keyword4Map.put("value",wechatInfoProperties.getOrderCloseTips());

        params.put("data",dataMap);
        params.put("emphasis_keyword","keyword2.DATA");
    }

}
