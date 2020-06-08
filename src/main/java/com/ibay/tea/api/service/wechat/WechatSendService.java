package com.ibay.tea.api.service.wechat;

import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.common.utils.WechatSignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyStore;

@Service
@Slf4j
public class WechatSendService {


    @Resource
    private WechatInfoProperties wechatInfoProperties;

    public String sendDataToWechatServer(String sendXml) throws Exception {
        log.info("create order call wechat send request body : {}",sendXml);
        String result  = "";
        KeyStore keyStore = WechatSignUtil.getKeyStore(wechatInfoProperties.getMchId());
        if (keyStore == null){
            log.error("send redpacket data to wechat server load keyStore fail keyStore is null ");
            return result;
        }
        CloseableHttpClient httpclient = null;
        try {
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, wechatInfoProperties.getMchId().toCharArray())
                    .build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( sslcontext,new String[] { "TLSv1" },
                    null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();

            HttpPost httpPost = new HttpPost(wechatInfoProperties.getCreateOrderUrl());
            StringEntity sendEntity = new StringEntity(sendXml,"utf-8");
            sendEntity.setContentEncoding("UTF-8");
            sendEntity.setContentType("application/json;charset=utf8");
            httpPost.setEntity(sendEntity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            StringBuffer buffer = new StringBuffer();
            try {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine())!= null) {
                        buffer.append(text+"\n");
                    }
                }
                result = buffer.toString();
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        }catch (Exception e){
            log.error("httpclient send redpacket data to wechat server happen exception",e);
            throw new RuntimeException();
        }finally {
            if (httpclient != null){
                httpclient.close();
            }
        }
        log.info("********wechat return string result :{}",result);
        return result;
    }
}
