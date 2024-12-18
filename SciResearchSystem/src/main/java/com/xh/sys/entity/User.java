package com.xh.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 学号/工号
     */
    private String card;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

//    /**
//     * 用户状态
//     */
//    private Integer userStatus;

    /**
     * 电话
     */
    private String userPhone;

    /**
     * 电子邮件
     */
    private String userEmail;

    /**
     * 角色id
     */
    private Integer userRoleId;

    /**
     * 所属学院id
     */
    private Integer userSchoolId;

    /**
     * 头像
     */
    private String userAvatar;

    /**
     * 用户删除标志
     */
    private Integer deleted;

    @TableField(exist = false)
    private String roleDesc;

    @TableField(exist = false)
    private String schoolDesc;

    @TableField(exist = false)
    private String newPassword;
}
