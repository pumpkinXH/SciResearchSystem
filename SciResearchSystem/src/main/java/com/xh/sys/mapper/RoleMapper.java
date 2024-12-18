package com.xh.sys.mapper;

import com.xh.sys.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select role_desc from role")
    List<String> getRoleDesc();

    @Select("select* from role where role_id!=1")
    List<Role> getAllRoleExceptAdmin();

    @Select("select* from role where role_id!=1 and role_id!=2")
    List<Role> getAllRoleExceptAdminColAdmin();

    @Select("select* from role where role_id!=3 and role_id!=4")
    List<Role> getAllRoleExceptSchAdminSci();
}
