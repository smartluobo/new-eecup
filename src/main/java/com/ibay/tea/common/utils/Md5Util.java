package com.ibay.tea.common.utils;

import java.security.MessageDigest;

public class Md5Util {
    /**
     * MD5加密
     * @param str	待加密的字符串
     * @return		加密后的字符串
     */
    public static String encryptMD5(String str){
        StringBuffer buffer = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            /// 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
            digest.update(str.getBytes("utf-8"));
            //计算出摘要
            byte array[] = digest.digest();
            int i;
            for (int offset = 0; offset < array.length; offset++) {
                i = array[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buffer.append("0");
                }
                // 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
                buffer.append(Integer.toHexString(i));
            }
            //32位加密
            str = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }
}
