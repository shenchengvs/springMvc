package com.holley.mvc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.holley.mvc.comenum.RetTypeEnum;
import com.holley.mvc.model.def.CurrentUser;
import com.holley.mvc.model.def.ExcelBean;
import com.holley.mvc.model.def.JsonResultBean;
import com.holley.mvc.model.def.MyGlobals;

public abstract class BaseController {

    // VIEW DEF
    protected final static String EXPORT_EXCEL_VIEW = "exportExcelView"; // 导出excel返回
    protected final static String MSG               = "base/msg";       // 结果信息页面

    // default value
    protected final static int    DEFAULT_PAGE_NUM  = 1;                // 默认页码
    protected final static int    DEFAULT_PAGE_SIZE = 20;               // 默认每页条数
    protected final static String PAGE_NUM_KEY      = "pageNum";        // pageNum
    protected final static String PAGE_SIZE_KEY     = "pageSize";       // pageSize

    // init param
    protected HttpServletRequest  request;
    protected HttpServletResponse response;
    protected HttpSession         session;
    protected Model               model;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response, Model model) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        this.model = model;
    }

    protected JSONObject getThreadLocalRs() {
        JSONObject rs = MyGlobals.threadLocal.get();
        MyGlobals.threadLocal.remove();
        return rs;
    }

    protected JsonResultBean createJsonResultBean() {
        return new JsonResultBean();
    }

    protected void addModel(String key, Object value) {
        model.addAttribute(key, value);
    }

    protected void addModelExcel(Object value) {
        addModel(MyGlobals.EXCEL_BEAN_KEY, value);
    }

    protected ExcelBean createExcelBean(String excelName, List list) {
        ExcelBean excelBean = new ExcelBean(excelName, list);
        addModelExcel(excelBean);
        return excelBean;
    }

    protected String getCookieByName(String cookieName) {
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

    protected Cookie saveToCookie30Min(String cookieName, String cookieValue) throws Exception {
        return saveToCookie(cookieName, cookieValue, 60 * 30);// cookie保存30分钟
    }

    protected Cookie saveToCookie(String cookieName, String cookieValue) throws Exception {
        return saveToCookie(cookieName, cookieValue, -1);// cookie保存当前会话
    }

    protected Cookie saveToCookie(String cookieName, String cookieValue, int maxAge) throws Exception {
        Cookie cookie = null;
        if (this.getCookieByName(cookieName) == null) {
            cookie = new Cookie(cookieName, cookieValue);
        } else {
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals(cookieName)) {
                    cookie.setValue(cookieValue);
                    break;
                }

            }
        }
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);// 秒
        response.addCookie(cookie);
        return cookie;
    }

    protected boolean isExportExcel() {
        String isExport = request.getParameter("isExport");
        return StringUtils.equals("true", isExport);
    }

    protected String getRemoteIP() {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    protected String getSessionID() {
        String jsessionID = session.getId();
        if (jsessionID.length() > 32) {
            jsessionID = jsessionID.substring(0, 32);
        }
        return jsessionID;
    }

    protected int getParameterInt(String key) {
        return NumberUtils.toInt(request.getParameter(key));
    }

    protected int getParameterInt(String key, int defaultValue) {
        int value = getParameterInt(key);
        value = value > 0 ? value : defaultValue;
        return value;
    }

    protected String getParameterString(String key) {
        return request.getParameter(key);
    }

    protected String getParameterString(String key, String defaultValue) {
        String value = getParameterString(key);
        value = StringUtils.isNotEmpty(value) ? value : defaultValue;
        return value;
    }

    protected String getUUID() {
        String uuid = UUID.randomUUID().toString();
        String token = uuid.replace("-", "");
        return token;
    }

    /**
     * type:"image/jpeg"
     * 
     * @param type
     */
    protected void setImgContentType(String type) {
        response.setContentType(type);
        response.setHeader("Pragma", "no-cache");// 禁止缓存。
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

    }

    protected Object getRequestAttr(String key) {
        return request.getAttribute(key);
    }

    protected Object getSessionAttr(String key) {
        return session.getAttribute(key);
    }

    protected void setRequestAttr(String key, Object value) {
        request.setAttribute(key, value);
    }

    protected void setSessionAttr(String key, Object value) {
        session.setAttribute(key, value);
    }

    protected Subject getShiroCurrentUser() {
        return SecurityUtils.getSubject();
    }

    protected CurrentUser getCurrentUser() {
        return (CurrentUser) getShiroCurrentUser().getPrincipal();
    }

    protected void currentUserLogout() {
        getShiroCurrentUser().logout();
    }

    protected boolean isCurrentUserLogin() {
        Subject subject = getShiroCurrentUser();
        return (subject.isAuthenticated() || subject.isRemembered());
    }

    /**
     * 分页注册器在需要分页的sql前注册
     * 
     * @return
     */
    protected <T> PageInfo<T> registerPage() {
        Page<T> startPage = PageHelper.startPage(getParameterInt(PAGE_NUM_KEY, DEFAULT_PAGE_NUM), getParameterInt(PAGE_SIZE_KEY, DEFAULT_PAGE_SIZE));
        return new PageInfo<T>(startPage);// 需要分页;
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResultBean catchException(HttpServletRequest request, Exception e) {
        System.out.println("catchException............");
        e.printStackTrace();
        JsonResultBean rs = null;
        if (e instanceof MissingServletRequestParameterException) {
            rs = new JsonResultBean(RetTypeEnum.PARAM_ERROR);
        } else {
            rs = new JsonResultBean(RetTypeEnum.SYS_ERROR);
        }
        return rs;
    }
}
