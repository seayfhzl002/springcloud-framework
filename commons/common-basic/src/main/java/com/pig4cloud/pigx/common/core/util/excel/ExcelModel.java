package com.pig4cloud.pigx.common.core.util.excel;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Setter
@Getter
public class ExcelModel implements java.io.Serializable {

    /**
     * 名称
     */
    private String name;
    /**
     * 缓存开始时间
     */
    private long beginTime;
    
    /**
     * 持续时间 (分钟)
     */
    private int durableTime;

    /**
     * excel下载地址
     */
    private String downloadUrl;

    /**
     * 数据是否填充完成
     */
    private boolean finish;

    /**
     *提交人账号
     */
    private String submitterAccount;



}
