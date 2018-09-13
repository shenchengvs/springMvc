package com.holley.mvc.service.impl;

import java.util.List;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import com.holley.mvc.biz.util.CommonUtil;
import com.holley.mvc.biz.util.ReflectUtil;
import com.holley.mvc.service.BaseService;

@Service("baseService")
public class BaseServiceImpl extends ApplicationObjectSupport implements BaseService {

    private Object obj = null;

    @Override
    public BaseService registerMapperBean(Class clazz) {
        this.obj = getApplicationContext().getBean(CommonUtil.toLowerCaseFirstOne(clazz.getSimpleName()));
        return this;
    }

    @Override
    public int countByExample(Object example) throws Exception {
        return (int) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("countByExample", obj.getClass()), example);
    }

    @Override
    public int deleteByExample(Object example) throws Exception {
        return (int) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("deleteByExample", obj.getClass()), example);
    }

    @Override
    public int deleteByPrimaryKey(Object key) throws Exception {
        return (int) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("deleteByPrimaryKey", obj.getClass()), key);
    }

    @Override
    public int insert(Object record) throws Exception {
        return (int) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("insert", obj.getClass()), record);
    }

    @Override
    public int insertSelective(Object record) throws Exception {
        return (int) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("insertSelective", obj.getClass()), record);
    }

    @Override
    public List<Object> selectByExample(Object example) throws Exception {
        return (List<Object>) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("selectByExample", obj.getClass()), example);
    }

    @Override
    public Object selectByPrimaryKey(Object key) throws Exception {
        return ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("selectByPrimaryKey", obj.getClass()), key);
    }

    @Override
    public int updateByExampleSelective(Object record, Object example) throws Exception {
        return (int) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("updateByExampleSelective", obj.getClass()), example);
    }

    @Override
    public int updateByExample(Object record, Object example) throws Exception {
        return (int) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("updateByExample", obj.getClass()), example);
    }

    @Override
    public int updateByPrimaryKeySelective(Object record) throws Exception {
        return (int) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("updateByPrimaryKeySelective", obj.getClass()), record);
    }

    @Override
    public int updateByPrimaryKey(Object record) throws Exception {
        return (int) ReflectUtil.executeMethod(obj, ReflectUtil.getMethodByName("updateByPrimaryKey", obj.getClass()), record);
    }

    @Override
    public <T> T getMapperBean(Class<T> clazz) {
        return (T) getApplicationContext().getBean(CommonUtil.toLowerCaseFirstOne(clazz.getSimpleName()));
    }
}
