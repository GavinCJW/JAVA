package com.test.demo.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class ExcelUtil {

    private static boolean _log(Map<String,Object> data , String key[]){
        for (String k: key) {
            if(!data.containsKey(k)){
                System.out.println("缺少"+k+"参数");
                return false;
            }
        }
        return true;
    }

    /*
    data{
        ExcelType, boolean(true:xls,false:xlsx)
        Title, String
        SheetName, String
        HeaderFlag, boolean
        PJClass, model.class
        List, List<model>
        FileName,String
    }*/
    public static void exportExcel(Map<String,Object> data , HttpServletResponse response){
        final String key[] = {"ExcelType","Title","SheetName","HeaderFlag","PJClass","List","FileName"};

        if(!_log(data,key)) return;

        ExcelType type = (boolean)data.get("ExcelType") ? ExcelType.HSSF : ExcelType.XSSF;
        String path = (String)data.get("FileName") + ((boolean)data.get("ExcelType") ? ".xls" : ".xlsx");
        ExportParams exportParams = new ExportParams((String)data.get("Title"), (String)data.get("SheetName") , type);
        exportParams.setCreateHeadRows((boolean)data.get("HeaderFlag"));

        Workbook workbook = ExcelExportUtil.exportExcel(new ArrayList<Map<String, Object>>(){
            {
                add(new HashMap<String,Object>(){
                    {
                        put("title",exportParams);
                        put("entity",data.get("PJClass"));
                        put("data",data.get("List"));
                    }
                });
            }
        }, type);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(path, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    data{
       TitleRows , Integer
       HeaderRows , Integer
       PJClass , model.class
       File , String(filepath) : MultipartFile(file)
    }
     */
    public static <E> List<E> importExcel(Map<String,Object> data){
        final String key[] = {"TitleRows","HeaderRows","PJClass","File"};

        if(!_log(data,key)) return null;

        ImportParams params = new ImportParams();
        params.setTitleRows((Integer)data.get("TitleRows"));
        params.setHeadRows((Integer)data.get("HeaderRows"));
        List<E> list = null;

        try {
            if(data.get("File") instanceof String) {
                list = ExcelImportUtil.importExcel(new File((String) data.get("File")), (Class<?>) data.get("PJClass"), params);
            }else{
                MultipartFile file = (MultipartFile)data.get("File");
                list = ExcelImportUtil.importExcel(file.getInputStream(), (Class<?>) data.get("PJClass"), params);
            }
        }catch (NoSuchElementException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
