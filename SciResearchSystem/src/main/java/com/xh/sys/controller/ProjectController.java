package com.xh.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.common.utils.ExcelUtil;
import com.xh.common.vo.Result;
import com.xh.sys.entity.Notice;
import com.xh.sys.entity.Project;
import com.xh.sys.entity.User;
import com.xh.sys.mapper.ProjectMapper;
import com.xh.sys.mapper.UserMapper;
import com.xh.sys.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@Api(tags = {"项目接口"})  //Swagger文档注解
@RestController
@RequestMapping("/pro")
public class ProjectController {
    @Autowired
    private IProjectService projectService;

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private UserMapper userMapper;

}
