package com.pig4cloud.pigx.common.core.constant;


/**
 * 为了书写方便，此处俩个必须要整除，可以省去很多不必要的判断。  另外如果自己测试，可以改为100,20
 */
public class ExcelConstant {


    /**
     * 每次向EXCEL写入的记录数(查询每页数据大小)
     */
    public static final Integer PER_WRITE_ROW_COUNT = 100;
    public static final Integer PER_WRITE_ROW_COUNT_BIG = 10000;

    /**
     * 每个sheet存储的记录数
     */
    public static final Integer PER_SHEET_ROW_COUNT = 500;
    public static final Integer PER_SHEET_ROW_COUNT_BIG = 50000;

    /**
     * 在durableTime时间(分钟)内，相同条件的导出过程不能重复执行，
     * 0.防止重复查询数据库。
     * 1.防止由并发引起EXCEL数据覆盖.
     * 2.durableTime时间过期时，允许数据覆盖
     */
    public static final Integer DURABLE_TIME=20;

    public static  final long EXCEL_REQUEST_OUT_TIME=55*1000L;
}
