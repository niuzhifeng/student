package com.student.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {

    public static ResponseEntity success(){
        return  new ResponseEntity(HttpStatus.OK);
    }
    
    public static ResponseEntity success(Object data){
        ResutlUtil resutlUtil = new ResutlUtil("200",data);
        return  new ResponseEntity(resutlUtil, HttpStatus.OK);
    }

    public static ResponseEntity fail(String errCode,Object data){
        ResutlUtil resutlUtil = new ResutlUtil("1",errCode,data);
        return  new ResponseEntity(resutlUtil, HttpStatus.valueOf(404));
    }
}
