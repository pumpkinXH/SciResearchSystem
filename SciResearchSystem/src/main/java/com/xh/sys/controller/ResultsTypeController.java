package com.xh.sys.controller;

import com.xh.common.vo.Result;
import com.xh.sys.entity.ResultsType;
import com.xh.sys.entity.School;
import com.xh.sys.service.IResultsTypeService;
import com.xh.sys.service.ISchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Api(tags = {"成果类别接口"})  //Swagger文档注解
@RestController
@RequestMapping("/resType")
public class ResultsTypeController {
    @Autowired
    IResultsTypeService resultsTypeService;

    @ApiOperation("返回所有成果类别")  //Swagger文档注解
    @GetMapping("/all")
    public Result<List<ResultsType>> getAllResultsType(){
        List<ResultsType> resultsTypeList = resultsTypeService.list();
        return Result.success(resultsTypeList);
    }
}
