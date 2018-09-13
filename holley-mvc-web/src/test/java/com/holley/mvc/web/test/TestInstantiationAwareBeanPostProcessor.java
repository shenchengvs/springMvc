package com.holley.mvc.web.test;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

public class TestInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    public TestInstantiationAwareBeanPostProcessor() {
        super();
        System.out.println("运行TestInstantiationAwareBeanPostProcessor的构造函数");
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("运行TestInstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation参数Class");
        return super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("运行TestInstantiationAwareBeanPostProcessor的postProcessAfterInstantiation返回boolean");
        return super.postProcessAfterInstantiation(bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("运行TestInstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation参数Object");
        return super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("运行TestInstantiationAwareBeanPostProcessor的postProcessAfterInstantiation返回Object");
        return super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        System.out.println("运行TestInstantiationAwareBeanPostProcessor的postProcessPropertyValues");
        pvs.getPropertyValue("age").setConvertedValue(19);
        return pvs;
    }

}
