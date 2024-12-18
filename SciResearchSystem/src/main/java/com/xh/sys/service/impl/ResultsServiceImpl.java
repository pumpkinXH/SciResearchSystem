package com.xh.sys.service.impl;

import com.xh.sys.entity.Project;
import com.xh.sys.entity.Results;
import com.xh.sys.mapper.ProjectMapper;
import com.xh.sys.mapper.ResultsMapper;
import com.xh.sys.service.IResultsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Service
public class ResultsServiceImpl extends ServiceImpl<ResultsMapper, Results> implements IResultsService {
    @Resource
    ResultsMapper resultsMapper;

    @Override
    public Results getResultsById(Integer resultsId) {
        Results results = this.baseMapper.selectById(resultsId);
        results.setSchoolDesc(resultsMapper.getSchoolDescByResultsId(resultsId));
        results.setResultsChargeName(resultsMapper.getChargeNameByResultsId(resultsId));
        results.setStatusDesc(resultsMapper.getResultsStatusDescByResultsId(resultsId));
        results.setTypeDesc(resultsMapper.getResultsTypeDescByResultsId(resultsId));
        return results;
    }
}
