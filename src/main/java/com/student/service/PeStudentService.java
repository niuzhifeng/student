package com.student.service;


import com.student.bean.PeStudent;
import com.student.mapper.PeStudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeStudentService {

    @Autowired
    private PeStudentMapper peStudentMapper;

    /**
     * 验证登陆
     * @param student
     * @return
     */
    public PeStudent authLogin(PeStudent student){
        return peStudentMapper.authLogin(student);
    }

    /**
     * 添加学生
     * @param student
     * @return
     */
    public PeStudent insert(PeStudent student){
        return peStudentMapper.insert(student);
    }

    /**
     * 删除学生
     * @param loginId
     * @return
     */
    public int deleteByPrimaryKey(String loginId){
        return peStudentMapper.deleteByPrimaryKey(loginId);
    }

    /**
     * 修改学生
     * @param student
     * @return
     */
    public int updateByPrimaryKey(PeStudent student){
        return peStudentMapper.updateByPrimaryKey(student);
    }

    /**
     * 通过主键得到一个学生
     * @param id
     * @return
     */
    public PeStudent selectByPrimaryKey(String id){
        return peStudentMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过用户名得到一个学生
     * @param loginId
     * @return
     */
    public PeStudent selectByLoginId(String loginId){
        return peStudentMapper.selectByLoginId(loginId);
    }

    /**
     * 得到所有学生
     * @return
     */
    public List<PeStudent> selectAllPeStudent(){
        return peStudentMapper.selectAllPeStudent();
    }
}
