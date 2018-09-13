package com.holley.mvc.biz.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;

/**
 * 获取动态代理类
 * 
 * @author sc
 */
public class ProxyUtil {

    public static <T> T returnAroundProxy(T t, MethodInterceptor methodInterceptor) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(t);
        proxyFactory.addAdvice(methodInterceptor);
        return (T) proxyFactory.getProxy();
    }

    public static <T> T returnBeforeAndAfterProxy(T t, MethodBeforeAdvice methodBeforeAdvice, AfterReturningAdvice afterReturningAdvice) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(t);
        if (methodBeforeAdvice != null) {
            proxyFactory.addAdvice(methodBeforeAdvice);
        }
        if (afterReturningAdvice != null) {
            proxyFactory.addAdvice(afterReturningAdvice);
        }
        return (T) proxyFactory.getProxy();
    }

}
