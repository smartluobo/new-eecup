package com.ibay.tea.common.utils;

import com.ibay.tea.api.config.ApiSysProperties;
import com.ibay.tea.common.annotation.NotSign;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WechatSignUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatSignUtil.class);
    private static volatile KeyStore keyStore = null;

    private static final String LOCK = "lock";

    public static KeyStore getKeyStore(String mchId) throws Exception{
        if (keyStore != null){
            return keyStore;
        }
        synchronized (LOCK){
            if (keyStore != null ){
                return keyStore;
            }
            FileInputStream in = null;
            try {
                keyStore = KeyStore.getInstance("PKCS12");
                File file = new File(ApiSysProperties.getStaticCertPath()+"apiclient_cert.p12");
                in = new FileInputStream(file);
                keyStore.load(in, mchId.toCharArray());
                return keyStore;
            } catch (KeyStoreException e) {
                LOGGER.error("wechat send redpacket get keyStore happen exception",e);
                return null;
            }finally {
                if (in != null){
                    in.close();
                }
            }
        }

    }

    public static String getSignForObject(Object o,String apiKey){
        String result = getNotSign(o,apiKey);
        LOGGER.info("No sign result : {}",result);
        return Md5Util.encryptMD5(result).toUpperCase();

    }

    /**
     * 签名算法
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    public static String getNotSign(Object o,String apiKey){
        List<String> list ;
        String result="";
        try {
            Class cls = o.getClass();
            Field[] fields = cls.getDeclaredFields();
            list = getFieldList(fields, o);
            Field[] superFields = cls.getSuperclass().getDeclaredFields(); //获取父类的私有属性
            list.addAll(getFieldList(superFields, o));
            int size = list.size();
            String[] arrayToSort = list.toArray(new String[size]);
            Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append(arrayToSort[i]);
            }
            result = sb.toString();
            if(StringUtils.isNotEmpty(apiKey)){
                result += "key="+apiKey;
            }else{
                result = result.substring(0,result.lastIndexOf("&"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将字段集合方法转换为String	   过滤掉不参与签名的字段
     * @param array
     * @param object
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static ArrayList<String> getFieldList(Field[] array, Object object) throws Exception{
        ArrayList<String> list = new ArrayList<String>();
        for(Field f:array){
            f.setAccessible(true);
            if(f.isAnnotationPresent(NotSign.class)){
                if(!f.getAnnotation(NotSign.class).value()){
                    if (f.get(object) != null && f.get(object) != "") {
                        list.add(f.getName() + "=" + f.get(object) + "&");
                    }
                }
            }else{
                if (f.get(object) != null && f.get(object) != "") {
                    list.add(f.getName() + "=" + f.get(object) + "&");
                }
            }
        }
        return list;
    }



}
