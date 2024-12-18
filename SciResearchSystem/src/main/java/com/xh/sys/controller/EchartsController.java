package com.xh.sys.controller;

import cn.hutool.core.collection.CollUtil;
import com.xh.common.vo.Result;
import com.xh.sys.entity.*;
import com.xh.sys.mapper.*;
import com.xh.sys.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = {"Echarts接口"})  //Swagger文档注解
@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private IProjectService projectService;

    @Autowired
    private IResultsService resultsService;

    @Autowired
    private IUserService userService;

    @Resource
    private ResultsTypeMapper resultsTypeMapper;

    @Resource
    private StatusMapper statusMapper;

    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ResultsMapper resultsMapper;

    @Resource
    private RoleMapper roleMapper;

    @ApiOperation("已成功申报项目根据学院可视化")
    @GetMapping("/deProVisBySchool")
    public Result<Map<String,Object>> deProVisBySchool(){
        List<String> schoolDescList = schoolMapper.getSchoolDescExceptCollege();
        List<Project> proList = projectMapper.getDeclarationProList();
        int[] cnt = new int[schoolDescList.size()];
        for(Project pro : proList){
            Integer proSchoolId = pro.getProSchoolId();
            switch (proSchoolId){
                case 2: cnt[0]+=1; break;  //计算机学院
                case 3: cnt[1]+=1; break;  //电气学院
                case 4: cnt[2]+=1; break;  //材料学院
                case 5: cnt[3]+=1; break;  //化工学院
                case 6: cnt[4]+=1; break;  //机械学院
                case 7: cnt[5]+=1; break;  //汽车学院
                case 8: cnt[6]+=1; break;  //
                case 9: cnt[7]+=1; break;  //
                case 10: cnt[8]+=1; break;  //
                case 11: cnt[9]+=1; break;  //
                case 12: cnt[10]+=1; break;  //
                case 13: cnt[11]+=1; break;  //
                default: break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        List<Integer> cntList = Arrays.stream(cnt).boxed().collect(Collectors.toList());  //cnt数组转list
        map.put("x", schoolDescList);
        map.put("y", cntList);
        return Result.success(map);
    }

    @ApiOperation("已结题项目根据学院可视化")
    @GetMapping("/conProVisBySchool")
    public Result<Map<String,Object>> conProVisBySchool(){
        List<String> schoolDescList = schoolMapper.getSchoolDescExceptCollege();
        List<Project> proList = projectMapper.getPassedProList();
        int[] cnt = new int[schoolDescList.size()];
        for(Project pro : proList){
            Integer proSchoolId = pro.getProSchoolId();
            switch (proSchoolId){
                case 2: cnt[0]+=1; break;  //计算机学院
                case 3: cnt[1]+=1; break;  //电气学院
                case 4: cnt[2]+=1; break;  //材料学院
                case 5: cnt[3]+=1; break;  //化工学院
                case 6: cnt[4]+=1; break;  //机械学院
                case 7: cnt[5]+=1; break;  //汽车学院
                case 8: cnt[6]+=1; break;  //
                case 9: cnt[7]+=1; break;  //
                case 10: cnt[8]+=1; break;  //
                case 11: cnt[9]+=1; break;  //
                case 12: cnt[10]+=1; break;  //
                case 13: cnt[11]+=1; break;  //
                default: break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        List<Integer> cntList = Arrays.stream(cnt).boxed().collect(Collectors.toList());  //cnt数组转list
        map.put("x", schoolDescList);
        map.put("y", cntList);
        return Result.success(map);
    }

    @ApiOperation("项目根据成果类别可视化")
    @GetMapping("/proVisByResType")
    public Result<List<Map<String, Object>>> proVisByResType(){
        List<Project> proList = getProListExceptDraft();
        List<String> typeDesc = resultsTypeMapper.getResultsTypeDesc();
        int[] cnt = new int [typeDesc.size()];
        for(Project pro : proList){
            Integer typeId = pro.getResultsTypeId();
            switch (typeId){
                case 1: cnt[0]+=1; break;  //论文
                case 2: cnt[1]+=1; break;  //专利
                case 3: cnt[2]+=1; break;  //软著
                case 4: cnt[3]+=1; break;  //获奖
                case 5: cnt[4]+=1; break;  //学术会议
                case 6: cnt[5]+=1; break;  //教学成果
                default: break;
            }
        }
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i <typeDesc.size() ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",typeDesc.get(i));
            map.put("value",cnt[i]);
            data.add(map);
        }
        return Result.success(data);
    }

    @ApiOperation("项目根据状态类别可视化")
    @GetMapping("/proVisByStatus")
    public Result<List<Map<String, Object>>> proVisByStatus(){
        List<Project> proList = getProListExceptDraft();
        List<String> statusList = statusMapper.getProStatusDescExceptDraft();
        int[] cnt = new int [statusList.size()];
        for(Project pro : proList){
            Integer statusId = pro.getProStatusId();
            switch (statusId){
                case 2: cnt[0]+=1; break;  //申报中
                case 3: cnt[1]+=1; break;  //院级申报通过
                case 4: cnt[2]+=1; break;  //校级申报通过
                case 5: cnt[3]+=1; break;  //中期检查申报中
                case 6: cnt[4]+=1; break;  //中期检查通过
                case 7: cnt[5]+=1; break;  //结题申报中
                case 8: cnt[6]+=1; break;  //院级结题通过
                case 9: cnt[7]+=1; break;  //校级结题通过
                default: break;
            }
        }
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i <statusList.size() ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",statusList.get(i));
            map.put("value",cnt[i]);
            data.add(map);
        }
        return Result.success(data);
    }

    private List<Project> getProListExceptDraft() {
        List<Project> list = projectService.list();
        list.removeIf(pro -> pro.getProStatusId() == 1);  //删去草稿数据
        return list;
    }

    private List<Results> getResListExceptDraft() {
        List<Results> list = resultsService.list();
        list.removeIf(res -> res.getResultsStatusId() == 10);  //删去草稿数据
        return list;
    }

    @ApiOperation("成果类别可视化")
    @GetMapping("/resVisByType")
    public Result<List<Map<String, Object>>> resVisByType(){
        List<Results> resultsList = getResListExceptDraft();
        List<String> typeDesc = resultsTypeMapper.getResultsTypeDesc();
        int[] cnt = new int [typeDesc.size()];
        for(Results res : resultsList){
            Integer typeId = res.getTypeId();
            switch (typeId){
                case 1: cnt[0]+=1; break;  //论文
                case 2: cnt[1]+=1; break;  //专利
                case 3: cnt[2]+=1; break;  //软著
                case 4: cnt[3]+=1; break;  //获奖
                case 5: cnt[4]+=1; break;  //学术会议
                case 6: cnt[5]+=1; break;  //教学成果
                default: break;
            }
        }
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i <typeDesc.size() ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",typeDesc.get(i));
            map.put("value",cnt[i]);
            data.add(map);
        }
        return Result.success(data);
    }

    @ApiOperation("成果根据状态类别可视化")
    @GetMapping("/resVisByStatus")
    public Result<List<Map<String, Object>>> resVisByStatus(){
        List<Results> resultsList = getResListExceptDraft();
        List<String> statusList = statusMapper.getResStatusDescExceptDraft();
        int[] cnt = new int [statusList.size()];
        for(Results res : resultsList){
            Integer statusId = res.getResultsStatusId();
            switch (statusId){
                case 11: cnt[0]+=1; break;  //成果鉴定中
                case 12: cnt[1]+=1; break;  //院级成果鉴定通过
                case 13: cnt[2]+=1; break;  //校级成果鉴定通过
                default: break;
            }
        }
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i <statusList.size() ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",statusList.get(i));
            map.put("value",cnt[i]);
            data.add(map);
        }
        return Result.success(data);
    }

    @ApiOperation("已提交成果根据学院可视化")
    @GetMapping("/deResVisBySchool")
    public Result<Map<String,Object>> deResVisBySchool(){
        List<String> schoolDescList = schoolMapper.getSchoolDescExceptCollege();
        List<Results> resultsList = getResListExceptDraft();
        int[] cnt = new int[schoolDescList.size()];
        for(Results res : resultsList){
            Integer resultsSchoolId = res.getResultsSchoolId();
            switch (resultsSchoolId){
                case 2: cnt[0]+=1; break;  //计算机学院
                case 3: cnt[1]+=1; break;  //电气学院
                case 4: cnt[2]+=1; break;  //材料学院
                case 5: cnt[3]+=1; break;  //化工学院
                case 6: cnt[4]+=1; break;  //机械学院
                case 7: cnt[5]+=1; break;  //汽车学院
                case 8: cnt[6]+=1; break;  //
                case 9: cnt[7]+=1; break;  //
                case 10: cnt[8]+=1; break;  //
                case 11: cnt[9]+=1; break;  //
                case 12: cnt[10]+=1; break;  //
                case 13: cnt[11]+=1; break;  //
                default: break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        List<Integer> cntList = Arrays.stream(cnt).boxed().collect(Collectors.toList());  //cnt数组转list
        map.put("x", schoolDescList);
        map.put("y", cntList);
        return Result.success(map);
    }

    @ApiOperation("已通过成果根据学院可视化")
    @GetMapping("/passedResVisBySchool")
    public Result<Map<String,Object>> passedResVisBySchool(){
        List<String> schoolDescList = schoolMapper.getSchoolDescExceptCollege();
        List<Results> resultsList = resultsMapper.getPassedResultsList();
        int[] cnt = new int[schoolDescList.size()];
        for(Results res : resultsList){
            Integer resultsSchoolId = res.getResultsSchoolId();
            switch (resultsSchoolId){
                case 2: cnt[0]+=1; break;  //计算机学院
                case 3: cnt[1]+=1; break;  //电气学院
                case 4: cnt[2]+=1; break;  //材料学院
                case 5: cnt[3]+=1; break;  //化工学院
                case 6: cnt[4]+=1; break;  //机械学院
                case 7: cnt[5]+=1; break;  //汽车学院
                case 8: cnt[6]+=1; break;  //
                case 9: cnt[7]+=1; break;  //
                case 10: cnt[8]+=1; break;  //
                case 11: cnt[9]+=1; break;  //
                case 12: cnt[10]+=1; break;  //
                case 13: cnt[11]+=1; break;  //
                default: break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        List<Integer> cntList = Arrays.stream(cnt).boxed().collect(Collectors.toList());  //cnt数组转list
        map.put("x", schoolDescList);
        map.put("y", cntList);
        return Result.success(map);
    }

    @ApiOperation("用户根据学院可视化")
    @GetMapping("/userVisBySchool")
    public Result<List<Map<String, Object>>> userVisBySchool(){
        List<String> schoolDescList = schoolMapper.getSchoolDesc();
        List<User> userList = userService.list();
        int[] cnt = new int[schoolDescList.size()];
        for(User user : userList){
            Integer userSchoolId = user.getUserSchoolId();
            switch (userSchoolId){
                case 1: cnt[0]+=1; break;  //学校
                case 2: cnt[1]+=1; break;  //计算机学院
                case 3: cnt[2]+=1; break;  //电气学院
                case 4: cnt[3]+=1; break;  //材料学院
                case 5: cnt[4]+=1; break;  //化工学院
                case 6: cnt[5]+=1; break;  //机械学院
                case 7: cnt[6]+=1; break;  //汽车学院
                case 8: cnt[6]+=1; break;  //
                case 9: cnt[7]+=1; break;  //
                case 10: cnt[8]+=1; break;  //
                case 11: cnt[9]+=1; break;  //
                case 12: cnt[10]+=1; break;  //
                case 13: cnt[11]+=1; break;  //
                default: break;
            }
        }
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i <schoolDescList.size() ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",schoolDescList.get(i));
            map.put("value",cnt[i]);
            data.add(map);
        }
        return Result.success(data);
    }

    @ApiOperation("用户根据角色类别可视化")
    @GetMapping("/userVisByRole")
    public Result<Map<String,Object>> userVisByRole(){
        List<String> roleDescList = roleMapper.getRoleDesc();
        List<User> userList = userService.list();
        int[] cnt = new int [roleDescList.size()];
        for(User user : userList){
            Integer roleId = user.getUserRoleId();
            switch (roleId){
                case 1: cnt[0]+=1; break;  //系统管理员
                case 2: cnt[1]+=1; break;  //校级管理员
                case 3: cnt[2]+=1; break;  //院级管理员
                case 4: cnt[3]+=1; break;  //科研人员
                default: break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        List<Integer> cntList = Arrays.stream(cnt).boxed().collect(Collectors.toList());  //cnt数组转list
        map.put("x", roleDescList);
        map.put("y", cntList);
        return Result.success(map);
    }
}
