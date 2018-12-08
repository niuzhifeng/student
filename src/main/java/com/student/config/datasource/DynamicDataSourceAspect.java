package com.student.config.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 数据库事务开启之前切换数据库.
 * @author niuzhifeng
 * @date 2018/8/25 17:24
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
        String dsId = ds.dataSource().name();
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            logger.error("数据源[{}]不存在，使用默认数据源 > {}", ds.dataSource(), point.getSignature());
        } else {
            logger.debug("Use DataSource : {} > {}", ds.dataSource(), point.getSignature());
            DynamicDataSourceContextHolder.setDatabaseType(ds.dataSource());
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        logger.debug("Revert DataSource : {} > {}", ds.dataSource(), point.getSignature());
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}
