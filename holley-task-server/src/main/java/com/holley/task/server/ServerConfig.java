package com.holley.task.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.holley.task.model.PointVo;
import com.holley.task.util.JarUtil;
import com.holley.task.util.JobUtil;

public class ServerConfig {

    public final static String                       CONFIG_IN_PATH         = "config";
    public static String                             successLock            = "successLock";
    public static String                             failLock               = "failLock";
    public static String                             url;                                                                 // 请求接口地址
    public static String                             ttl;                                                                 // 超时时间
    public static String                             protocol;                                                            // 规约
    public static String                             city;                                                                // 城市
    // public static int stepSize;
    public static List<Integer>                      step                   = new ArrayList<Integer>();

    public static int                                size                   = 300;                                        // 每个任务计量点数量
    public static int                                maxSaveSuccessDataSize = 100;
    public static int                                maxSaveSuccessDateTime = 2;
    public static int                                maxSaveFailDataSize    = 100;
    public static int                                maxSaveFailDateTime    = 2;
    public static String                             unSuccessDataPath;
    public static String                             successDataPath;
    public static String                             successDataFile;
    public static String                             failDataPath;
    public static String                             failDataFile;
    public static int                                queryCount             = 5;                                          // 失败几次算失败

    public static String                             baudRate;
    public static String                             checkType;
    public static String                             dataBit;
    public static String                             stopBit;
    public static String                             relayTimeOut;
    public static String                             ammCtrlPasswd;
    public static String                             operCode;

    private static Map<String, Map<String, PointVo>> successDatas           = new HashMap<String, Map<String, PointVo>>();

    public static void init() throws Exception {
        JarUtil jar = new JarUtil(MainServer.class);
        // PropertyConfigurator.configure(jar.getJarPath() + "/" + CONFIG_IN_PATH + "/log4j.properties");
        Properties p = new Properties();
        p.load(new FileInputStream(jar.getJarPath() + "/" + CONFIG_IN_PATH + "/config.properties"));
        // p.load(ServerConfig.class.getClassLoader().getResourceAsStream("config.properties"));
        url = p.getProperty("url");
        ttl = p.getProperty("ttl");
        protocol = p.getProperty("protocol");
        city = p.getProperty("city");
        // stepSize = Integer.valueOf(p.getProperty("stepSize"));
        List<String> stepStrs = Arrays.asList(p.getProperty("step").split(","));
        for (String s : stepStrs) {
            step.add(Integer.valueOf(s));
        }
        size = Integer.valueOf(p.getProperty("size"));
        queryCount = Integer.valueOf(p.getProperty("queryCount"));
        unSuccessDataPath = p.getProperty("unSuccessDataPath");

        successDataPath = p.getProperty("successDataPath");
        successDataFile = p.getProperty("successDataFile");
        maxSaveSuccessDataSize = Integer.valueOf(p.getProperty("maxSaveSuccessDataSize"));
        maxSaveSuccessDateTime = Integer.valueOf(p.getProperty("maxSaveSuccessDateTime"));

        failDataPath = p.getProperty("failDataPath");
        failDataFile = p.getProperty("failDataFile");
        maxSaveFailDataSize = Integer.valueOf(p.getProperty("maxSaveFailDataSize"));
        maxSaveFailDateTime = Integer.valueOf(p.getProperty("maxSaveFailDateTime"));

        baudRate = p.getProperty("baudRate");
        checkType = p.getProperty("checkType");
        dataBit = p.getProperty("dataBit");
        stopBit = p.getProperty("stopBit");
        relayTimeOut = p.getProperty("relayTimeOut");
        ammCtrlPasswd = p.getProperty("ammCtrlPasswd");
        operCode = p.getProperty("operCode");
        initFailData();
        initSuccessData();
        initTaskPool();
    }

    private static PointVo getPointData(String dataStr) {
        String[] tem = dataStr.split(",");
        String rtuAddr = tem[0];
        String pointAddr = tem[1];
        String commPort = tem[2];
        String pointOrder = tem[3];
        if (rtuAddr.length() < 12 || pointAddr.length() < 12 || StringUtils.isEmpty(commPort) || StringUtils.isEmpty(pointOrder)) {
            return null;
        }
        if (rtuAddr.length() > 12) {
            rtuAddr = rtuAddr.substring(rtuAddr.length() - 12, rtuAddr.length());
        }
        if (pointAddr.length() > 12) {
            pointAddr = pointAddr.substring(pointAddr.length() - 12, pointAddr.length());
        }
        PointVo vo = new PointVo();
        vo.setRtuAddr(rtuAddr);
        vo.setPointAddr(pointAddr);
        vo.setCommPort(commPort);
        vo.setPointOrder(pointOrder);
        return vo;
    }

    private static void initFailData() throws Exception {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
        failDataFile = formater.format(new Date()) + "_" + failDataFile;
        File file = new File(failDataPath + failDataFile);
        if (!file.exists()) {
            File dirFile = new File(failDataPath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void initSuccessData() throws Exception {
        File file = new File(successDataPath + successDataFile);
        if (!file.exists()) {
            File dirFile = new File(successDataPath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String r = null;
        Map<String, PointVo> map = null;
        while ((r = reader.readLine()) != null) {
            if (StringUtils.isNotEmpty(r)) {
                PointVo vo = getPointData(r);
                if (vo != null) {
                    if (successDatas.containsKey(vo.getRtuAddr())) {
                        successDatas.get(vo.getRtuAddr()).put(vo.getPointAddr(), vo);
                    } else {
                        map = new HashMap<String, PointVo>();
                        map.put(vo.getPointAddr(), vo);
                        successDatas.put(vo.getRtuAddr(), map);
                    }
                }

            }

        }
    }

    private static boolean isSuccess(PointVo vo) {
        if (successDatas.containsKey(vo.getRtuAddr())) {
            return successDatas.get(vo.getRtuAddr()).containsKey(vo.getPointAddr());
        }
        return false;
    }

    private static void initTaskPool() {
        BufferedReader reader = null;
        String r = null;
        int count = 0;
        String rtuStr = null;
        List<PointVo> list = new ArrayList<PointVo>(size);
        List<PointVo> tem = new ArrayList<PointVo>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(unSuccessDataPath)));
            while ((r = reader.readLine()) != null) {
                if (StringUtils.isNotEmpty(r)) {
                    PointVo vo = getPointData(r);
                    if (vo == null || isSuccess(vo)) {
                        continue;
                    }
                    if (count >= size) {
                        if (rtuStr.equals(vo.getRtuAddr())) {
                            list.add(vo);
                            count++;
                            continue;
                        }
                        tem.addAll(list);
                        JobUtil.execute(new DataJob(tem));
                        count = 0;
                        tem.clear();
                        list.clear();
                        list.add(vo);
                    } else {
                        list.add(vo);
                    }
                    rtuStr = vo.getRtuAddr();
                    count++;
                }
            }
            if (!list.isEmpty()) {
                JobUtil.execute(new DataJob(list));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveSuccessData(String data) {
        BufferedWriter writer = null;
        synchronized (successLock) {
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(successDataPath + successDataFile, true)));
                writer.write(data);
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static void saveFailData(String data) {
        BufferedWriter writer = null;
        synchronized (failLock) {
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(failDataPath + failDataFile, true)));
                writer.write(data);
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
