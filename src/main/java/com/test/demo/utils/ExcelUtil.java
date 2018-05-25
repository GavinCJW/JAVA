package com.test.demo.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.*;

@Service
public class ExcelUtil {
    //配置参数变量
    private static Map<String,Object> _export = new HashMap<String, Object>(){
        {
            put("ExcelType",true);
            put("Title",null);
            put("HeaderFlag",true);
            put("SheetName",null);
            put("FileName",UUID.randomUUID().toString().split("-")[0].trim());
        }
    };
    private static Map<String,Object> _import = new HashMap<String, Object>(){
        {
            put("TitleRows",0);
            put("HeaderRows",1);
        }
    };

    private static void setData(Map<String,Object> config,Map<String,Object> data){
        if(config == null) return;
        for (String key : config.keySet()){
            data.put(key,config.get(key));
        }
    }

    private static void fileOut(Workbook workbook ,String path,HttpServletResponse response){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(path, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportExcel(Collection<?> Collection , Class<?>Entity ,HttpServletResponse response , Map<String,Object> config){
        setData(config,_export);

        ExcelType type = (boolean)_export.get("ExcelType") ? ExcelType.HSSF : ExcelType.XSSF;
        ExportParams exportParams = new ExportParams((String)_export.get("Title"), (String)_export.get("SheetName") , type);
        exportParams.setCreateHeadRows((boolean)_export.get("HeaderFlag"));

        Workbook workbook = ExcelExportUtil.exportExcel(new ArrayList<Map<String, Object>>(){
            {
                add(new HashMap<String,Object>(){
                    {
                        put("title",exportParams);
                        put("entity",Entity);
                        put("data",Collection);
                    }
                });
            }
        }, type);

        String path = _export.get("FileName") + ((boolean)_export.get("ExcelType") ? ".xls" : ".xlsx");
        fileOut(workbook,path,response);

    }

    public static void exportExcel(Collection<?> Collection , Class<?>Entity ,HttpServletResponse response){
        exportExcel(Collection,Entity,response,null);
    }

    public static void exportExcel(Collection<? extends Map<?, ?>> Collection , Map<String,Object> Entity ,HttpServletResponse response , Map<String,Object> config){
        setData(config,_export);

        ExcelType type = (boolean)_export.get("ExcelType") ? ExcelType.HSSF : ExcelType.XSSF;
        ExportParams exportParams = new ExportParams((String)_export.get("Title"), (String)_export.get("SheetName") , type);
        exportParams.setCreateHeadRows((boolean)_export.get("HeaderFlag"));

        List<ExcelExportEntity> _Entity = new ArrayList<>();
        for(String k : (Entity).keySet()){
            _Entity.add(new ExcelExportEntity((String) Entity.get(k), k));
        }
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, _Entity , Collection);

        String path = _export.get("FileName") + ((boolean)_export.get("ExcelType") ? ".xls" : ".xlsx");
        fileOut(workbook,path,response);
    }

    public static void exportExcel(Collection<? extends Map<?, ?>> Collection , Map<String,Object> Entity ,HttpServletResponse response){
        exportExcel(Collection,Entity,response,null);
    }

    /*********************************************************************************/

    public static <E> Collection<E> importExcel(Object File , Class<?> pojoClass , Map<String,Object> config){
        setData(config,_import);

        ImportParams params = new ImportParams();
        params.setTitleRows((Integer)_import.get("TitleRows"));
        params.setHeadRows((Integer)_import.get("HeaderRows"));

        Collection<E> data = null;
        try {
            if(File instanceof String) {
                data = ExcelImportUtil.importExcel(new File((String) File), pojoClass, params);
            }else if (File instanceof MultipartFile){
                data = ExcelImportUtil.importExcel(((MultipartFile)File).getInputStream(), pojoClass, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }

    public static <E> Collection<E> importExcel(Object File , Class<?> pojoClass){
        return importExcel(File,pojoClass,null);
    }

}
