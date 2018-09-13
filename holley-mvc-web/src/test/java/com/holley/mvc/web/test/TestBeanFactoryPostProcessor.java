package com.holley.mvc.web.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class TestBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    public TestBeanFactoryPostProcessor() {
        super();
        System.out.println("运行TestBeanFactoryPostProcessor的构造函数");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("运行TestBeanFactoryPostProcessor的postProcessBeanFactory");
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("testBean");
        beanDefinition.getPropertyValues().add("age", 12);
    }

}
