package com.student.controller;

import com.student.bean.DataCheckVO;
import com.student.service.DataCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/v1/data")
@RestController
public class DataCheckController {

    @Autowired
    private DataCheckService dataCheckService;

    /**
     * 比对数据
     * {
     *      column":"id,login_id",
     *      condition":"{'login_id':'20180002'}",
     *      originTable":"pe_student_tmp",
     *      targetTable":"pe_student"
     *  }
     *
     *
     * @param dataCheckVO 前端传输的数据表名称比对字段及条件
     * @return
     */
    @PostMapping(value = "/check",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity check(@RequestBody DataCheckVO dataCheckVO){
        return new ResponseEntity(dataCheckService.checkData(dataCheckVO), HttpStatus.OK);
    }
}
