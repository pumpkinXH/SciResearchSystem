package com.xh.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@TableName("menu")
@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单id
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    /**
     * 组件
     */
    private String component;

    /**
     * 地址
     */
    private String path;

    /**
     * 指定跳转路径
     */
    private String redirect;

    /**
     * 名称
     */
    private String name;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 父菜单id
     */
    private Integer parentId;

    /**
     * 是否为叶子菜单
     */
    private String isLeaf;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    @TableField(exist = false)  //因为在表里不存在该字段，所以要加这个注解
    @JsonInclude(JsonInclude.Include.NON_EMPTY)  //非空时才会体现
    private List<Menu> children;

    @TableField(exist = false)
    private Map<String, Object> meta;

    public Map<String, Object> getMeta(){
        meta = new HashMap<>();
        meta.put("title", title);
        meta.put("icon",icon);
        return meta;
    }
}
