package com.xh.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.common.vo.Result;
import com.xh.sys.entity.School;
import com.xh.sys.mapper.SchoolMapper;
import com.xh.sys.service.ISchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = {"学院接口"})  //Swagger文档注解
@RestController
@RequestMapping("/school")
public class SchoolController {
    @Autowired
    ISchoolService schoolService;

    @Resource
    SchoolMapper schoolMapper;

    @ApiOperation("返回所有部门")  //Swagger文档注解
    @GetMapping("/all")
    public Result<List<School>> getAllSchool(){
        List<School> schoolList = schoolService.list();
        return Result.success(schoolList);
    }

    @ApiOperation("返回所有二级学院")  //Swagger文档注解
    @GetMapping("/allExCo")
    public Result<List<School>> getAllSchoolExceptCollege(){
        List<School> schoolList = schoolMapper.getAllSchoolExceptCollege();
        return Result.success(schoolList);
    }

    @ApiOperation("返回学校")  //Swagger文档注解
    @GetMapping("/college")
    public Result<List<School>> getCollege(){
        List<School> schoolList = schoolMapper.getCollege();
        return Result.success(schoolList);
    }

    @ApiOperation("学院信息列表显示")  //Swagger文档注解
    @GetMapping("/list")
    public Result<Map<String, Object>> getSchoolList(@RequestParam(value = "schoolName",required = false) String schoolName,
                                                     @RequestParam(value = "schoolDesc",required = false) String schoolDesc,
                                                     @RequestParam(value = "pageNo") Long pageNo,
                                                     @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<School> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(schoolName), School::getSchoolName, schoolName); //传入的字符串长度大于0再做拼接
        wrapper.like(StringUtils.hasLength(schoolDesc), School::getSchoolDesc, schoolDesc);
        wrapper.ne(School::getSchoolId, 1);
        Page<School> page = new Page<>(pageNo,pageSize);
        schoolService.page(page,wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("新增学院")  //Swagger文档注解
    @PostMapping
    public Result<?> addSchool(@RequestBody School school){
        schoolMapper.insert(school);
        return Result.success("新增学院信息成功");
    }

    @ApiOperation("修改学院信息")  //Swagger文档注解
    @PutMapping
    public Result<?> updateSchool(@RequestBody School school){ //修改
        schoolMapper.updateById(school);
        return Result.success("修改学院信息成功");
    }

    @ApiOperation("根据学院id查询")  //Swagger文档注解
    @GetMapping("/{id}")
    public Result<School> getSchoolById(@PathVariable("id") Integer schoolId){
        School school = schoolMapper.selectById(schoolId);
        return Result.success(school);
    }

    @ApiOperation("根据学院id删除")  //Swagger文档注解
    @DeleteMapping("/{id}")
    public Result<School> deleteProById(@PathVariable("id") Integer schoolId){
        schoolMapper.deleteById(schoolId);
        schoolMapper.resetAutoIncrement(); // 重置学院id的自增值
        return Result.success("删除学院信息成功");
    }
}
