package com.ibay.tea.common.utils;

import com.ibay.tea.common.CommonConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 字符串分割工具类

 */
public class StringSplitUtil {
    /**
     * 按照分割符分割并返回列表
     *
     * @param value
     * @param separate
     * @return
     */
    public static List<String> split(String value, String separate) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isBlank(value) || StringUtils.isBlank(separate)) {
            return result;
        }
        result.addAll(Arrays.asList(StringUtils.split(value, separate)));
        return result;
    }

    /**
     * 将String按照分割符分割并返回成数组
     *
     * @param value
     * @param separate
     * @return
     */
    public static String[] splitToArray(String value, String separate) {
        String[] result = {};
        if (StringUtils.isBlank(value) || StringUtils.isBlank(separate)) {
            return result;
        }
        return StringUtils.split(value, separate);
    }

    /**
     * 默认逗号分隔并返回列表,若结果为空 返回空的list
     *
     * @param value
     * @return
     */
    public static List<String> split(String value) {
        return split(value, CommonConstant.DEFAULT_SEPARATE);
    }

    /**
     * 默认逗号分隔并返回数组,若结果为空 返回空的数组
     *
     * @param value
     * @return
     */
    public static String[] splitToArray(String value) {
        return splitToArray(value, CommonConstant.DEFAULT_SEPARATE);
    }
}
