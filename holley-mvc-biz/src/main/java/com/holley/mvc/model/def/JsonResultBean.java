package com.holley.mvc.model.def;

import com.holley.mvc.comenum.RetTypeEnum;

/**
 * 通用数据调用结果返回bean
 * 
 * @author sc
 */
public class JsonResultBean {

    private int    ret = 0;                            // 返回参数编码
    private String msg = RetTypeEnum.SUCCESS.getText(); // 返回信息
    private Object data;                               // 数据

    public JsonResultBean() {
        super();
    }

    public JsonResultBean(RetTypeEnum retTypeEnum) {
        this.ret = retTypeEnum.getValue();
        this.msg = retTypeEnum.getText();
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() throws Exception {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
