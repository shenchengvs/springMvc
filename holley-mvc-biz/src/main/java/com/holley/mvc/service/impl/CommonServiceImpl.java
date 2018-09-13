package com.holley.mvc.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.holley.mvc.mapper.ObjEnterpriseMapper;
import com.holley.mvc.model.ObjEnterprise;
import com.holley.mvc.service.CommonService;

@Service("commonService")
public class CommonServiceImpl implements CommonService {

    @Resource
    private ObjEnterpriseMapper objEnterpriseMapper;

    @Override
    public ObjEnterprise selectEnterpriseByPrimaryKey(Integer eid) {
        return objEnterpriseMapper.selectByPrimaryKey(eid);
    }

    @Override
    public List<ObjEnterprise> selectEnterpriseByPage(Map<String, Object> map) {
        return objEnterpriseMapper.selectEnterpriseByPage(map);
    }

}
