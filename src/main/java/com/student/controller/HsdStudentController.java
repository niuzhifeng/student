package com.student.controller;

import com.student.bean.Photo;
import com.student.util.ResponseEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 河师大同学
 *
 * @author nzf
 * @date 2018-07-24
 */
@RestController
@RequestMapping("/beijing")
public class HsdStudentController extends ResponseEntityUtil{

    //单个文件
    private File or;
    //文件夹下文件列表
    private File[] files;
    
    @GetMapping("/photo")
    public ResponseEntity photosFor103(HttpServletRequest request){
        System.out.print(request.getCookies());
        String path = "/Users/nzf/IdeaProjects/macbook4nzf/student/src/main/resources/static/images/103";
        return success(iteratorPath(path));
    }

    /**
     * 用于遍历文件夹     
     */
    private List<Photo> iteratorPath(String dir) {
        or = new File(dir);
        files = or.listFiles();
        //存储所有文件的路径信息
        List<Photo> list = new ArrayList<>();
        if (null != files) {
            for (File file : files) {
                if (file.isFile()) {
                    Photo photo = new Photo();
                    photo.setName("path" + file.getName());    
                    photo.setRealPath("/images/103" + File.separator+ file.getName());
                    list.add(photo);
                }else if (file.isDirectory()) {    
                    iteratorPath(file.getAbsolutePath());
                }
            }
        }
        return list;
    }
}
