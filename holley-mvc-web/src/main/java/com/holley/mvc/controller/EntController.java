package com.holley.mvc.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.holley.mvc.common.util.QinNiuUtil;
import com.holley.mvc.mapper.ObjEnterpriseMapper;
import com.holley.mvc.model.ObjEnterprise;
import com.holley.mvc.model.def.CurrentUser;
import com.holley.mvc.model.def.ExcelBean;
import com.holley.mvc.model.def.JsonResultBean;
import com.holley.mvc.service.BaseService;
import com.holley.mvc.service.CommonService;

@Controller
@RequestMapping("/ent")
public class EntController extends BaseController {

    public final static Logger logger = Logger.getLogger(EntController.class);

    @Resource
    private CommonService      commonService;
    @Resource
    private BaseService        baseService;

    @RequestMapping("/queryEnt")
    @ResponseBody
    public JsonResultBean queryEnt() {
        ObjEnterprise ent = commonService.selectEnterpriseByPrimaryKey(2);
        JsonResultBean resultBean = createJsonResultBean();
        resultBean.setData(ent);
        return resultBean;
    }

    @RequestMapping("/queryJson")
    @ResponseBody
    public JsonResultBean queryJson() throws Exception {
        JsonResultBean rs = createJsonResultBean();// 创建结果bean
        PageInfo<ObjEnterprise> pageInfo = registerPage();
        // baseService.registerMapperBean(ObjEnterpriseMapper.class).selectByExample(null);
        baseService.getMapperBean(ObjEnterpriseMapper.class).selectByExample(null);
        rs.setData(pageInfo);// 封装分页参数
        return null;
    }

    @RequestMapping("/uploadFilePage")
    public String uploadFilePage() {
        CurrentUser c = getCurrentUser();
        System.out.println(getShiroCurrentUser().getSession().getAttribute("permissions"));
        return "uploadFile";
    }

    @RequestMapping(value = "/testPage", method = RequestMethod.GET)
    public String testPage() {
        return "testPage";
    }

    @RequestMapping(value = "/queryEntInfo", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultBean queryEntInfo(String name, @RequestParam(value = "ages[]") Integer[] ages) {
        JsonResultBean rs = wrapQueryEnt(false);
        return rs;
    }

    @RequestMapping(value = "/queryEntExcel", method = RequestMethod.POST)
    public String queryEntExcel(String name, Integer age) {
        wrapQueryEnt(true);
        return EXPORT_EXCEL_VIEW;
    }

    private JsonResultBean wrapQueryEnt(boolean isExcel) {
        if (isExcel) {
            List<ObjEnterprise> list = commonService.selectEnterpriseByPage(null);
            ExcelBean excelBean = createExcelBean("企业信息", list);
            excelBean.setTitleProperie("企业编码", "eid");
            excelBean.setTitleProperie("企业名称", "disc");
            excelBean.setTitleProperie("地址", "abbr");
            return null;
        } else {
            JsonResultBean rs = createJsonResultBean();// 创建结果bean
            PageInfo<ObjEnterprise> pageInfo = registerPage();// 注册分页
            commonService.selectEnterpriseByPage(null);// 分页查询
            rs.setData(pageInfo);// 封装分页参数
            return rs;
        }
    }

    // 七牛上传文件
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultBean uploadFile() throws Exception {
        JsonResultBean rs = createJsonResultBean();// 创建结果bean
        QinNiuUtil.uploadFile(request);
        return rs;
    }
}
