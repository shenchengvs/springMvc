package com.holley.mvc.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * HTTP工具类
 * 
 * @author sc
 */
public class HttpUtil {

    private final static String CHARSET_ENCODING = "UTF-8";

    public static JSONObject readerResult(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        InputStreamReader inputReader = null;
        BufferedReader bufferReader = null;
        JSONObject rs = null;
        try {
            inputReader = new InputStreamReader(req.getInputStream(), CHARSET_ENCODING);
            bufferReader = new BufferedReader(inputReader);
            String line = null;
            while ((line = bufferReader.readLine()) != null) {
                sb.append(line);
            }
            String temp = sb.toString();
            if (temp.startsWith("data:")) {
                temp.substring("data:".length(), temp.length());
            }
            rs = JSONObject.parseObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputReader != null) {
                try {
                    inputReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return rs;
    }
}
