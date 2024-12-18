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

    @ApiOperation("科研人员项目信息列表显示")  //Swagger文档注解
    @GetMapping("/sciList")
    public Result<Map<String, Object>> getSciProList(@RequestParam(value = "proName",required = false) String proName,
                                                  @RequestParam(value = "proChargeId",required = false) String proChargeId,
                                                  @RequestParam(value = "proSchoolId",required = false) String proSchoolId,
                                                  @RequestParam(value = "proSubject",required = false) String proSubject,
                                                  @RequestParam(value = "resultsTypeId",required = false) String resultsTypeId,
                                                  @RequestParam(value = "proStatusId",required = false) String proStatusId,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(proName), Project::getProName, proName); //传入的字符串长度大于0再做拼接
        wrapper.eq(StringUtils.hasLength(proChargeId), Project::getProChargeId, proChargeId);
        wrapper.eq(StringUtils.hasLength(proSchoolId), Project::getProSchoolId, proSchoolId);
        wrapper.like(StringUtils.hasLength(proSubject),Project::getProSubject, proSubject);
        wrapper.eq(StringUtils.hasLength(resultsTypeId),Project::getResultsTypeId, resultsTypeId);
        wrapper.eq(StringUtils.hasLength(proStatusId),Project::getProStatusId, proStatusId);  //带草稿
        wrapper.orderByDesc(Project::getUpdateTime); // 根据修改时间倒序排序
        Page<Project> page = new Page<>(pageNo,pageSize);
        projectService.page(page,wrapper);
        page.getRecords().forEach(index->{
            index.setSchoolDesc(projectMapper.getSchoolDescByProId(index.getProId()));
            index.setProChargeName(projectMapper.getChargeNameByProId(index.getProId()));
            index.setResultsTypeDesc(projectMapper.getResultsTypeDescByProId(index.getProId()));
            index.setStatusDesc(projectMapper.getProStatusDescByProId(index.getProId()));
        });
        Map<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("管理员项目审核信息列表显示")  //Swagger文档注解
    @GetMapping("/auditList")
    public Result<Map<String, Object>> getProAuditList(@RequestParam(value = "proName",required = false) String proName,
                                                       @RequestParam(value = "proChargeId",required = false) String proChargeId,
                                                       @RequestParam(value = "proSchoolId",required = false) String proSchoolId,
                                                       @RequestParam(value = "proSubject",required = false) String proSubject,
                                                       @RequestParam(value = "resultsTypeId",required = false) String resultsTypeId,
                                                       @RequestParam(value = "proStatusId",required = false) String proStatusId,
                                                       @RequestParam(value = "pageNo") Long pageNo,
                                                       @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(proName), Project::getProName, proName); //传入的字符串长度大于0再做拼接
        wrapper.eq(StringUtils.hasLength(proChargeId), Project::getProChargeId, proChargeId);
        wrapper.eq(StringUtils.hasLength(proSchoolId), Project::getProSchoolId, proSchoolId);
        wrapper.like(StringUtils.hasLength(proSubject),Project::getProSubject, proSubject);
        wrapper.eq(StringUtils.hasLength(resultsTypeId),Project::getResultsTypeId, resultsTypeId);
        wrapper.eq(StringUtils.hasLength(proStatusId),Project::getProStatusId, proStatusId);
        wrapper.ne(Project::getProStatusId, 1); // 不带草稿
        wrapper.orderByAsc(Project::getSubmitTime); // 根据提交时间正序排序
        Page<Project> page = new Page<>(pageNo,pageSize);
        projectService.page(page,wrapper);
        page.getRecords().forEach(index->{
            index.setSchoolDesc(projectMapper.getSchoolDescByProId(index.getProId()));
            index.setProChargeName(projectMapper.getChargeNameByProId(index.getProId()));
            index.setResultsTypeDesc(projectMapper.getResultsTypeDescByProId(index.getProId()));
            index.setStatusDesc(projectMapper.getProStatusDescByProId(index.getProId()));
        });
        Map<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("管理员项目信息列表显示")  //Swagger文档注解
    @GetMapping("/list")
    public Result<Map<String, Object>> getProList(@RequestParam(value = "proName",required = false) String proName,
                                                  @RequestParam(value = "proChargeId",required = false) String proChargeId,
                                                  @RequestParam(value = "proSchoolId",required = false) String proSchoolId,
                                                  @RequestParam(value = "proSubject",required = false) String proSubject,
                                                  @RequestParam(value = "resultsTypeId",required = false) String resultsTypeId,
                                                  @RequestParam(value = "proStatusId",required = false) String proStatusId,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(proName), Project::getProName, proName); //传入的字符串长度大于0再做拼接
        wrapper.eq(StringUtils.hasLength(proChargeId), Project::getProChargeId, proChargeId);
        wrapper.eq(StringUtils.hasLength(proSchoolId), Project::getProSchoolId, proSchoolId);
        wrapper.like(StringUtils.hasLength(proSubject),Project::getProSubject, proSubject);
        wrapper.eq(StringUtils.hasLength(resultsTypeId),Project::getResultsTypeId, resultsTypeId);
        wrapper.eq(StringUtils.hasLength(proStatusId),Project::getProStatusId, proStatusId);
        wrapper.ne(Project::getProStatusId, 1); // 不带草稿
        wrapper.orderByDesc(Project::getUpdateTime); // 根据修改时间倒序排序
        Page<Project> page = new Page<>(pageNo,pageSize);
        projectService.page(page,wrapper);
        page.getRecords().forEach(index->{
            index.setSchoolDesc(projectMapper.getSchoolDescByProId(index.getProId()));
            index.setProChargeName(projectMapper.getChargeNameByProId(index.getProId()));
            index.setResultsTypeDesc(projectMapper.getResultsTypeDescByProId(index.getProId()));
            index.setStatusDesc(projectMapper.getProStatusDescByProId(index.getProId()));
        });
        Map<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("新增项目")  //Swagger文档注解
    @PostMapping
    public Result<?> addPro(@RequestBody Project pro){ //json转Request对象
        if(StringUtils.hasLength(pro.getUpdateTimeString())){
            pro.setUpdateTimeFormString(pro.getUpdateTimeString());
        }
        if(StringUtils.hasLength(pro.getSubmitTimeString())){
            pro.setSubmitTimeFormString(pro.getSubmitTimeString());
        }else{
            pro.setSubmitTime(null);
        }
        projectMapper.insert(pro);
        return Result.success("新增项目成功");
    }

    @ApiOperation("修改项目信息")  //Swagger文档注解
    @PutMapping
    public Result<?> updatePro(@RequestBody Project pro){ //修改
        if(StringUtils.hasLength(pro.getUpdateTimeString())){
            pro.setUpdateTimeFormString(pro.getUpdateTimeString());
        }
        if(StringUtils.hasLength(pro.getSubmitTimeString())){
            pro.setSubmitTimeFormString(pro.getSubmitTimeString());
        }else{
            pro.setSubmitTime(null);
        }
        projectMapper.updateById(pro);
        return Result.success("修改项目成功");
    }

    @ApiOperation("根据项目id查询")  //Swagger文档注解
    @GetMapping("/{id}")
    public Result<Project> getProById(@PathVariable("id") Integer proId){
        Project pro = projectService.getProById(proId);
        return Result.success(pro);
    }

    @ApiOperation("根据项目id删除")  //Swagger文档注解
    @DeleteMapping("/{id}")
    public Result<Project> deleteProById(@PathVariable("id") Integer proId){
        projectMapper.deleteById(proId);
        return Result.success("删除项目成功");
    }

    @ApiOperation("导出Excel")  //Swagger文档注解
    @GetMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(value = "proChargeId",required = false) String proChargeId,
                            @RequestParam(value = "proSchoolId",required = false) String proSchoolId,
                            @RequestParam(value = "proStatusId",required = false) String proStatusId) throws IOException {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(proChargeId), Project::getProChargeId, proChargeId);
        wrapper.eq(StringUtils.hasLength(proSchoolId), Project::getProSchoolId, proSchoolId);
        wrapper.ne(StringUtils.hasLength(proStatusId),Project::getProStatusId, proStatusId); // 不带草稿
        wrapper.orderByAsc(Project::getProSchoolId); // 根据学院升序排序
        wrapper.orderByAsc(Project::getProChargeId); // 根据负责人升序排序
        wrapper.orderByDesc(Project::getUpdateTime); // 根据修改时间倒序排序
        List<Project> list = projectService.list(wrapper);
        for(Project pro:list){
            pro.setSchoolDesc(projectMapper.getSchoolDescByProId(pro.getProId()));
            pro.setProChargeName(projectMapper.getChargeNameByProId(pro.getProId()));
            pro.setResultsTypeDesc(projectMapper.getResultsTypeDescByProId(pro.getProId()));
            pro.setStatusDesc(projectMapper.getProStatusDescByProId(pro.getProId()));
        }
        ExcelUtil.writeExcel("项目表",response, list, Project.class);
    }
}
