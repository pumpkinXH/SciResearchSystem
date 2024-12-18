package com.xh.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.TimeUtil;
import com.xh.common.vo.Result;
import com.xh.sys.entity.Notice;
import com.xh.sys.entity.Project;
import com.xh.sys.entity.User;
import com.xh.sys.mapper.NoticeMapper;
import com.xh.sys.service.INoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Api(tags = {"消息通知接口"})  //Swagger文档注解
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    INoticeService noticeService;

    @Resource
    NoticeMapper noticeMapper;

    @ApiOperation(value = "通知列表")
    @GetMapping("/list")
    public Result<Map<String, Object>> getNoticeList(@RequestParam(value = "noticeTitle",required = false) String noticeTitle,
                                                     @RequestParam(value = "schoolId",required = false) String schoolId,
                                                     @RequestParam(value = "pageNo") Long pageNo,
                                                     @RequestParam(value = "pageSize") Long pageSize
                                                     ){
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(noticeTitle),Notice::getNoticeTitle, noticeTitle); //模糊查询，传入的字符串长度大于0再做拼接
        wrapper.eq(StringUtils.hasLength(schoolId),Notice::getSchoolId, schoolId);
        wrapper.orderByDesc(Notice::getNoticeTime); // 倒序排序
        Page<Notice> page = new Page<>(pageNo,pageSize);
        noticeService.page(page,wrapper);
        page.getRecords().forEach(index -> {
            index.setSchoolDesc(noticeMapper.getSchoolDescByNoticeId(index.getNoticeId()));  //学院名
        });
        Map<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("新增通知")  //Swagger文档注解
    @PostMapping
    public Result<?> addNotice(@RequestBody Notice notice){
        notice.setNoticeTimeFormString(notice.getNoticeTimeString());
        noticeMapper.insert(notice);
        return Result.success("新增通知成功");
    }

    @ApiOperation("修改通知信息")  //Swagger文档注解
    @PutMapping
    public Result<?> updateNotice(@RequestBody Notice notice){ //修改
        notice.setNoticeTimeFormString(notice.getNoticeTimeString());
        noticeMapper.updateById(notice);
        return Result.success("修改通知成功");
    }

    @ApiOperation("根据通知id查询")  //Swagger文档注解
    @GetMapping("/{id}")
    public Result<Notice> getNoticeById(@PathVariable("id") Integer noticeId){
        Notice notice = noticeService.getNoticeById(noticeId);
        return Result.success(notice);
    }

    @ApiOperation("根据通知id删除")  //Swagger文档注解
    @DeleteMapping("/{id}")
    public Result<Project> deleteNoticeById(@PathVariable("id") Integer noticeId){
        noticeMapper.deleteById(noticeId);
        return Result.success("删除通知成功");
    }
}
