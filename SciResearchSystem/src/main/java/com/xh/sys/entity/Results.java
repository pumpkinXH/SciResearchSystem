package com.xh.sys.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Data
@TableName("results")
public class Results implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成果编号
     */
    @ExcelIgnore
    @TableId(value = "results_id", type = IdType.AUTO)
    private Integer resultsId;

    /**
     * 成果名称
     */
    @ExcelProperty(value = "成果名", index = 0)
    private String resultsName;

    /**
     * 成果所属人编号
     */
    @ExcelIgnore
    private Integer resultsChargeId;

    /**
     * 成果所属部门编号
     */
    @ExcelIgnore
    private Integer resultsSchoolId;

    /**
     * 所属学科
     */
    @ExcelProperty(value = "所属学科", index = 3)
    private String resultsSubject;

    /**
     * 成果类型编号
     */
    @ExcelIgnore
    private Integer typeId;

    /**
     * 成果状态编号
     */
    @ExcelIgnore
    private Integer resultsStatusId;

    /**
     * 成果描述
     */
    @ExcelIgnore
    private String resultsDesc;

    /**
     * 相关文件
     */
    @ExcelIgnore
    private String resultsUrl;

    /**
     * 修改时间
     */
    @ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;

    /**
     * 提交时间
     */
    @ExcelIgnore
    @TableField(updateStrategy= FieldStrategy.IGNORED)  //可以更新为空
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime submitTime;

    //修改时间的字符串类型
    @ExcelIgnore
    @TableField(exist = false)
    private String updateTimeString;

    //提交时间的字符串类型
    @ExcelIgnore
    @TableField(exist = false)
    private String submitTimeString;

    //所属学院名
    @ExcelProperty(value = "所属学院", index = 2)
    @TableField(exist = false)
    private String schoolDesc;

    //成果所属人名
    @ExcelProperty(value = "所属人", index = 1)
    @TableField(exist = false)
    private String resultsChargeName;

    //成果类型名
    @ExcelProperty(value = "成果类别", index = 4)
    @TableField(exist = false)
    private String typeDesc;

    //成果状态名
    @ExcelProperty(value = "成果状态", index = 5)
    @TableField(exist = false)
    private String statusDesc;

    //将传入的字符串类型的修改时间转为LocalDateTime类型
    public void setUpdateTimeFormString(String timeStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.updateTime = LocalDateTime.parse(timeStr, formatter);
    }

    //将传入的字符串类型的提交时间转为LocalDateTime类型
    public void setSubmitTimeFormString(String timeStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.submitTime = LocalDateTime.parse(timeStr, formatter);
    }
}
