package com.student.mapper;

import com.student.bean.PeStudent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 学生mapper
 */
@Mapper
public interface PeStudentMapper {

    /**
     * 验证登陆
     * @param student
     * @return
     */
    PeStudent authLogin(PeStudent student);
    
    /**
     * 添加学生
     * @param student
     * @return
     */
    PeStudent insert(PeStudent student);

    /**
     * 删除学生
     * @param loginId
     * @return
     */
    int deleteByPrimaryKey(String loginId);

    /**
     * 修改学生
     * @param student
     * @return
     */
    int updateByPrimaryKey(PeStudent student);

    /**
     * 通过主键得到一个学生
     * @param id
     * @return
     */
    PeStudent selectByPrimaryKey(String id);

    /**
     * 通过用户名得到一个学生
     * @param loginId
     * @return
     */
    PeStudent selectByLoginId(String loginId);

    /**
     * 得到所有学生
     * @return
     */
    List<PeStudent> selectAllPeStudent();

}
