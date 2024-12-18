package com.xh.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.common.vo.Result;
import com.xh.sys.entity.Role;
import com.xh.sys.entity.User;
import com.xh.sys.mapper.RoleMapper;
import com.xh.sys.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Api(tags = {"角色接口"})  //Swagger文档注解
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @Resource
    private RoleMapper roleMapper;

    @ApiOperation("角色信息列表显示")  //Swagger文档注解
    @GetMapping("/list")
    public Result<Map<String, Object>> getRoleList(@RequestParam(value = "roleName",required = false) String roleName,
                                                   @RequestParam(value = "roleDesc",required = false) String roleDesc,
                                                   @RequestParam(value = "pageNo") Long pageNo,
                                                   @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(roleName),Role::getRoleName, roleName); //传入的字符串长度大于0再做拼接
        wrapper.like(StringUtils.hasLength(roleDesc),Role::getRoleDesc, roleDesc);
        Page<Role> page = new Page<>(pageNo,pageSize);
        roleService.page(page,wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("新增角色")  //Swagger文档注解
    @PostMapping
    public Result<?> addRole(@RequestBody Role role){ //json转Request对象
        roleService.addRole(role);
        return Result.success("新增角色成功");
    }

    @ApiOperation("修改角色信息")  //Swagger文档注解
    @PutMapping
    public Result<?> updateRole(@RequestBody Role role){ //修改
        roleService.updateRole(role); //密码传入为空，则代表不会更新密码
        return Result.success("修改角色成功");
    }

    @ApiOperation("根据角色id查询")  //Swagger文档注解
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer roleId){
        Role role = roleService.getRoleById(roleId);
        return Result.success(role);
    }

    @ApiOperation("根据角色id删除")  //Swagger文档注解
    @DeleteMapping("/{id}")
    public Result<Role> deleteRoleById(@PathVariable("id") Integer roleId){
        roleService.deleteRoleById(roleId);
        return Result.success("删除角色成功");
    }

    @ApiOperation("返回所有角色权限")  //Swagger文档注解
    @GetMapping("/all")
    public Result<List<Role>> getAllRole(){
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
    }

    @ApiOperation("返回除系统管理员外的角色权限")  //Swagger文档注解
    @GetMapping("/allExAd")
    public Result<List<Role>> getAllRoleExceptAdmin(){
        List<Role> roleList = roleMapper.getAllRoleExceptAdmin();
        return Result.success(roleList);
    }
}
