package com.exam.controller;

import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.exam.common.CommonData;
import com.exam.domain.Teacher;
import com.exam.domain.vo.PageVO;
import com.exam.service.TeacherService;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class TeacherController {
    Logger log = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private TeacherService teacherService;

    @RequestMapping("/teacher/teacher.html")
    public String toTeacher(Model model) {
        PageVO pageVO = teacherService.find(CommonData.DEFAULT_PAGE, CommonData.DEFAULT_ROWS, null);
        model.addAttribute("pageVO", pageVO);
        return "teacher/teacher";
    }

    @RequestMapping("/teacher/pageTemplate.html")
    public String toPageTemplate(Integer pageNo, String tname, Model model) {
        PageVO pageVO = teacherService.find(pageNo, CommonData.DEFAULT_ROWS, tname);
        model.addAttribute("pageVO", pageVO);
        return "teacher/teacher::#pageTemplate";
    }

    @RequestMapping("/teacher/formTemplate.html")
    public String toFormTemplate(Long id, Model model) {
        if (id != null) {
            Teacher teacher = teacherService.findById(id);
            model.addAttribute("teacher", teacher);
        }
        return "teacher/formTemplate";
    }

    @RequestMapping("/teacher/save")
    @ResponseBody
    public boolean save(Teacher teacher) {
        log.debug("tname:[{}]", teacher.getTname());
        teacher.setPass(CommonData.DEFAULT_PASS);
        try {
            teacherService.save(teacher);
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    @RequestMapping("/teacher/update")
    @ResponseBody
    public boolean update(Teacher teacher) {
        teacher.setMnemonicCode(PinyinUtil.getPinyin(teacher.getTname(), ""));
        try {
            teacherService.update(teacher);
            return true;
        } catch (DuplicateKeyException e) {
            log.debug("名称或助记码重复 [{},{}]", teacher.getTname(), teacher.getMnemonicCode());
            return false;
        }
    }
    @RequestMapping("/teacher/deleteAll")
    @ResponseBody
    public void deleteAll(String ids){
        teacherService.deleteAll(ids);
    }
    @RequestMapping("/teacher/importsTemplate.html")
    public String toImports(){
        return "teacher/importsTemplate";
    }
    @RequestMapping("/teacher/downTemplate")
    public ResponseEntity<byte[]> downTemplate() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("files/teacher.xlsx");
        byte[] bs=new byte[is.available()];
        int read = IOUtils.read(is, bs);
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition","attachment;filename=teachers.xlsx");
        headers.add("content-type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return new ResponseEntity<byte[]>(bs,headers, HttpStatus.OK);
    }
    @RequestMapping("/teacher/imports")
    @ResponseBody
    public String imports(MultipartFile excel) throws IOException {
        System.out.println(excel.getOriginalFilename());
        CommonsMultipartFile cFile = (CommonsMultipartFile) excel;
        DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
        InputStream inputStream = fileItem.getInputStream();

        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("教师名称","tname");
        List<Teacher> teacherList = reader.readAll(Teacher.class);
        String msg = teacherService.saves(teacherList);
        return msg;

    }
}
