package com.holley.mvc.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebLoadListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        /*
         * ApplicationContext con =
         * WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()); CommonService ser =
         * (CommonService)con.getBean("commonService");
         */
        System.out.println("监听器");
    }
}
