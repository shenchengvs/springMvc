package com.holley.mvc.common.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;

public class ShiroSecurityUtil {

    private final static String salt = "shen.cheng@holley.cn";

    public static String base64Enc(String source) {
        return Base64.encodeToString(source.getBytes());
    }

    public static String base64Dec(String source) {
        return Base64.decodeToString(source);
    }

    public static String md5(String source) {
        return new Md5Hash(source, salt).toString();
    }

    public static String passwordEnc(String source) {
        return base64Enc(md5(source));
    }

    public static String sha256Enc(String source) {
        return new Sha256Hash(source, salt).toString();
    }
}
