package com.student.util;


/**
 * 自定义json返回
 */
public class ResutlUtil {

    /** 状态 0成功 1失败*/
    private String status;
    /** 错误码 */
    private String errCode;
    /** 返回数据 */
    private Object data;

    public ResutlUtil(){}
    
    public ResutlUtil(String errCode,Object data){
        this.status = "0";
        this.errCode = errCode;
        this.data = data;
    }

    public ResutlUtil(String status,String errCode,Object data){
       this.status = status;
       this.errCode = errCode;
       this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getErrCode() {
        return errCode;
    }

    public Object getData() {
        return data;
    }
}
