package com.holley.mvc.biz.util;

import com.alibaba.fastjson.JSON;
import com.holley.platform.common.util.StringUtil;

/**
 * 公共工具类
 * 
 * @author sc
 */
public class CommonUtil {

    private final static String CHARSET_ENCODING = "UTF-8";

    public static boolean isJsonObject(String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        } else if (!str.startsWith("{") || !str.endsWith("}")) {
            return false;
        }
        try {
            JSON.parseObject(str);
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }

    public static boolean isJsonArray(String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        } else if (!str.startsWith("[") || !str.endsWith("]")) {
            return false;
        }
        try {
            JSON.parseArray(str);
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }

    public static boolean isJson(String str) {
        if (isJsonObject(str) || isJsonArray(str)) {
            return true;
        }
        return false;
    }

    // 首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) return s;
        else return new StringBuilder().append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    // 首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) return s;
        else return new StringBuilder().append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
