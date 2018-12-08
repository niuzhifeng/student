package com.student.enumconst;

/**
 * 错误码记录
 */
public enum ResultEnumConst {

    UNKNOW_ERROR("9999","未知错误"),
    NOT_FOUND_ERROR("404","丢失异常"),
    SUCCESS("200","成功");
    
    private String errCode;
    private String errMsg;

    private ResultEnumConst(String errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
