package com.xh.sys.mapper;

import com.xh.sys.entity.Results;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
public interface ResultsMapper extends BaseMapper<Results> {
    public String getSchoolDescByResultsId(Integer resultsId);

    public String getChargeNameByResultsId(Integer resultsId);

    public String getResultsTypeDescByResultsId(Integer resultsId);

    public String getResultsStatusDescByResultsId(Integer resultsId);

    @Select("select * from results where results_status_id = 13")
    public List<Results> getPassedResultsList();
}
