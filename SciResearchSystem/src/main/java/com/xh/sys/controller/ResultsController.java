package com.xh.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.common.utils.ExcelUtil;
import com.xh.common.vo.Result;
import com.xh.sys.entity.Project;
import com.xh.sys.entity.Results;
import com.xh.sys.mapper.ProjectMapper;
import com.xh.sys.mapper.ResultsMapper;
import com.xh.sys.mapper.UserMapper;
import com.xh.sys.service.IProjectService;
import com.xh.sys.service.IResultsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"成果接口"})  //Swagger文档注解
@RestController
@RequestMapping("/res")
public class ResultsController {
    @Autowired
    private IResultsService resultsService;

    @Resource
    private ResultsMapper resultsMapper;
    @Resource
    private UserMapper userMapper;

    @ApiOperation("科研人员成果信息列表显示")  //Swagger文档注解
    @GetMapping("/sciList")
    public Result<Map<String, Object>> getSciResultsList(@RequestParam(value = "resultsName",required = false) String resultsName,
                                                  @RequestParam(value = "resultsChargeId",required = false) String resultsChargeId,
                                                  @RequestParam(value = "resultsSchoolId",required = false) String resultsSchoolId,
                                                  @RequestParam(value = "resultsSubject",required = false) String resultsSubject,
                                                  @RequestParam(value = "typeId",required = false) String typeId,
                                                  @RequestParam(value = "resultsStatusId",required = false) String resultsStatusId,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<Results> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(resultsName), Results::getResultsName, resultsName); //传入的字符串长度大于0再做拼接
        wrapper.eq(StringUtils.hasLength(resultsChargeId), Results::getResultsChargeId, resultsChargeId);
        wrapper.eq(StringUtils.hasLength(resultsSchoolId), Results::getResultsSchoolId, resultsSchoolId);
        wrapper.like(StringUtils.hasLength(resultsSubject),Results::getResultsSubject, resultsSubject);
        wrapper.eq(StringUtils.hasLength(typeId),Results::getTypeId, typeId);
        wrapper.eq(StringUtils.hasLength(resultsStatusId),Results::getResultsStatusId, resultsStatusId);  //带草稿
        wrapper.orderByDesc(Results::getUpdateTime); // 根据修改时间倒序排序
        Page<Results> page = new Page<>(pageNo,pageSize);
        resultsService.page(page,wrapper);
        page.getRecords().forEach(index->{
            index.setSchoolDesc(resultsMapper.getSchoolDescByResultsId(index.getResultsId()));
            index.setResultsChargeName(resultsMapper.getChargeNameByResultsId(index.getResultsId()));
            index.setTypeDesc(resultsMapper.getResultsTypeDescByResultsId(index.getResultsId()));
            index.setStatusDesc(resultsMapper.getResultsStatusDescByResultsId(index.getResultsId()));
        });
        Map<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("管理员成果审核信息列表显示")  //Swagger文档注解
    @GetMapping("/auditList")
    public Result<Map<String, Object>> getResultsAuditList(@RequestParam(value = "resultsName",required = false) String resultsName,
                                                            @RequestParam(value = "resultsChargeId",required = false) String resultsChargeId,
                                                            @RequestParam(value = "resultsSchoolId",required = false) String resultsSchoolId,
                                                            @RequestParam(value = "resultsSubject",required = false) String resultsSubject,
                                                            @RequestParam(value = "typeId",required = false) String typeId,
                                                            @RequestParam(value = "resultsStatusId",required = false) String resultsStatusId,
                                                            @RequestParam(value = "pageNo") Long pageNo,
                                                            @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<Results> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(resultsName), Results::getResultsName, resultsName); //传入的字符串长度大于0再做拼接
        wrapper.eq(StringUtils.hasLength(resultsChargeId), Results::getResultsChargeId, resultsChargeId);
        wrapper.eq(StringUtils.hasLength(resultsSchoolId), Results::getResultsSchoolId, resultsSchoolId);
        wrapper.like(StringUtils.hasLength(resultsSubject),Results::getResultsSubject, resultsSubject);
        wrapper.eq(StringUtils.hasLength(typeId),Results::getTypeId, typeId);
        wrapper.eq(StringUtils.hasLength(resultsStatusId),Results::getResultsStatusId, resultsStatusId);
        wrapper.ne(Results::getResultsStatusId, 10); //不带草稿
        wrapper.orderByAsc(Results::getSubmitTime); // 根据提交时间正序排序
        Page<Results> page = new Page<>(pageNo,pageSize);
        resultsService.page(page,wrapper);
        page.getRecords().forEach(index->{
            index.setSchoolDesc(resultsMapper.getSchoolDescByResultsId(index.getResultsId()));
            index.setResultsChargeName(resultsMapper.getChargeNameByResultsId(index.getResultsId()));
            index.setTypeDesc(resultsMapper.getResultsTypeDescByResultsId(index.getResultsId()));
            index.setStatusDesc(resultsMapper.getResultsStatusDescByResultsId(index.getResultsId()));
        });
        Map<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("管理员成果信息列表显示")  //Swagger文档注解
    @GetMapping("/list")
    public Result<Map<String, Object>> getResultsList(@RequestParam(value = "resultsName",required = false) String resultsName,
                                                      @RequestParam(value = "resultsChargeId",required = false) String resultsChargeId,
                                                      @RequestParam(value = "resultsSchoolId",required = false) String resultsSchoolId,
                                                      @RequestParam(value = "resultsSubject",required = false) String resultsSubject,
                                                      @RequestParam(value = "typeId",required = false) String typeId,
                                                      @RequestParam(value = "resultsStatusId",required = false) String resultsStatusId,
                                                      @RequestParam(value = "pageNo") Long pageNo,
                                                      @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<Results> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(resultsName), Results::getResultsName, resultsName); //传入的字符串长度大于0再做拼接
        wrapper.eq(StringUtils.hasLength(resultsChargeId), Results::getResultsChargeId, resultsChargeId);
        wrapper.eq(StringUtils.hasLength(resultsSchoolId), Results::getResultsSchoolId, resultsSchoolId);
        wrapper.like(StringUtils.hasLength(resultsSubject),Results::getResultsSubject, resultsSubject);
        wrapper.eq(StringUtils.hasLength(typeId),Results::getTypeId, typeId);
        wrapper.eq(StringUtils.hasLength(resultsStatusId),Results::getResultsStatusId, resultsStatusId);
        wrapper.ne(Results::getResultsStatusId, 10); //不带草稿
        wrapper.orderByDesc(Results::getUpdateTime); // 根据修改时间倒序排序
        Page<Results> page = new Page<>(pageNo,pageSize);
        resultsService.page(page,wrapper);
        page.getRecords().forEach(index->{
            index.setSchoolDesc(resultsMapper.getSchoolDescByResultsId(index.getResultsId()));
            index.setResultsChargeName(resultsMapper.getChargeNameByResultsId(index.getResultsId()));
            index.setTypeDesc(resultsMapper.getResultsTypeDescByResultsId(index.getResultsId()));
            index.setStatusDesc(resultsMapper.getResultsStatusDescByResultsId(index.getResultsId()));
        });
        Map<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("新增成果")  //Swagger文档注解
    @PostMapping
    public Result<?> addResults(@RequestBody Results results){ //json转Request对象
        if(StringUtils.hasLength(results.getUpdateTimeString())){
            results.setUpdateTimeFormString(results.getUpdateTimeString());
        }
        if(StringUtils.hasLength(results.getSubmitTimeString())){
            results.setSubmitTimeFormString(results.getSubmitTimeString());
        }else{
            results.setSubmitTime(null);
        }
        resultsMapper.insert(results);
        return Result.success("新增成果成功");
    }

    @ApiOperation("修改成果信息")  //Swagger文档注解
    @PutMapping
    public Result<?> updateResults(@RequestBody Results results){ //修改
        if(StringUtils.hasLength(results.getUpdateTimeString())){
            results.setUpdateTimeFormString(results.getUpdateTimeString());
        }
        if(StringUtils.hasLength(results.getSubmitTimeString())){
            results.setSubmitTimeFormString(results.getSubmitTimeString());
            System.out.println(2);
        }else{
            results.setSubmitTime(null);
            System.out.println(3);
        }
        resultsMapper.updateById(results);
        return Result.success("修改成果成功");
    }

    @ApiOperation("根据成果id查询")  //Swagger文档注解
    @GetMapping("/{id}")
    public Result<Results> getResultsById(@PathVariable("id") Integer resultsId){
        Results results = resultsService.getResultsById(resultsId);
        return Result.success(results);
    }

    @ApiOperation("根据成果id删除")  //Swagger文档注解
    @DeleteMapping("/{id}")
    public Result<Project> deleteResultsById(@PathVariable("id") Integer resultsId){
        resultsMapper.deleteById(resultsId);
        return Result.success("删除成果成功");
    }

    @ApiOperation("导出Excel")  //Swagger文档注解
    @GetMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(value = "resultsChargeId",required = false) String resultsChargeId,
                            @RequestParam(value = "resultsSchoolId",required = false) String resultsSchoolId,
                            @RequestParam(value = "resultsStatusId",required = false) String resultsStatusId) throws IOException {
        LambdaQueryWrapper<Results> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(resultsChargeId), Results::getResultsChargeId, resultsChargeId);
        wrapper.eq(StringUtils.hasLength(resultsSchoolId), Results::getResultsSchoolId, resultsSchoolId);
        wrapper.ne(StringUtils.hasLength(resultsStatusId),Results::getResultsStatusId, resultsStatusId); // 不带草稿
        wrapper.orderByAsc(Results::getResultsSchoolId); // 根据学院升序排序
        wrapper.orderByAsc(Results::getResultsChargeId); // 根据负责人升序排序
        wrapper.orderByDesc(Results::getUpdateTime); // 根据修改时间倒序排序
        List<Results> list = resultsService.list(wrapper);
        for(Results res:list){
            res.setSchoolDesc(resultsMapper.getSchoolDescByResultsId(res.getResultsId()));
            res.setResultsChargeName(resultsMapper.getChargeNameByResultsId(res.getResultsId()));
            res.setTypeDesc(resultsMapper.getResultsTypeDescByResultsId(res.getResultsId()));
            res.setStatusDesc(resultsMapper.getResultsStatusDescByResultsId(res.getResultsId()));
        }
        ExcelUtil.writeExcel("成果表",response, list, Results.class);
    }
}
