package com.holley.mvc.extend;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.holley.mvc.common.util.SendMsgUtil;
import com.holley.mvc.model.def.JsonResultBean;

public class ReturnHandler implements HandlerMethodReturnValueHandler {

    @Override
    public void handleReturnValue(Object obj, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        JsonResultBean bean = (JsonResultBean) obj;
        bean.setMsg("111");
        modelAndViewContainer.setRequestHandled(true);

        SendMsgUtil.sendJsonMessage(nativeWebRequest.getNativeResponse(HttpServletResponse.class), bean, false);

    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        System.out.println(22222);
        return methodParameter.getParameterType() == JsonResultBean.class;
    }

}
