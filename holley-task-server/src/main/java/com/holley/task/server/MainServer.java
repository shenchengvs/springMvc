package com.holley.task.server;

import org.apache.log4j.Logger;

public class MainServer {

    public final static Logger logger = Logger.getLogger(MainServer.class);

    public static void main(String[] args) throws Exception {
        ServerConfig.init();
    }
}
