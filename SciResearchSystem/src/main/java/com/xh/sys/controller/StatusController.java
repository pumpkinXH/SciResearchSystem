package com.xh.sys.controller;

import com.xh.common.vo.Result;
import com.xh.sys.entity.School;
import com.xh.sys.entity.Status;
import com.xh.sys.entity.User;
import com.xh.sys.mapper.StatusMapper;
import com.xh.sys.service.ISchoolService;
import com.xh.sys.service.IStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Api(tags = {"状态接口"})  //Swagger文档注解
@RestController
@RequestMapping("/status")
public class StatusController {
    @Resource
    StatusMapper statusMapper;

    @ApiOperation("返回所有项目状态")  //Swagger文档注解
    @GetMapping("/allPro")
    public Result<List<Status>> getAllProStatus(){
        List<Status> allProStatus = statusMapper.getAllProStatus();
        return Result.success(allProStatus);
    }

    @ApiOperation("返回除草稿外的项目状态")  //Swagger文档注解
    @GetMapping("/proExceptDraft")
    public Result<List<Status>> getProStatusExceptDraft(){
        List<Status> proStatusExceptDraft = statusMapper.getProStatusExceptDraft();
        return Result.success(proStatusExceptDraft);
    }

    @ApiOperation("返回所有成果状态")  //Swagger文档注解
    @GetMapping("/allRes")
    public Result<List<Status>> getAllResStatus(){
        List<Status> allResStatus = statusMapper.getAllResStatus();
        return Result.success(allResStatus);
    }

    @ApiOperation("返回除草稿外的成果状态")  //Swagger文档注解
    @GetMapping("/resExceptDraft")
    public Result<List<Status>> getResStatusExceptDraft(){
        List<Status> resStatusExceptDraft = statusMapper.getResStatusExceptDraft();
        return Result.success(resStatusExceptDraft);
    }
}
