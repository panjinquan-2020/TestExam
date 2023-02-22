package com.exam.test;

import com.exam.domain.Teacher;
import com.exam.service.TeacherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class Test1 {
    @Autowired
    TeacherService teacherService;
    @Test
    public void t1(){
        Teacher t=new Teacher();
        t.setTname("张三1111");
        t.setPass("123");
        teacherService.save(t);
    }
}
