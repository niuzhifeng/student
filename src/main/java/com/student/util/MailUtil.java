package com.student.util;

import freemarker.template.Template;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * 邮件工具类
 */

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 发送简单文本
     * @param toUserName  发送给
     * @param themeName   主题
     * @param detail      详情     
     * @throws Exception
     */
    public void sendSimpleMail(String toUserName,String themeName,String detail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(toUserName); //自己给自己发送邮件
        message.setSubject(themeName);
        message.setText(detail);
        mailSender.send(message);
    }

    /**
     * 发送HTML邮件
     * @param toUserName  发送给
     * @param themeName   主题
     * @param detail      详情 
     */
    public void sendHtmlMail(String toUserName,String themeName,String detail) {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setSentDate(new Date());
            helper.setValidateAddresses(true);
            helper.setTo(toUserName);
            helper.setSubject(themeName);
            helper.setText(detail, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);
    }

    /**
     * 发送带有附件的邮件
     * @param toUserName  发送给
     * @param themeName   主题
     * @param detail      详情
     * @param filePath    附件路径
     */
    public void sendAttachmentsMail(String toUserName,String themeName,String detail,String filePath) {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(toUserName);
            helper.setSubject(themeName);
            helper.setText(detail);
            //注意项目路径问题，自动补用项目路径
            FileSystemResource file = new FileSystemResource(new File(filePath));
            //加入邮件
            helper.addAttachment(file.getFilename(), file);
        } catch (Exception e){
            e.printStackTrace();
        }
        mailSender.send(message);
    }

    /**
     * 发送带静态资源的邮件
     * @param toUserName  发送给
     * @param themeName   主题
     * @param detail      详情
     * @param filePath    附件路径
     */
    public void sendInlineMail(String toUserName,String themeName,String detail,String filePath) {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(toUserName);
            helper.setSubject(themeName);
            //第二个参数指定发送的是HTML格式,同时cid:是固定的写法
            helper.setText(detail);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addInline(file.getClass().getTypeName(),file);
        } catch (Exception e){
            e.printStackTrace();
        }
        mailSender.send(message);
    }

    /**
     * 发送模板邮件
     * @param toUserName  发送给
     * @param themeName   主题
     * @param detail      详情 
     */
    public void sendTemplateMail(String toUserName,String themeName,String detail){
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(toUserName);
            helper.setSubject(themeName);

            Map<String, Object> model = new HashedMap();
            model.put("username", username);

            //修改 application.properties 文件中的读取路径
            FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
            configurer.setTemplateLoaderPath("classpath:templates");
            //读取 html 模板
            Template template = configurer.getConfiguration().getTemplate("mail.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);
    }
}
