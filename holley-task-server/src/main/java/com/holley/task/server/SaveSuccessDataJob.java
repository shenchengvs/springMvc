package com.holley.task.server;

import java.util.List;

import org.apache.log4j.Logger;

public class SaveSuccessDataJob implements Runnable {

    public final static Logger logger = Logger.getLogger(SaveSuccessDataJob.class);
    private List<String>       list;

    public SaveSuccessDataJob(List<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        System.out.println("list:" + list);
        if (list != null && !list.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (String s : list) {
                str.append(s).append("\r\n");
            }

            ServerConfig.saveSuccessData(str.toString());
        }
    }
}
