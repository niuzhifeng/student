package com.student.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class CommonConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${com.application.name:defaultApplication}")
    private String applicationName;

    @PostConstruct
    private void init(){
        logger.info("init method:" + applicationName);
    }
}
