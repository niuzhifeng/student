package com.student.util;

import com.student.enumconst.RedisEnumConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录发送短信和邮件认证工具类
 */
@Component
public class LoginAuthUtil {

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 使用HTML模版发送邮件
     * @param email
     * @param token
     */
    public void loginAuth(String email,String token){
        String themeName = "登录验证";

        StringBuffer detail = new StringBuffer();
        detail.append("您正在进行账号登陆验证，请点击链接");
        detail.append("<a href='http://localhost:1111/pestudent/auth?token=" + token +"' target='_blank'>");
        detail.append("<font color='red'>登录认证</font></a>");
        detail.append("进行验证！");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String,String> map = new HashMap();
        map.put("token",token );
        map.put("sendTime", sdf.format(new Date()));
        
        redisUtil.saveObject4Expire(RedisEnumConst.LOGIN_AUTH_SEND_EMAIL + ":" + email, map,1, TimeUnit.SECONDS);
        mailUtil.sendHtmlMail(email,themeName,detail.toString());
    }
    
}
