package com.student.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截器
 */
public class LoginHandlerInterCeptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info("preHandle:" + request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws IOException{
        logger.info("postHandle:" + request.getRequestURI());
        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if("homeUri".equals(cookie.getName())){
//                response.sendRedirect(cookie.getValue());
//            }
//        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.info("afterCompletion:" + request.getRequestURI());
    }
}
