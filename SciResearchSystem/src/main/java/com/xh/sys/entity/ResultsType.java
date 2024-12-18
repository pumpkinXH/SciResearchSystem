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
@TableName("results_type")
public class ResultsType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "results_type_id", type = IdType.AUTO)
    private Integer resultsTypeId;

    /**
     * 成果类别名
     */
    private String resultsTypeName;

    /**
     * 成果类别描述
     */
    private String resultsTypeDesc;
}
