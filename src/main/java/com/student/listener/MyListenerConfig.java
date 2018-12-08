package com.student.listener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注入自定义监听器
 */
@Configuration
public class MyListenerConfig{

    @Bean
    public ServletListenerRegistrationBean ServletContextListener(){
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new MyListener());
        return servletListenerRegistrationBean;
    }
}
