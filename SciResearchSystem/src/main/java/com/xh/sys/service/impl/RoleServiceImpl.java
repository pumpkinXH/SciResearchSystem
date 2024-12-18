package com.xh.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xh.sys.entity.Role;
import com.xh.sys.entity.RoleMenu;
import com.xh.sys.mapper.RoleMapper;
import com.xh.sys.mapper.RoleMenuMapper;
import com.xh.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional //多表操作时要加这个
    public void addRole(Role role) {
        // 写入角色表
        this.baseMapper.insert(role);
        // 写入角色和权限菜单的对照表
        if(role.getMenuIdList()!=null){
            for(Integer menuId : role.getMenuIdList()){
                roleMenuMapper.insert(new RoleMenu(null,role.getRoleId(),menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer roleId) {  //获取用户权限菜单
        Role role = this.baseMapper.selectById(roleId);
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(roleId);
        role.setMenuIdList(menuIdList);
        return role;
    }

    @Override
    @Transactional //多表操作的注解
    public void updateRole(Role role) {
        //修改角色表
        this.baseMapper.updateById(role);
        //删除原有权限菜单
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(wrapper);
        //新增权限菜单
        if(role.getMenuIdList()!=null){
            for(Integer menuId : role.getMenuIdList()){
                roleMenuMapper.insert(new RoleMenu(null,role.getRoleId(),menuId));
            }
        }
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer roleId) {
        //删除角色表
        this.baseMapper.deleteById(roleId);
        //删除原有权限菜单
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);
    }
}
