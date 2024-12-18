package com.xh.sys.service;

import com.xh.sys.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
public interface IProjectService extends IService<Project> {

    void addPro(Project pro);

    void updatePro(Project pro);

    Project getProById(Integer proId);

    void deleteProById(Integer proId);
}
