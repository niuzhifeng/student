package com.student.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 自定义监听器
 */
@WebListener
public class MyListener implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("MyListener contextInitialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("MyListener contextDestroyed.");
    }
}
