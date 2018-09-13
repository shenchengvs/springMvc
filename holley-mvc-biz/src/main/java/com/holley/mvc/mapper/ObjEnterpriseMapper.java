package com.holley.mvc.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.mvc.model.ObjEnterprise;
import com.holley.mvc.model.ObjEnterpriseExample;

public interface ObjEnterpriseMapper {
    int countByExample(ObjEnterpriseExample example);

    int deleteByExample(ObjEnterpriseExample example);

    int deleteByPrimaryKey(Integer eid);

    int insert(ObjEnterprise record);

    int insertSelective(ObjEnterprise record);

    List<ObjEnterprise> selectByExample(ObjEnterpriseExample example);

    ObjEnterprise selectByPrimaryKey(Integer eid);

    int updateByExampleSelective(@Param("record") ObjEnterprise record, @Param("example") ObjEnterpriseExample example);

    int updateByExample(@Param("record") ObjEnterprise record, @Param("example") ObjEnterpriseExample example);

    int updateByPrimaryKeySelective(ObjEnterprise record);

    int updateByPrimaryKey(ObjEnterprise record);
    
    List<ObjEnterprise> selectEnterpriseByPage(Map<String,Object> map);
}