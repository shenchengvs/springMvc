package com.holley.mvc.model.def;

import com.alibaba.fastjson.JSONObject;

public class MyGlobals {

    public final static ThreadLocal<JSONObject> threadLocal         = new ThreadLocal<JSONObject>();
    // BEAN DEF
    public final static String                  JSON_QUERY_BEAN_KEY = "jsonQueryBeanKey";
    public final static String                  EXCEL_BEAN_KEY      = "excelBeanKey";
    public final static String                  VALID_CODE_KEY      = "validCode";                  // 图片验证码key
}
