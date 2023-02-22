package com.exam.dao;

import com.exam.domain.Exam;

public interface ExamMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Exam record);

    int insertSelective(Exam record);

    Exam selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Exam record);

    int updateByPrimaryKey(Exam record);
}