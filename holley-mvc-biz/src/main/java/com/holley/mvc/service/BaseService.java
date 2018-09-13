package com.holley.mvc.service;

import java.util.List;

public interface BaseService {

    int countByExample(Object example) throws Exception;

    int deleteByExample(Object example) throws Exception;

    int deleteByPrimaryKey(Object key) throws Exception;

    int insert(Object record) throws Exception;

    int insertSelective(Object record) throws Exception;

    List<Object> selectByExample(Object example) throws Exception;

    Object selectByPrimaryKey(Object key) throws Exception;

    int updateByExampleSelective(Object record, Object example) throws Exception;

    int updateByExample(Object record, Object example) throws Exception;

    int updateByPrimaryKeySelective(Object record) throws Exception;

    int updateByPrimaryKey(Object record) throws Exception;

    BaseService registerMapperBean(Class clazz);

    <T> T getMapperBean(Class<T> clazz);
}
