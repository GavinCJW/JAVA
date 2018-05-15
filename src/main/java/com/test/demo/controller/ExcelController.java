package com.test.demo.controller;

import com.test.demo.Application;
import com.test.demo.mapper.UserMapper;
import com.test.demo.model.User;
import com.test.demo.util.ExcelUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class ExcelController {

    @Resource
    UserMapper _mapper;

    @RequestMapping("/excel/export")
    private void exportExcel(HttpServletResponse response){
        ExcelUtil.exportExcel(new HashMap<String,Object>(){
            {
                put("ExcelType", false);
                put("Title", "花名册");
                put("SheetName", "草帽一伙");
                put("HeaderFlag", true);
                put("PJClass", User.class);
                put("List", _mapper.get());
                put("FileName","海贼王");
            }
        },response);
    }

    @RequestMapping("/excel/import")
    private List<User> importExcel(@RequestParam("File") MultipartFile file){
        List<User> personList = ExcelUtil.importExcel(new HashMap<String,Object>(){
            {
                put("TitleRows",1);
                put("HeaderRows",1);
                put("PJClass",User.class);
                put("File",file/*"D:\\Download\\海贼王.xls"*/);
            }
        });
        return personList;
    }


}
