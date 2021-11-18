package com.pig4cloud.pigx.common.core.util;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        for (int i = 0; i < title.length; i++) {
            sheet.autoSizeColumn((short)i); //调整全列宽度
        }
        return wb;
    }

    //发送响应流方法
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static Workbook getWorkBook(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith("xls")) {
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {

        }
        return workbook;
    }

    public static String stringDateProcess(Cell cell) {
        String result = new String();
        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
            SimpleDateFormat sdf = null;
            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                sdf = new SimpleDateFormat("HH:mm");
            } else {// 日期
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            }
            Date date = cell.getDateCellValue();
            result = sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 58) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            double value = cell.getNumericCellValue();
            Date date = DateUtil
                    .getJavaDate(value);
            result = sdf.format(date);
        } else {
            double value = cell.getNumericCellValue();
            CellStyle style = cell.getCellStyle();
            DecimalFormat format = new DecimalFormat();
            String temp = style.getDataFormatString();
            // 单元格设置成常规
            if ("General".equals(temp)) {
                format.applyPattern("#");
            }
            result = format.format(value);
        }

        return result;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = stringDateProcess(cell);
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }



    public static List<List<String[]>> getDataList(MultipartFile excel){
		Workbook workbook = getWorkBook(excel);
		List<List<String[]>> list = new ArrayList<>();
		if (workbook != null) {
			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {//判断excle有几个sheet页
				List<String[]> sheetList = new ArrayList<>();
				Sheet sheet = workbook.getSheetAt(sheetNum);//依次读取每一个sheet页
				if (sheet == null) {//判断当前的sheet页有没有数据
					continue;//如果没有，则跳过当前的循环，进入下次循环
				}
				int firstRowNum = sheet.getFirstRowNum();//读取第一次出现数据的下标（从哪一行开始有数据）
				int lastRowNum = sheet.getLastRowNum();//读取最后一条数据的下标（没有数据的那一行）
				for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
					Row row = sheet.getRow(rowNum);//获取当前行的对象
					if (row == null) {
						continue;
					}
					int firstCellNum = row.getFirstCellNum();//取出第一列的下表
					int lastCellNum = row.getLastCellNum();//取出最后有数据一列的下表
					String[] cells = new String[row.getLastCellNum()];//通过已得打的列数创建对应大小的数组
					for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {//循环取出每一列的数据
						Cell cell = row.getCell(cellNum);//取出该列的数据
						cells[cellNum] = ExcelUtil.getCellValue(cell);//将得到的数据存如刚刚生命的数据中
					}
					sheetList.add(cells);
				}
				list.add(sheetList);
			}
		}
		return list;
	}
	public static void writeFile(String sheetName, String []title, String [][]values, HSSFWorkbook wb, FileOutputStream fileOutputStream) throws IOException {
		HSSFWorkbook hssfWorkbook = getHSSFWorkbook(sheetName, title, values, wb);
		hssfWorkbook.write(fileOutputStream);
	}
}
