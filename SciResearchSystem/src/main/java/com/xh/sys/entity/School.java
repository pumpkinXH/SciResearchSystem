package com.xh.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Data
@TableName("school")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学院号
     */
    @TableId(value = "school_id", type = IdType.AUTO)
    private Integer schoolId;

    /**
     * 学院名
     */
    private String schoolName;

    /**
     * 学院描述
     */
    private String schoolDesc;
}
