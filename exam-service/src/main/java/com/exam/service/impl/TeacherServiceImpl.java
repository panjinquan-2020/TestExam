package com.exam.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.exam.common.CommonData;
import com.exam.dao.TeacherMapper;
import com.exam.domain.Teacher;
import com.exam.domain.vo.PageVO;
import com.exam.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    private Logger log=LoggerFactory.getLogger(TeacherServiceImpl.class);
    @Override
    public void save(Teacher teacher) {
        System.out.println(teacher.getTname());
        String pinyin = PinyinUtil.getPinyin(teacher.getTname(), "");
        teacher.setMnemonicCode(pinyin);
        String pass = DigestUtil.md5Hex(teacher.getPass());
        teacher.setPass(pass);
        try {
            teacherMapper.insert(teacher);
        }catch (DuplicateKeyException e){
            log.info("名称或助记码重复[{}]",teacher.getTname());
            throw e;
        }
    }

    @Override
    public Teacher findByName(String tname) {
        return teacherMapper.findByName(tname);
    }

    @Override
    public void updatePwd(Long tid, String pass) {
        teacherMapper.updatePwd(tid,pass);
    }

    @Override
    public PageVO find(int page, int rows, String tname) {
        if (page < 1) {
            page = 1;
        }
        long total = teacherMapper.total(tname);
        int max = (int) (total = (total % rows == 0 ? total / rows : total / rows + 1));
        max=Math.max(1,max);
        if (page>max){
            page=max;
        }
        int start=(page-1)*rows;
        int length=rows;
        List<Teacher> teacherList = teacherMapper.find(start, length, tname);
        System.out.println(teacherList);
        Map<String, Object> condition=new HashMap<>();
        condition.put("tname",tname);
        return new PageVO(page,rows,total,max,start,start+length-1,teacherList,condition);
    }

    @Override
    public Teacher findById(Long id) {
        return teacherMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Teacher teacher) {
        teacherMapper.updateByPrimaryKeySelective(teacher);
    }

    @Override
    public void deleteAll(String ids) {
        String[] idArray=ids.split(",");
        for (String id:idArray){
            teacherMapper.deleteByPrimaryKey(Long.valueOf(id));
        }
    }

    @Override
    public String saves(List<Teacher> teacherList) {
        String msg="";
        int count1=0;
        int count2=0;
        for (Teacher teacher:teacherList){
            teacher.setPass(CommonData.DEFAULT_PASS);
            try {
                this.save(teacher);
                count1++;
            }catch (DuplicateKeyException e){
                log.debug("名称或助记码重复",teacher.getTname());
                count2++;
                msg+="【"+teacher.getTname()+"】记录因为名称重复导致失败|";
            }
        }
        msg="共导入记录【"+teacherList.size()+"】条|成功导入记录【"+count1+"】条|失败导入记录【"+count2+"】条";
        return null;
    }
}