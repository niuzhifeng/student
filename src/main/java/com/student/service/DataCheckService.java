package com.student.service;

import com.google.gson.Gson;
import com.student.bean.DataCheckPO;
import com.student.bean.DataCheckVO;
import com.student.config.datasource.DataSourceEnum;
import com.student.config.datasource.TargetDataSource;
import com.student.mapper.DataCheckMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DataCheckService {

    @Autowired
    private DataCheckMapper dataCheckMapper;

    // 存放数据表
    private String tableName;
    
    /**
     * 比对数据
     * @param dataCheckVO 前端传输的数据表名称比对字段及条件
     * @return 
     */
    public Map<String,Object> checkData(DataCheckVO dataCheckVO){

        // 存放最终结果
        Map<String,Object> result = this.getDataCheckPO(dataCheckVO);

        if(!(boolean)result.get("success")){
            return result;
        }

        DataCheckPO dataCheckPO = (DataCheckPO)result.get("data");
        Map<String,Object> originMap;
        if(dataCheckPO.getOrigin().indexOf(".") > -1){
            originMap = dataCheckMapper.mapByTableName(dataCheckPO.getColumn(), dataCheckPO.getCondition(), dataCheckPO.getOrigin());
        } else {
            originMap = this.getOriginDataMap(dataCheckPO);
        }
        Map<String,Object> targetMap = this.getTargetDataMap(dataCheckPO);

        List<String> errList = new ArrayList<>();
        String errMsg;
        if(MapUtils.isNotEmpty(originMap)){
            for(String column : dataCheckPO.getColumn()){
                if(MapUtils.isNotEmpty(originMap) && MapUtils.isNotEmpty(targetMap)){
                    Object originValue = originMap.get(column);
                    Object targetValue = targetMap.get(column);
                    if(null != originValue && null != targetValue){

                        if(originValue.hashCode() != targetValue.hashCode()){
                            errMsg = "列：" + column + ",错误数据：" + originValue + "，正确数据：" + targetValue;
                            errList.add(errMsg);
                        }
                    }else if(originValue != targetValue){

                        errMsg = "列：" + column + ",错误数据：" + originValue + "，正确数据：" + targetValue;
                        errList.add(errMsg);
                    }
                }else if(originMap != targetMap){

                    errMsg =  "列：" + column + ",错误数据：" + (originMap == null ? null : originMap.get(column))
                            + "，正确数据：" + (targetMap == null ? null : targetMap.get(column)) + "";
                    errList.add(errMsg);
                }
            }
        }
        result.put("errMsg",errList);
        return result;
    }

    /**
     * 源数据
     * @param dataCheckPO  实体对象
     * @return List
     */
    @TargetDataSource(dataSource = DataSourceEnum.secondaryDatasource)
    private Map<String,Object> getOriginDataMap(DataCheckPO dataCheckPO){
       return dataCheckMapper.mapByTableName(dataCheckPO.getColumn(), dataCheckPO.getCondition(), dataCheckPO.getOrigin());
    }

    /**
     * 目标数据
     * @param dataCheckPO  实体对象
     * @return List
     */
    @TargetDataSource(dataSource = DataSourceEnum.masterDataSource)
    private Map<String,Object> getTargetDataMap(DataCheckPO dataCheckPO){
        return dataCheckMapper.mapByTableName(dataCheckPO.getColumn(), dataCheckPO.getCondition(), dataCheckPO.getTarget());
    }
    
    /**
     * VO 转 PO
     * @param dataCheckVO 前端传输的数据表名称比对字段及条件
     * @return
     */
    private Map<String,Object> getDataCheckPO(DataCheckVO dataCheckVO){


        DataCheckPO dataCheckPO = new DataCheckPO();

        // 表
        dataCheckPO.setOrigin(dataCheckVO.getOrigin());
        dataCheckPO.setTarget(dataCheckVO.getTarget());

        // 列字段
        List<String> columns = new ArrayList<>();
        String columnVO = dataCheckVO.getColumn();
        
        if(StringUtils.isNotBlank(columnVO)){
            if(columnVO.indexOf(",") > -1) {
                CollectionUtils.addAll(columns, columnVO.split(","));
            }else {
                columns.add(columnVO);
            }
        } 
        if(MapUtils.isNotEmpty(validate(dataCheckPO.getOrigin()))){
            return validate(dataCheckPO.getOrigin());
        }
        
        // 查询源数据表的列
        List<String> originColumnList = dataCheckMapper.getColums(columns,tableName);
        columns.clear();
        columns = originColumnList;

        dataCheckPO.setColumn(columns);

        // 校验目标数据
        if(MapUtils.isNotEmpty(validate(dataCheckPO.getTarget()))){
            return validate(dataCheckPO.getTarget());
        }

        // 条件
        Map<String,Object> conditions = new HashMap<>(16);
        if(StringUtils.isNotBlank(dataCheckVO.getCondition())){
            Gson gson = new Gson();
            conditions = gson.fromJson(dataCheckVO.getCondition(), conditions.getClass());
        }
        dataCheckPO.setCondition(conditions);

        // 正确返回
        Map<String,Object> result = new HashMap<>(16);
        result.put("success", true);
        result.put("data", dataCheckPO);
        return result;
    }

    /**
     * 校验源数据和目标数据
     * @param param 源数据或目标数据
     * @return
     */
    private Map<String,Object> validate(String param){

        // 存放校验数据结果集
        Map<String,Object> result = new HashMap<>(16);
        
       if(param.indexOf(".") > -1){
            String dbName = param.split("\\.")[0];
            tableName = param.split("\\.")[1];
            if(StringUtils.isBlank(dataCheckMapper.getDb(dbName))){
                result.put("success", false);
                result.put("errMsg", "数据库：" + dbName + "不存在！");
                result.put("data", null);
                return result;
            }
        } else {
           tableName = param;
       }
                               
       Map tableMap = dataCheckMapper.getTable(tableName);
       if(MapUtils.isEmpty(tableMap)){
           result.put("success", false);
           result.put("errMsg", "数据表：" + tableName + "不存在！");
           result.put("data", null);
           return result;
        }
        
        return result;
    }
}