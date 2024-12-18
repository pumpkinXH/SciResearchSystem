package com.xh.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.lettuce.core.StrAlgoArgs;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Data
@TableName("notice")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Integer noticeId;

    /**
     * 通知信息标题
     */
    private String noticeTitle;

    /**
     * 通知详情
     */
    private String noticeDesc;

    /**
     * 通知文件
     */
    private String noticeFileUrl;

    /**
     * 所属学院
     */
    private Integer schoolId;

    /**
     * 通知时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime noticeTime;

    //通知时间的字符串类型
    @TableField(exist = false)
    private String noticeTimeString;

    //通知学院名
    @TableField(exist = false)
    private String schoolDesc;

    /**
     * 删除标志
     */
    private Integer deleted;

    //将传入的字符串类型的通知时间转为LocalDateTime类型
    public void setNoticeTimeFormString(String timeStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.noticeTime = LocalDateTime.parse(timeStr, formatter);
    }
}
