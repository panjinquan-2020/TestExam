package com.exam.service;

import com.exam.domain.Teacher;
import com.exam.domain.vo.PageVO;

import java.util.List;

public interface TeacherService {
    void save(Teacher teacher);
    Teacher findByName(String tname);
    void updatePwd(Long tid,String pass);
    PageVO find(int page, int rows, String tname);
    Teacher findById(Long id);
    void update(Teacher teacher);
    void deleteAll(String ids);
    String saves(List<Teacher> teacherList);
}
