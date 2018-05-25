package com.test.demo.controller;

import com.test.demo.mapper.UserMapper;
import com.test.demo.model.User;
import com.test.demo.utils.ExcelUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
@RestController
@EnableAutoConfiguration
public class ExcelController {

    @Resource
    private UserMapper _mapper;

    @RequestMapping("/excel/export")
    private void exportExcel(HttpServletResponse response){
        //ExcelUtil.exportExcel(_mapper.get(),User.class,response);
        ExcelUtil.exportExcel(_mapper.get_test(),new HashMap<String,String>(){
            {
                put("status","状态");
                put("name","姓名");
                put("price","金额");
                put("date","生日");
            }
        },response);
    }

    @RequestMapping("/excel/import")
    private Collection<?> importExcel(@RequestParam("File") MultipartFile file){
        System.out.println("dfadsfas");
        return ExcelUtil.importExcel(file,User.class);
    }

}
