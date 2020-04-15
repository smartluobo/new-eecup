package com.ibay.tea.common.utils;

import org.apache.commons.lang3.StringUtils;

public class CommonUtil {

    public static String getLikeQueryStr(String result){
        if (StringUtils.isEmpty(result)){
            return null;
        }
        return "%"+result+"%";
    }
}
