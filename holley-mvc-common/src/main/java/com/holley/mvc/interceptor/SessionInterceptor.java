package com.holley.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.holley.mvc.comenum.RetTypeEnum;
import com.holley.mvc.common.util.SendMsgUtil;
import com.holley.mvc.model.def.JsonResultBean;

/**
 * 监测用户拦截器
 * 
 * @author sc
 */
public class SessionInterceptor extends BaseInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object obj, Exception e) throws Exception {
        System.out.println("afterCompletion-----SessionInterceptor");
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object obj, ModelAndView model) throws Exception {
        // if (isAjaxRequest(req) && model != null) {
        // // MappingJackson2JsonView view = new MappingJackson2JsonView();
        // // view.setPrettyPrint(false);
        // // model.setView(view);
        // List<Object> rs = new ArrayList<Object>(model.getModel().values());
        // model.clear();
        // SendMsgUtil.sendJsonMessage(res, rs.get(0), false);
        // }
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object obj) throws Exception {
        if (!isPermission(req.getServletPath())) {
            if (isAjaxRequest(req)) {
                JsonResultBean rs = new JsonResultBean(RetTypeEnum.UN_PERMISSION);
                SendMsgUtil.sendJsonMessage(res, rs, false);
            } else {
                res.sendRedirect(req.getContextPath() + "/frame/unauthorized");
            }
            return false;
        }
        return true;
    }

}
