package com.holley.mvc.controller;

import holley.dubbo.open.DemoService;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.holley.mvc.service.CommonService;

/**
 * 登录注册级不经过拦截器
 * 
 * @author sc
 */
@Controller
@RequestMapping("/frame")
public class FrameController extends BaseController {

    public final static Logger logger = Logger.getLogger(FrameController.class);

    @Resource
    private CommonService      commonService;
    @Resource
    private DemoService        demoService;

    @RequestMapping(value = "/login")
    public String login() {
        System.out.println(demoService.test().get(0).getName());
        if (isCurrentUserLogin()) {
            return "redirect:/ent/uploadFilePage";
        }
        String errorMsg = (String) getRequestAttr(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        System.out.println(getSessionID());
        if (UnknownAccountException.class.getName().equals(errorMsg)) {
            setRequestAttr("errorMsg", "账户或密码有误");
        } else if (IncorrectCredentialsException.class.getName().equals(errorMsg)) {
            setRequestAttr("errorMsg", "账户或密码有误");
        } else if (!StringUtils.isEmpty(errorMsg)) {
            setRequestAttr("errorMsg", errorMsg);
        }

        return "login";
    }

    @RequestMapping(value = "/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }

    @RequestMapping(value = "/logout")
    public String logout() {
        currentUserLogout();
        return "redirect:login";
    }

}
