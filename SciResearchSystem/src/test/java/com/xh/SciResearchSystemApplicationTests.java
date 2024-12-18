package com.xh;

import com.xh.common.vo.Result;
import com.xh.sys.entity.Project;
import com.xh.sys.entity.User;
import com.xh.sys.mapper.SchoolMapper;
import com.xh.sys.mapper.UserMapper;
import com.xh.sys.service.IProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class SciResearchSystemApplicationTests {

    @Autowired
    private IProjectService projectService;

    @Resource
    private SchoolMapper schoolMapper;

    @Test
    public Result<Map<String,Object>> proVisBySchool(){
        List<String> schoolDescList = schoolMapper.getSchoolDescExceptCollege();
        List<Project> proList = getProListExceptDraft();
        Integer[] cnt = new Integer[schoolDescList.size()];
        for(Project pro : proList){
            Integer proSchoolId = pro.getProSchoolId();
            switch (proSchoolId){
                case 2: cnt[0]+=1; break;  //计算机学院
                case 3: cnt[1]+=1; break;  //电气学院
                default: break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        ArrayList<Integer> cntList = new ArrayList<>(cnt.length);
        Collections.addAll(cntList,cnt);
        map.put("x", schoolDescList);
        map.put("y", cntList);
        System.out.println(map);
        return Result.success(map);
    }

    private List<Project> getProListExceptDraft() {
        List<Project> list = projectService.list();
        list.removeIf(pro -> pro.getProStatusId() == 1);  //删去草稿数据
        return list;
    }
}
