package com.ibay.tea.common.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.common.cache.Cache;
import com.ibay.tea.common.service.SendSmsService;
import com.ibay.tea.config.SmsSysProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SendSmsServiceImpl implements SendSmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendSmsServiceImpl.class);
    @Resource
    private SmsSysProperties smsSysProperties;

    @Resource
    private Cache<String,String> verificationCodeCache;



    public boolean sendVerificationCode(String phoneNum,String smsCode){
        try {
            LOGGER.info("smsSysProperties:{}",smsSysProperties);
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsSysProperties.getAccessKeyId(), smsSysProperties.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", smsSysProperties.getProduct(), smsSysProperties.getDomain());
            IAcsClient acsClient = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
            request.setPhoneNumbers(phoneNum);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(smsSysProperties.getSignName());

            //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
            request.setTemplateCode(smsSysProperties.getTemplateCode());
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam("{\"code\":\""+smsCode+"\"}");
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
                return true;
            }else {
                String message = sendSmsResponse.getMessage();
                LOGGER.info("remote call sms send fail return result : {}",message);
                return false;
            }
        }catch (Exception e) {
            LOGGER.error("remote call sms send happen exception",e);
            return false;
        }
    }

    @Override
    public void cacheVerificationCode(String phoneNum, String verificationCode) {
        verificationCodeCache.put(phoneNum,verificationCode);
    }

    @Override
    public boolean checkVerificationCode(String phoneNum, String verificationCode) {
        String cacheCode = verificationCodeCache.getIfPresent(phoneNum);
        if (verificationCode.equals(cacheCode)){
            return true;
        }
        return false;
    }


}
