package com.student.util;

import com.student.enumconst.ResultEnumConst;

/**
 * 自定义异常处理类
 */
public class PeStudentException extends RuntimeException {

     public void handleException(Object object){
         if(object instanceof PeStudentException){
             ResultEnumConst.UNKNOW_ERROR.getErrCode();
         }else{

         }
     }
}
