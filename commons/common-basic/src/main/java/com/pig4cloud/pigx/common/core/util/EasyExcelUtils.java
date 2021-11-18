package com.pig4cloud.pigx.common.core.util;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.pig4cloud.pigx.common.core.constant.ExcelConstant;
import com.pig4cloud.pigx.common.core.exception.BizException;
import com.pig4cloud.pigx.common.core.util.excel.ExcelMgr;
import com.pig4cloud.pigx.common.core.util.excel.ExcelModel;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Slf4j
public class EasyExcelUtils {

    public static String getExcelUrl(HttpServletRequest request,File file,String gateway){
        String excelUrl=gateway+file.getName();
        //String excelUrl=request.getRequestURL().substring(0,request.getRequestURL().lastIndexOf(request.getServletPath()))+"/"+file.getName();
        return excelUrl;
    }

    public static String getExcelUrl(HttpServletRequest request,File file,Boolean isFinish,String gateway,String submitterAccount){
        String excelUrl=EasyExcelUtils.getExcelUrl(request,file,gateway);
        EasyExcelUtils.maintainExcelStatus(file.getName(),excelUrl,isFinish,submitterAccount);
        return excelUrl;
    }

    /**
     * 维护EXCEL状态
     * @param excelUrl
     * @param isFinish
     */
    public static void maintainExcelStatus(String name,String excelUrl,Boolean isFinish,String submitterAccount){
        ExcelMgr.getInstance().addCache(ExcelModel.builder()
                .name(name)
                .downloadUrl(excelUrl)
                .beginTime(System.currentTimeMillis())
                .durableTime(ExcelConstant.DURABLE_TIME)
                .finish(isFinish)
                .submitterAccount(submitterAccount)
                .build());
    }

    /**
     * 是否正在生成文件
     * @param request
     * @param file
     */
    public static boolean isRegeneratingExcel(HttpServletRequest request,File file,String gateway,String submitterAccount){
        String excelUrl=EasyExcelUtils.getExcelUrl(request,file,gateway);
        //String excelUrl=request.getRequestURL().substring(0,request.getRequestURL().lastIndexOf(request.getServletPath()))+"/"+file.getName();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        if(ObjectUtil.isNotEmpty(ExcelMgr.getInstance().getValue(excelUrl)) && !ExcelMgr.getInstance().getValue(excelUrl).isFinish()){
            throw new BizException("same condition export generating ...Please try again later");
            //log.info("same condition export regenerating ...Please try again later");
            //return true;
        }
        EasyExcelUtils.maintainExcelStatus(file.getName(),excelUrl,false,submitterAccount);
        return false;
    }

    public static void write(int sheetIndex, List data, FileOutputStream fileOutputStream, Class excelHeaderClass, String SheetName,ExcelTypeEnum excelTypeEnum) throws  Throwable{
        EasyExcel.write(new BufferedOutputStream(fileOutputStream), excelHeaderClass)
                .excelType(excelTypeEnum)
                .autoCloseStream(Boolean.TRUE)
                .needHead(true)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(SheetName+(++sheetIndex)).doWrite(data);
    }

    /**
     * 每个sheet存储的记录数
     */
    public static  int getPerSheetRowCount(int totalRowCount){
        if (totalRowCount > 5000) {
            return ExcelConstant.PER_SHEET_ROW_COUNT_BIG;
        } else {
            return ExcelConstant.PER_SHEET_ROW_COUNT;
        }
    }

    /**
     * 每次向EXCEL写入的记录数(查询每页数据大小)
     */
    public static  int getperWriteRowCount(int totalRowCount){
        if (totalRowCount > 5000) {
          return ExcelConstant.PER_WRITE_ROW_COUNT_BIG;
        } else {
            return ExcelConstant.PER_WRITE_ROW_COUNT;
        }
    }

}
