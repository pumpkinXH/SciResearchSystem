package com.xh.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xh.sys.entity.Project;
import com.xh.sys.entity.User;
import com.xh.sys.mapper.ProjectMapper;
import com.xh.sys.mapper.UserMapper;
import com.xh.sys.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {
    @Resource
    ProjectMapper projectMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public void addPro(Project pro) {
    }

    @Override
    public void updatePro(Project pro) {
        //修改负责人
//        String proChargeName = pro.getProChargeName();
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username",proChargeName);
//        List<User> user = userMapper.selectList(queryWrapper);
//        pro.setProChargeId(user.get(0).getUserId());
//        projectMapper.updateById(pro);
    }

    @Override
    public Project getProById(Integer proId) {
        Project pro = this.baseMapper.selectById(proId);
        pro.setSchoolDesc(projectMapper.getSchoolDescByProId(proId));
        pro.setProChargeName(projectMapper.getChargeNameByProId(proId));
        pro.setStatusDesc(projectMapper.getProStatusDescByProId(proId));
        pro.setResultsTypeDesc(projectMapper.getResultsTypeDescByProId(proId));
        return pro;
    }

    @Override
    public void deleteProById(Integer proId) {

    }
}
