package com.holley.mvc.web.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class TestBeanPostProcessor implements BeanPostProcessor {

    public TestBeanPostProcessor() {
        System.out.println("运行TestBeanPostProcessor的构造函数");
    }

    @Override
    public Object postProcessAfterInitialization(Object arg0, String arg1) throws BeansException {
        System.out.println("运行TestBeanPostProcessor的postProcessAfterInitialization");
        System.out.println(arg1);
        return arg0;
    }

    @Override
    public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
        System.out.println("运行TestBeanPostProcessor的postProcessBeforeInitialization");
        System.out.println(arg1);
        return arg0;
    }

}
