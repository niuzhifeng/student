package com.student.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义过滤器
 */
@WebFilter(filterName = "myFilter")
public class MyFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected List<Pattern> patternList = new ArrayList();

    @Override
    public void init(FilterConfig filterConfig) {
        Pattern pattern = Pattern.compile(".*");
        patternList.add(pattern);
        logger.info("MyFilter的init方法加载了。。。。。。。");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        logger.info("MyFilter的doFilter方法启动了。。。。。。。");
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        logger.info("requestURI:{}" , httpServletRequest.getRequestURI());
        if(isInWhiteList(httpServletRequest.getRequestURI())){
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        logger.info("MyFilter的destroy方法结束了。。。。。。。");
    }

    private boolean isInWhiteList(String url){
        for (Pattern pattern : patternList) {
            Matcher matcher = pattern.matcher(url);
            if(matcher.matches()){
                return true;
            }
        }
        return false;
    }
}
