package com.student.bean;

import java.io.Serializable;

/**
 * 图片实体
 *
 * @author nzf
 * @date 2018-01-24 22:00:00
 */
public class Photo implements Serializable {

    private String name;
    private String realPath;

    public Photo(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }
}
