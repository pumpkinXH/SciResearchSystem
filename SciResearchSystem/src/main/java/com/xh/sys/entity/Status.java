package com.xh.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@TableName("status")
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "status_id", type = IdType.AUTO)
    private Integer statusId;

    /**
     * 状态名
     */
    private String statusName;

    /**
     * 状态描述
     */
    private String statusDesc;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    @Override
    public String toString() {
        return "Status{" +
            "statusId=" + statusId +
            ", statusName=" + statusName +
            ", statusDesc=" + statusDesc +
        "}";
    }
}
