package com.ibay.tea.common.utils;

import com.ibay.tea.config.PrintSysProperties;
import com.ibay.tea.entity.TbPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Component
public class OrderItemPrintUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemPrintUtil.class) ;

    @Resource
    private PrintSysProperties printSysProperties;

//    public static String partner="43183489";//用户id
//    public	static String machine_code="4004621420";//打印机终端号
//    public	static String apiKey="7604f4cf63901f543c4008ef3d697aecbc8ec2f0";//API密钥
//    public	static String mKey="527617393096";//打印机密钥

    public String sendContent(TbPrinter tbPrinter, String content){
        String machineCode = tbPrinter.getPrinterSn();
        LOGGER.info("machine_code : {},printSysProperties info : {}",tbPrinter.getPrinterSn(),printSysProperties);
        try{
            Map<String,String> params=new HashMap<String,String>();
            params.put("partner", printSysProperties.getOrderItemUserId());
            params.put("machine_code", machineCode);
            String time = String.valueOf(System.currentTimeMillis());
            params.put("time", time);
            String sign=signRequest(tbPrinter.getPrinterKey(),params);

            byte[] data = ("partner="+printSysProperties.getOrderItemUserId()+"&machine_code="+machineCode+"&content="+content+"&sign="+sign+"&time="+time).getBytes();
            URL url = new URL("http://open.10ss.net:8888");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5 * 1000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type","text/html; charset=utf-8");
            conn.setRequestProperty("Content-Length",String.valueOf(data.length));

            //获取输出流
            OutputStream outStream = conn.getOutputStream();
            //传入参数
            outStream.write(data);
            outStream.flush();
            outStream.close();


            //获取输入流
            InputStream is = conn.getInputStream();

            System.out.println(conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                int i = -1;
                byte[] b = new byte[1024];
                StringBuffer result = new StringBuffer();
                while ((i = is.read(b)) != -1) {
                    result.append(new String(b, 0, i));
                }
                String resultStr = result.toString();
                LOGGER.info("print orderItem content : {}, result: {}",content,resultStr);
                return resultStr;
            }
            return null;
        }catch(Exception e){
           LOGGER.error("print orderItem happen exception content : {}",content,e);
            return null;
        }
    }
    /**
     * 打印签名
     * @param params
     * @return
     */
    public  String signRequest(String apiKey,Map<String,String> params){
        Map<String,String> sortedParams=new TreeMap<String,String>();
        sortedParams.putAll(params);
        Set<Map.Entry<String,String>> paramSet=sortedParams.entrySet();
        StringBuilder query=new StringBuilder();
        query.append(printSysProperties.getOrderItemApiKey());
        for (Map.Entry<String, String> param:paramSet) {
            query.append(param.getKey());
            query.append(param.getValue());
        }
        query.append(apiKey);
        String encryptStr=MD5.MD5Encode(query.toString()).toUpperCase();
        return encryptStr;
    }
}
