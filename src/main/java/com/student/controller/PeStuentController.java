package com.student.controller;


import com.google.common.collect.ImmutableMap;
import com.student.bean.PeStudent;
import com.student.enumconst.RedisEnumConst;
import com.student.enumconst.ResultEnumConst;
import com.student.service.PeStudentService;
import com.student.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/pestudent")
public class PeStuentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${server.port:1111}")
    private Integer port;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LoginAuthUtil loginAuthUtil;

    @Autowired
    private PeStudentService peStudentService;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * 登录发邮件验证
     */
    @PostMapping(value = "/login")
    public ResponseEntity<PeStudent> login(HttpServletRequest request,HttpServletResponse response,@NotNull PeStudent student){
        // 先查询用户名是否正确，即用户是否存在
        PeStudent peStudent =  peStudentService.selectByLoginId(student.getLoginId());
        if(null != peStudent){
            // 再确认用户名密码是否都正确
            peStudent = peStudentService.authLogin(student);
            if(null != peStudent){
                // 获取用户名
                String loginId = peStudent.getLoginId();
                // 获取邮箱
                String email = peStudent.getEmail();
                
                if(StringUtils.isNotBlank(email)){
                    //先查询缓存是否token还存在
                    Map<String,String> sendEmailMap = (Map<String, String>) redisUtil.getObject(RedisEnumConst.LOGIN_AUTH_SEND_EMAIL + ":" + email,Map.class);
                    if(MapUtils.isEmpty(sendEmailMap)){
                        String token = JWTHelper.genToken(ImmutableMap.of("loginId",loginId,"email", email,"timeStamp", System.currentTimeMillis() + ""));
                        logger.error("token:{}", token);
                        redisUtil.saveObject4Expire(loginId, token, 30, TimeUnit.MINUTES);
                        //loginAuthUtil.loginAuth(peStudent.getEmail(), token);
                        String remoteHost = request.getScheme() + "://" + request.getServerName() + ":8080";
                        try {
                            if(this.auth(token)){
                                response.sendRedirect("/?access_token=" + token);
                            }else {
                                response.sendRedirect(remoteHost + "/templates/login.html" );
                            }
                        } catch (IOException e) {
                            return ResponseEntityUtil.success("邮件已发送至" + email +"邮箱（5分钟内有效）,请注意查收！");
                        }
                        return null;
                    }else{
                        return ResponseEntityUtil.success("邮件已在" +sendEmailMap.get("sendTime")+ "发送至" + email +"邮（5分钟内有效）,请注意查收！");
                    }
                } else {
                    return ResponseEntityUtil.fail(ResultEnumConst.UNKNOW_ERROR.getErrCode(),"未查询到您的邮箱");
                }
            } else {
                return ResponseEntityUtil.fail(ResultEnumConst.UNKNOW_ERROR.getErrCode(),"用户名或密码错误");

            }
        } else {
            return ResponseEntityUtil.fail(ResultEnumConst.UNKNOW_ERROR.getErrCode(),"用户名不存在");
        }
    }
    
    /**
     * token 验证
     * @param token
     * @return
     */
    @GetMapping("/auth")
    public  boolean auth(@Param("token") String token){
        if(StringUtils.isNotBlank(token)){
            Map<String,String> map = JWTHelper.verifyToken(token);
            String loginId = map.get("loginId");
            String email = map.get("email");

            PeStudent peStudent =  peStudentService.selectByLoginId(loginId);
            if(email.equals(peStudent.getEmail())){
                redisUtil.saveObject4Expire(loginId, token, 30, TimeUnit.MINUTES);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 添加学生
     * @param student
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<PeStudent> insert(@RequestBody PeStudent student){
        PeStudent peStudent = peStudentService.insert(student);
        return new ResponseEntity(peStudent, HttpStatus.OK);
    }

    /**
     * 删除学生
     * @param loginId
     * @return
     */
    @PutMapping("/delete/{loginId}")
    public ResponseEntity deleteByPrimaryKey(@NotNull @PathVariable("loginId") String loginId){
        int result =  peStudentService.deleteByPrimaryKey(loginId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 修改学生
     * @param student
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity updateByPrimaryKey(@RequestBody PeStudent student){
        int result = peStudentService.updateByPrimaryKey(student);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 得到一个学生
     * @param loginId
     * @return
     */
    @GetMapping("/getOne/{loginId}")
    public ResponseEntity<PeStudent> selectByPrimaryKey(@NotNull @PathVariable("loginId") String loginId){
        PeStudent peStudent =  redisUtil.getObject(RedisEnumConst.GET_ONE_STUDENT + ":" + loginId,PeStudent.class);
        if(null == peStudent){
            peStudent = peStudentService.selectByLoginId(loginId);
            redisUtil.saveObject4Expire(RedisEnumConst.GET_ONE_STUDENT+ ":" + loginId, peStudent,10, TimeUnit.MINUTES);
        }
        return new ResponseEntity(peStudent, HttpStatus.OK);
    }

    /**
     * 得到所有学生
     * @return
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<PeStudent>> selectAllPeStudent(){
        List<PeStudent> list =  redisUtil.getObject(RedisEnumConst.GET_ALL_STUDENT,List.class);
        if(CollectionUtils.isEmpty(list)){
            list = peStudentService.selectAllPeStudent();
            redisUtil.saveObject4Expire(RedisEnumConst.GET_ALL_STUDENT,list,10,TimeUnit.MINUTES);
        }
        logger.error("getAll List ,port:{}", port);
        return new ResponseEntity(list, HttpStatus.OK);
    }
}