package com.xh.sys.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
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
@TableName("project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目号
     */
    @ExcelIgnore
    @TableId(value = "pro_id", type = IdType.AUTO)
    private Integer proId;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名", index = 0)
    private String proName;

    /**
     * 项目负责人编号
     */
    @ExcelIgnore
    private Integer proChargeId;

    /**
     * 所属学院编号
     */
    @ExcelIgnore
    private Integer proSchoolId;

    /**
     * 所属学科
     */
    @ExcelProperty(value = "所属学科", index = 3)
    private String proSubject;

    /**
     * 成果类别号
     */
    @ExcelIgnore
    private Integer resultsTypeId;

    /**
     * 项目状态号
     */
    @ExcelIgnore
    private Integer proStatusId;

    /**
     * 项目成员
     */
    @ExcelIgnore
    private String members;

    /**
     * 项目预算
     */
    @ExcelProperty(value = "项目预算", index = 6)
    private Integer budget;

    /**
     * 立项目的
     */
    @ExcelIgnore
    private String purpose;

    /**
     * 预期成果
     */
    @ExcelIgnore
    private String expectedResult;

    /**
     * 方案分析
     */
    @ExcelIgnore
    private String schemeAnalysis;

    /**
     * 项目申报书
     */
    @ExcelIgnore
    private String proProposalUrl;

    /**
     * 中期检查报告
     */
    @ExcelIgnore
    private String interInspectUrl;

    /**
     * 项目结项书
     */
    @ExcelIgnore
    private String proEndfileUrl;

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

    //项目负责人名
    @ExcelProperty(value = "负责人", index = 1)
    @TableField(exist = false)
    private String proChargeName;

    //成果类别名
    @ExcelProperty(value = "成果类别", index = 4)
    @TableField(exist = false)
    private String resultsTypeDesc;

    //项目状态名
    @ExcelProperty(value = "项目状态", index = 5)
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
