package com.student.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * druid数据源动态切换.
 *
 * @author niuzhifeng
 * @date 2018/8/25 17:24
 */
public class DynamicDataSourceContextHolder extends AbstractRoutingDataSource {

    private static final ThreadLocal<DataSourceEnum> CONTEXT_HOLDER = new ThreadLocal<>();

    public static List<String> dataSourceIds = new ArrayList();

    public static void setDatabaseType(DataSourceEnum type) {
        CONTEXT_HOLDER.set(type);
    }

    public static void clearDataSourceType() {
        CONTEXT_HOLDER.remove();
    }

    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return CONTEXT_HOLDER.get();
    }
}