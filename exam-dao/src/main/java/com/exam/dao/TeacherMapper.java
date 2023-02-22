package com.exam.dao;

import com.exam.domain.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Teacher record);

    int insertSelective(Teacher record);

    Teacher selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);

    Teacher findByName(String tname);

    void updatePwd(@Param("tid") Long tid, @Param("pass") String pass);
    long total(String tname);

    List<Teacher> find(@Param("start")int start,@Param("length") int length,@Param("tname") String tname);
}