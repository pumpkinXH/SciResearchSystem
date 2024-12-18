package com.xh.sys.service;

import com.xh.sys.entity.Results;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
public interface IResultsService extends IService<Results> {

    Results getResultsById(Integer resultsId);
}
