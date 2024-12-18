package com.xh.sys.mapper;

import com.xh.sys.entity.ResultsType;
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
public interface ResultsTypeMapper extends BaseMapper<ResultsType> {
    @Select("select results_type_desc from results_type")
    public List<String> getResultsTypeDesc();
}
