package com.student.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 存储后端实际运用数据库表名称比对字段及条件
 *
 * @author niuzhifeng
 * @date 2018-11-24 03:00:00
 */
public class DataCheckPO implements Serializable{

    /**
     * 查询的数据列
     */
    private List<String> column;

    /**
     * 查询的条件
     */
    private Map<String,Object> condition;

    /**
     * 源数据
     */
    private String origin;

    /**
     * 目标数据
     */
    private String target;

    public DataCheckPO(){}


    public List<String> getColumn() {
        return column;
    }

    public void setColumn(List<String> column) {
        this.column = column;
    }

    public Map<String,Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String,Object> condition) {
        this.condition = condition;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
