package com.student.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注入自定义拦截器
 */
@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterCeptor()).addPathPatterns("/**").excludePathPatterns("/error");
    }
}
