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
 * @since 2023-04-13
 */
@TableName("files")
@Data
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件唯一标识md5
     */
    private String md5;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 删除标志
     */
    private Integer deleted;
}
