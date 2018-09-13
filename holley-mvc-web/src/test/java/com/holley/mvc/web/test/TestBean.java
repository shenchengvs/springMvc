package com.holley.mvc.web.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class TestBean implements BeanFactoryAware, BeanNameAware, InitializingBean, DisposableBean {

    private String name;
    private int    age;

    public void setName(String name) {
        System.out.println("运行TestBean的setName");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        System.out.println("运行TestBean的setAge");
        this.age = age;
    }

    public TestBean() {
        System.out.println("运行TestBean的构造函数");
    }

    public void myInit() {
        System.out.println("运行TestBean的myInit");
    }

    public void myDestroy() {
        System.out.println("运行TestBean的myDestroy");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("运行DisposableBean的destroy");

    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("运行BeanNameAware的setBeanName");

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("运行BeanFactoryAware的setBeanFactory");

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("运行InitializingBean的afterPropertiesSet");

    }
}
