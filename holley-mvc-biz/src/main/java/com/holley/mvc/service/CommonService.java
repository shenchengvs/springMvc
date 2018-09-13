package com.holley.mvc.service;

import java.util.List;
import java.util.Map;

import com.holley.mvc.model.ObjEnterprise;

public interface CommonService {
	 ObjEnterprise selectEnterpriseByPrimaryKey(Integer eid);
	 
	 List<ObjEnterprise> selectEnterpriseByPage(Map<String,Object> map);
}
