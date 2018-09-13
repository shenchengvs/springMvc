package com.holley.task.model;

public class PointVo {

    private String rtuAddr;      // 终端地址
    private String pointAddr;    // 测量点地址
    private String pointOrder;   // 测量点序号
    private String commPort;     // 端口号
    private String ammCtrlPasswd; // 密码

    private int    step = 1;     // 成功执行到第几步
    private int    queryCount;   // 请求次数
    private int    status;       // 1：成功2：失败

    public String getRtuAddr() {
        return rtuAddr;
    }

    public void setRtuAddr(String rtuAddr) {
        this.rtuAddr = rtuAddr;
    }

    public String getPointAddr() {
        return pointAddr;
    }

    public void setPointAddr(String pointAddr) {
        this.pointAddr = pointAddr;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(int queryCount) {
        this.queryCount = queryCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAmmCtrlPasswd() {
        return ammCtrlPasswd;
    }

    public void setAmmCtrlPasswd(String ammCtrlPasswd) {
        this.ammCtrlPasswd = ammCtrlPasswd;
    }

    public String getPointOrder() {
        return pointOrder;
    }

    public void setPointOrder(String pointOrder) {
        this.pointOrder = pointOrder;
    }

    public String getCommPort() {
        return commPort;
    }

    public void setCommPort(String commPort) {
        this.commPort = commPort;
    }

}
