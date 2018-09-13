package com.holley.mvc.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holley.mvc.common.util.ValidateCodeUtil;
import com.holley.mvc.model.def.MyGlobals;
import com.holley.mvc.model.dto.ObjEnterpriseDTO;
import com.holley.mvc.service.CommonService;

/**
 * 公共controller不经过拦截器
 * 
 * @author sc
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

    public final static Logger logger = Logger.getLogger(CommonController.class);

    @Resource
    private CommonService      commonService;

    @RequestMapping("/websocketPage")
    public String websocketPage() throws Exception {
        return "websocket";
    }

    @RequestMapping("/validateCodePage")
    public String validateCodePage() throws Exception {
        return "validateCode";
    }

    @RequestMapping("/validateCode")
    public String validateCode() throws Exception {
        System.out.println(session.getAttribute("time"));
        long time = System.currentTimeMillis();
        session.setAttribute("time", time);

        setImgContentType("image/jpeg");
        ValidateCodeUtil vCode = new ValidateCodeUtil(120, 40, 4, 20);
        System.out.println(vCode.getCode());
        session.setAttribute(MyGlobals.VALID_CODE_KEY, vCode.getCode());
        vCode.write(response.getOutputStream());
        return null;
    }

    @RequestMapping("/login1")
    public String login1(HttpServletRequest request, Model model) {
        return "login";
    }

    /**
     * url传参XXX?paramname1=xxx&paramname2=xxx
     * 
     * @param request
     * @param model
     * @param param1
     * @param param2
     * @return
     */
    @RequestMapping("/login2")
    public String login(HttpServletRequest request, Model model, @RequestParam("paramname1") int param1, @RequestParam("paramname2") String param2) {
        return "login";
    }

    /**
     * url传参：XXX:123 占位符可随意定制
     * 
     * @param request
     * @param model
     * @param paramname
     * @return
     */
    @RequestMapping("/login3:{param}")
    public String login(HttpServletRequest request, Model model, @PathVariable("param") String param) {
        return "login";
    }

    /**
     * ajax异步以对象形式传参， contentType:'application/json' data参数必须穿json字串不是对象
     * 
     * @param request
     * @param model
     * @param dto
     * @return
     */
    @RequestMapping("/test1")
    @ResponseBody
    public String test1(HttpServletRequest request, Model model, @RequestBody ObjEnterpriseDTO dto) {
        logger.info(dto);
        return "help";
    }

    @RequestMapping("/test2")
    @ResponseBody
    public String test2(HttpServletRequest request, Model model) {
        System.out.println(22);
        return "help";
    }

    @RequestMapping("/test3")
    @ResponseBody
    public Map<String, Object> test3(HttpServletRequest request, String name, Date time) {
        System.out.println(33);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("time", time);
        return map;
    }

    @RequestMapping("/test4")
    @ResponseBody
    public ObjEnterpriseDTO test4(HttpServletRequest request, ObjEnterpriseDTO dto) {
        System.out.println(44);
        return dto;
    }
}
