package com.student.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataCheckMapper {


    /**
     * 查询数据库是否存在
     * @param dbName 数据库名称
     * @return
     */
    String getDb(@Param("dbName") String dbName);

    /**
     * 查询数据表是否存在
     * @param tableName 数据表名称
     * @return
     */
    Map getTable(@Param("tableName") String tableName);

    /**
     * 获取数据表的列
     * @param columns 查询列
     * @param tableName 数据表名称
     * @return
     */
    List<String> getColums(@Param("columns") List<String> columns,
                     @Param("tableName") String tableName);

    /**
     * 根据表名称获取数据
     * @param columns 查询列
     * @param condition 查询条件
     * @param tableName 数据表
     * @return List
     */
    Map<String,Object> mapByTableName(@Param("columns") List<String> columns,
                                      @Param("condition") Map<String,Object> condition,
                                      @Param("tableName") String tableName);
}

