package com.holley.mvc.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class BaseInterceptor implements HandlerInterceptor {

    public final static String JSON_RESULT          = "jsonResult";
    public final static String JSON_RESULT_BEAN_KEY = "jsonResultBeanKey";

    protected String getCookieByName(String cookieName, HttpServletRequest request) {
        Cookie allCookie[] = request.getCookies();
        if (allCookie != null && allCookie.length != 0) {
            for (int i = 0; i < allCookie.length; i++) {
                String keyname = allCookie[i].getName();
                String value = allCookie[i].getValue();
                if (StringUtils.equals(cookieName, keyname)) {
                    return value;
                }
            }
        }
        return null;
    }

    protected boolean isPermission(String permission) {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isPermitted(permission);
    }

    protected boolean isAjaxRequest(HttpServletRequest httpServletRequest) {
        return "XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"));
    }
}
