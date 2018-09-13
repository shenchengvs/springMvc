package com.holley.mvc.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.holley.mvc.comenum.LoginFailReasonEnum;
import com.holley.mvc.model.def.MyGlobals;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

    /**
     * 每次登录都会到这里来，这里用来处理 不注销之前已登录用户下，再次登录
     */
    // @Override
    // protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    // if (isLoginRequest(request, response)) {
    // if (isLoginSubmission(request, response)) {
    // // 本次用户登陆账号
    // String account = this.getUsername(request);
    //
    // Subject subject = this.getSubject(request, response);
    // // 之前登陆的用户
    // CurrentUser user = (CurrentUser) subject.getPrincipal();
    // // 如果两次登陆的用户不一样，则先退出之前登陆的用户,（有问题，相同用户无法跳转页面）解决：可以不判断，都退出之前的登录，再重新登录
    // if (account != null && user != null && !account.equals(user.getUserName())) {
    // // 获取session，获取验证码
    // HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    // HttpSession session = httpServletRequest.getSession();
    // String sRand = (String) session.getAttribute("sRand");
    // // 注销登录，同时会使session失效
    // subject.logout();
    // // 所以重新设置session
    // HttpSession session1 = httpServletRequest.getSession();
    // session1.setAttribute("sRand", sRand);
    // }
    // }
    // }
    //
    // return super.isAccessAllowed(request, response, mappedValue);
    // }

    /**
     * 重写FormAuthenticationFilter的onLoginSuccess方法 指定的url传递进去，这样就实现了跳转到指定的页面；
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.getAndClearSavedRequest(request);// 清理了session中保存的请求信息
        WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
        return false;
    }

    /**
     * 验证码验证
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response) && isLoginSubmission(request, response)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            // 从session获取验证码，正确的验证码
            HttpSession session = httpServletRequest.getSession();
            String saveValidCode = (String) session.getAttribute(MyGlobals.VALID_CODE_KEY);
            // 获取输入的验证码
            String validCode = httpServletRequest.getParameter(MyGlobals.VALID_CODE_KEY);

            // 验证失败，设置错误信息
            if (StringUtils.isEmpty(saveValidCode) || StringUtils.isEmpty(validCode) || !saveValidCode.equals(validCode)) {
                httpServletRequest.setAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, LoginFailReasonEnum.VALID_CODE_ERROR.getText());
                // 拒绝访问
                return true;
            }
        }
        return super.onAccessDenied(request, response);
    }
}
