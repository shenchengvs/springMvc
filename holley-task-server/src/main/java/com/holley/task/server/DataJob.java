package com.holley.task.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.dom4j.Document;

import com.alibaba.fastjson.JSON;
import com.holley.task.model.PointVo;
import com.holley.task.util.JobUtil;
import com.holley.task.util.XmlUtil;

public class DataJob implements Runnable {

    public final static Logger                              logger          = Logger.getLogger(DataJob.class);
    private ConcurrentHashMap<String, Map<String, PointVo>> datas           = new ConcurrentHashMap<String, Map<String, PointVo>>();
    private boolean                                         isRun           = true;
    private List<String>                                    successDatas    = new ArrayList<String>();
    private List<String>                                    failDatas       = new ArrayList<String>();
    private long                                            successDateTime = System.currentTimeMillis();
    private long                                            failDateTime    = System.currentTimeMillis();

    public DataJob(List<PointVo> list) {
        initData(list);
    }

    private void initData(List<PointVo> list) {
        for (PointVo vo : list) {
            if (datas.containsKey(vo.getRtuAddr())) {
                Map<String, PointVo> map = datas.get(vo.getRtuAddr());
                map.put(vo.getPointAddr(), vo);
            } else {
                Map<String, PointVo> map = new HashMap<String, PointVo>();
                map.put(vo.getPointAddr(), vo);
                datas.put(vo.getRtuAddr(), map);
            }
        }
    }

    @Override
    public void run() {
        while (isRun) {
            logger.info("run");
            if (datas.isEmpty() && successDatas.isEmpty() && failDatas.isEmpty()) {
                logger.info("break");
                break;
            }
            for (Map.Entry<String, Map<String, PointVo>> entry : datas.entrySet()) {
                System.out.println("in...");
                String rtuAddr = entry.getKey();
                Map<String, PointVo> map = entry.getValue();
                if (isEnd(map)) {
                    datas.remove(rtuAddr);
                    continue;
                }
                for (Map.Entry<String, PointVo> en : map.entrySet()) {
                    PointVo vo = en.getValue();
                    if (canCmd(vo)) {
                        queryCmd(vo);
                    }
                }
            }
            if (isSuccessDelay()) {
                doSaveSuccessDatas();
            }
            if (isFailDelay()) {
                doSaveFailDatas();
            }
            try {
                if (datas.isEmpty()) {
                    TimeUnit.SECONDS.sleep(5);
                } else {
                    TimeUnit.MILLISECONDS.sleep(500);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean canCmd(PointVo vo) {
        if (vo.getStatus() > 0) {
            return false;
        }
        Map<String, PointVo> map = datas.get(vo.getRtuAddr());
        for (Map.Entry<String, PointVo> entry : map.entrySet()) {
            PointVo temp = entry.getValue();
            if (temp.getStatus() == 0 && !temp.getPointAddr().equals(vo.getPointAddr())) {
                if (temp.getStep() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void queryCmd(PointVo pointVo) {
        try {
            String requestString = XmlUtil.createXML(pointVo);
            logger.info("请求:" + requestString);
            logger.info(Thread.currentThread() + "---pointVo:=====" + JSON.toJSONString(pointVo));
            // String rs = HttpUtil.httpUrlConnection(ServerConfig.url, requestString);
            // Document document = XmlUtil.strToDocument(rs);
            // test
            Document document = XmlUtil.strToDocument(XmlUtil.createXMLtest());
            // test
            String status = document.getRootElement().element("task").elementText("status");
            // logger.info("返回:" + rs);
            logger.info("返回:" + document.asXML());
            if ("1".equals(status)) {
                // if (true) {
                if (pointVo.getStep() >= ServerConfig.step.get(ServerConfig.step.size() - 1)) {
                    pointVo.setStatus(1);
                    wrapSuccessDatas(pointVo);
                    successDateTime = System.currentTimeMillis();
                    if (successDatas.size() >= ServerConfig.maxSaveSuccessDataSize) {
                        doSaveSuccessDatas();
                    }
                } else {
                    pointVo.setQueryCount(1);
                    int currentStep = pointVo.getStep();
                    if (currentStep == 1) {
                        // 设置出厂开关设置AA级密码
                        String dateTime = document.getRootElement().element("task").element("items").elementText("item");
                        pointVo.setAmmCtrlPasswd(createAApwd(dateTime, pointVo.getPointAddr()));
                    }
                    int index = ServerConfig.step.indexOf(currentStep);
                    index++;
                    if (index <= (ServerConfig.step.size() - 1)) {
                        pointVo.setStep(ServerConfig.step.get(index));
                    }

                }

            } else {
                pointVo.setQueryCount(pointVo.getQueryCount() + 1);
            }
        } catch (Exception e) {
            pointVo.setQueryCount(pointVo.getQueryCount() + 1);
            logger.error(e.getMessage());
        }
        if (pointVo.getQueryCount() >= ServerConfig.queryCount) {
            pointVo.setStatus(2);
            wrapFailDatas(pointVo);
            failDateTime = System.currentTimeMillis();
            if (failDatas.size() >= ServerConfig.maxSaveFailDataSize) {
                doSaveFailDatas();
            }
        }
    }

    private boolean isEnd(Map<String, PointVo> map) {
        int size = 0;
        for (Map.Entry<String, PointVo> entry : map.entrySet()) {
            if (entry.getValue().getStatus() > 0) {
                size++;
            }
        }
        if (size == map.size()) {
            return true;
        }
        return false;
    }

    private boolean isSuccessDelay() {
        long currentTime = System.currentTimeMillis();
        long tem = (currentTime - successDateTime) / 1000;// 秒
        tem = tem / 60;// 分
        return tem >= ServerConfig.maxSaveSuccessDateTime;
    }

    private boolean isFailDelay() {
        long currentTime = System.currentTimeMillis();
        long tem = (currentTime - failDateTime) / 1000;// 秒
        tem = tem / 60;// 分
        return tem >= ServerConfig.maxSaveFailDateTime;
    }

    private void wrapSuccessDatas(PointVo pointVo) {
        StringBuilder b = new StringBuilder();
        b.append(pointVo.getRtuAddr()).append(",");
        b.append(pointVo.getPointAddr()).append(",");
        b.append(pointVo.getCommPort()).append(",");
        b.append(pointVo.getPointOrder()).append(",");
        successDatas.add(b.toString());
    }

    private void doSaveSuccessDatas() {
        if (!successDatas.isEmpty()) {
            List<String> l = new ArrayList<String>();
            l.addAll(successDatas);
            JobUtil.execute(new SaveSuccessDataJob(l));
            successDatas.clear();
        }
    }

    private void wrapFailDatas(PointVo pointVo) {
        StringBuilder b = new StringBuilder();
        b.append(pointVo.getRtuAddr()).append(",");
        b.append(pointVo.getPointAddr()).append(",");
        b.append(pointVo.getCommPort()).append(",");
        b.append(pointVo.getPointOrder()).append(",");
        failDatas.add(b.toString());
    }

    private void doSaveFailDatas() {
        if (!failDatas.isEmpty()) {
            List<String> l = new ArrayList<String>();
            l.addAll(failDatas);
            JobUtil.execute(new SaveFailDataJob(l));
            failDatas.clear();
        }
    }

    private String createAApwd(String dateTime, String pointAddr) {
        String temTime = dateTime.substring(dateTime.length() - 6, dateTime.length());
        String temAddr = pointAddr.substring(pointAddr.length() - 6, pointAddr.length());
        int dateTimei = Integer.valueOf(temTime);
        int pointAddri = Integer.valueOf(temAddr);
        int t = pointAddri ^ dateTimei;
        return t + "";
    }

}
