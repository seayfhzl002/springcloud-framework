package com.pig4cloud.pigx.common.core.util.excel;

import com.pig4cloud.pigx.common.core.util.EasyExcelUtils;
import lombok.Getter;

@Getter
public class ExcelPage {

    /**
     * 查询总数
     */
    private int totalRowCount;

    /**
     * 每个sheet存储的记录数
     */
    private int perSheetRowCount;


    /**
     * 每次向EXCEL写入的记录数(查询每页数据大小)
     */
    private int pageSize;

    /**
     *
     */
    private int previousSheetWriteCount;

    private int lastSheetWriteCount;

    private int sheetCount;

    public ExcelPage(int totalRowCount){
        this.totalRowCount=totalRowCount;
        this.perSheetRowCount= EasyExcelUtils.getPerSheetRowCount(totalRowCount);
        this.pageSize=EasyExcelUtils.getperWriteRowCount(totalRowCount);
        this.sheetCount=totalRowCount % perSheetRowCount == 0 ? (totalRowCount / perSheetRowCount) : (totalRowCount / perSheetRowCount + 1);
        this.previousSheetWriteCount=perSheetRowCount / pageSize;
        this.lastSheetWriteCount=totalRowCount % perSheetRowCount == 0 ?
                previousSheetWriteCount :
                (totalRowCount % perSheetRowCount % pageSize == 0 ? totalRowCount % perSheetRowCount / pageSize : (totalRowCount % perSheetRowCount / pageSize + 1));
    }

}
