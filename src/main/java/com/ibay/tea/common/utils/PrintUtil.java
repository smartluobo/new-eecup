package com.ibay.tea.common.utils;

import com.ibay.tea.config.PrintSysProperties;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PrintUtil {

    @Resource
    private PrintSysProperties printSysProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintUtil.class);

    //**********测试时，打开下面注释掉方法的即可,更多接口文档信息,请访问官网开放平台查看**********
    public static void main(String[] args) throws Exception{




        //==================添加打印机接口（支持批量）==================
        //***返回值JSON字符串***
        //正确例子：{"msg":"ok","ret":0,"data":{"ok":["sn#key#remark#carnum","316500011#abcdefgh#快餐前台"],"no":["316500012#abcdefgh#快餐前台#13688889999  （错误：识别码不正确）"]},"serverExecutedTime":3}
        //错误：{"msg":"参数错误 : 该帐号未注册.","ret":-2,"data":null,"serverExecutedTime":37}

        //提示：打印机编号(必填) # 打印机识别码(必填) # 备注名称(选填) # 流量卡号码(选填)，多台打印机请换行（\n）添加新打印机信息，每次最多100行(台)。
//			String snlist = "sn1#key1#remark1#carnum1\nsn2#key2#remark2#carnum2";
//			String method = addprinter(snlist);
//			System.out.println(method);



        //==================方法1.打印订单==================
        //***返回值JSON字符串***
        //成功：{"msg":"ok","ret":0,"data":"xxxxxxx_xxxxxxxx_xxxxxxxx","serverExecutedTime":5}
        //失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}

//			String method1 = print(SN);
//			System.out.println(method1);



        //===========方法2.查询某订单是否打印成功=============
        //***返回值JSON字符串***
        //成功：{"msg":"ok","ret":0,"data":true,"serverExecutedTime":2}//data:true为已打印,false为未打印
        //失败：{"msg":"错误描述","ret":非0, "data":null,"serverExecutedTime":7}

//			String orderid = "xxxxxxx_xxxxxxxx_xxxxxxxx";//订单ID，从方法1返回值data获取
//			String method2 = queryOrderState(orderid);
//			System.out.println(method2);



        //===========方法3.查询指定打印机某天的订单详情============
        //***返回值JSON字符串***
        //成功：{"msg":"ok","ret":0,"data":{"print":6,"waiting":1},"serverExecutedTime":9}//print已打印，waiting为打印
        //失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}

//			String strdate = "2016-11-12";//注意时间格式为"yyyy-MM-dd"
//			String method3 = queryOrderInfoByDate(SN,strdate);
//			System.out.println(method3);



        //===========方法4.查询打印机的状态==========================
        //***返回值JSON字符串***
        //成功：{"msg":"ok","ret":0,"data":"状态","serverExecutedTime":4}
        //失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}

//        String method4 = queryPrinterStatus(SN);
//			System.out.println(method4);

    }





    //=====================以下是函数实现部分================================================

    public String addprinter(String snlist){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(printSysProperties.getPrintUrl());
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",printSysProperties.getPrintUserName()));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(printSysProperties.getPrintUserName(),printSysProperties.getPrintUKey(),STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printerAddlist"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("printerContent",snlist));

        CloseableHttpResponse response = null;
        String result = null;
        return execute(httpClient, post, nvps, response, result);

    }


    //方法1
    public String print(String sn,String content){
        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(printSysProperties.getPrintUrl());
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",printSysProperties.getPrintUserName()));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(printSysProperties.getPrintUserName(),printSysProperties.getPrintUKey(),STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printMsg"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));
        nvps.add(new BasicNameValuePair("content",content));
        nvps.add(new BasicNameValuePair("times","1"));//打印联数

        CloseableHttpResponse response = null;

        return execute(httpClient, post, nvps, response, content);

    }


    //方法2
    public String queryOrderState(String printOrderId){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(printSysProperties.getPrintUrl());
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",printSysProperties.getPrintUserName()));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(printSysProperties.getPrintUserName(),printSysProperties.getPrintUKey(),STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_queryOrderState"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("orderid",printOrderId));

        CloseableHttpResponse response = null;
        String result = null;
        return execute(httpClient, post, nvps, response, result);

    }



    //方法3
    public String queryOrderInfoByDate(String sn,String strdate){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(printSysProperties.getPrintUrl());
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",printSysProperties.getPrintUserName()));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(printSysProperties.getPrintUserName(),printSysProperties.getPrintUKey(),STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_queryOrderInfoByDate"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));
        nvps.add(new BasicNameValuePair("date",strdate));//yyyy-MM-dd格式

        CloseableHttpResponse response = null;
        String result = null;
        return execute(httpClient, post, nvps, response, result);

    }



    //方法4
    public String queryPrinterStatus(String sn){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(printSysProperties.getPrintUrl());
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",printSysProperties.getPrintUserName()));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(printSysProperties.getPrintUserName(),printSysProperties.getPrintUKey(),STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_queryPrinterStatus"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));

        CloseableHttpResponse response = null;
        String result = null;
        return execute(httpClient, post, nvps, response, result);

    }

    private String execute(CloseableHttpClient httpClient, HttpPost post, List<NameValuePair> nvps, CloseableHttpResponse response, String result) {
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int stateCode = response.getStatusLine().getStatusCode();
            LOGGER.info("order print starting content : {},return http code : {}",result,stateCode);

            if(stateCode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                    LOGGER.info("order print return result :{}",result);
                }
            }
        } catch (Exception e) {
            LOGGER.error("order print happen exception", e);
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //生成签名字符串
    private String signature(String USER,String UKEY,String STIME){
        String s = DigestUtils.sha1Hex(USER+UKEY+STIME);
        return s;
    }
}
