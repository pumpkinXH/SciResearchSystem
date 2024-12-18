package com.xh.sys.service;

import com.xh.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
public interface IRoleService extends IService<Role> {

    void addRole(Role role);

    Role getRoleById(Integer roleId);

    void updateRole(Role role);

    void deleteRoleById(Integer roleId);
}
