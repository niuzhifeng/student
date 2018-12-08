package com.student.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * druid动态数据源配置
 *
 *@author niuzhifeng
 *@date 2018/8/25 17:24
 */
@Configuration
@EnableTransactionManagement
public class DynamicDataSourceConfig {

    @Value("${mybatis.type-aliases-package:com.student.mapper}")
    private String typeAliasesPackage;

    @Value("${mybatis.mapperLocations:classpath*:database/mapper/*.xml}")
    private String mappermLocations;

    /***
     * 主数据源连接
     **/
    @Bean(name="masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql.master")
    @Primary
    public DataSource masterDataSource(){
        return new DruidDataSource();
    }

    /***
     * 第二数据源连接
     **/
    @Bean(name = "secondaryDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql.second")
    public DataSource secondaryDatasource() {
        return new DruidDataSource();
    }

    @Bean
    public DynamicDataSourceContextHolder dataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                                     @Qualifier("secondaryDatasource") DataSource secondaryDatasource) {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceEnum.masterDataSource, masterDataSource);
        targetDataSources.put(DataSourceEnum.secondaryDatasource, secondaryDatasource);

        //设置数据源id
        DynamicDataSourceContextHolder.dataSourceIds.add(DataSourceEnum.masterDataSource.name());
        DynamicDataSourceContextHolder.dataSourceIds.add(DataSourceEnum.secondaryDatasource.name());

        DynamicDataSourceContextHolder dataSource = new DynamicDataSourceContextHolder();

        // 默认的datasource设置为myTestDbDataSource
        dataSource.setDefaultTargetDataSource(masterDataSource);
        // 该方法是AbstractRoutingDataSource的方法
        dataSource.setTargetDataSources(targetDataSources);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource,
                                               @Qualifier("secondaryDatasource") DataSource secondaryDatasource) throws Exception {

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        //返回数据驼峰设置
//        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setObjectWrapperFactory(new MapWrapperFactory());
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(this.dataSource(masterDataSource, secondaryDatasource));
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mappermLocations));
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSourceContextHolder dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}