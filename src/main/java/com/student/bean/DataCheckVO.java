package com.student.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 存储前端传输的数据表名称比对字段及条件
 *
 * @author niuzhifeng
 * @date 2018-11-24 03:00:00
 */
public class DataCheckVO implements Serializable{

    /**
     * 查询的数据列
     */
    private String column;

    /**
     * 查询的条件
     */
    private String condition;

    /**
     * 源数据
     */
    private String origin;

    /**
     * 目标数据
     */
    private String target;

    public DataCheckVO(){}


    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
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
