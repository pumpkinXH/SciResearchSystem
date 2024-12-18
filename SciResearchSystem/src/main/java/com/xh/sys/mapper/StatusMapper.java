package com.xh.sys.mapper;

import com.xh.sys.entity.Status;
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
public interface StatusMapper extends BaseMapper<Status> {
    @Select("select * from status limit 9")
    public List<Status> getAllProStatus();

    @Select("select * from status limit 9,4")
    public List<Status> getAllResStatus();

    @Select("select * from status limit 1,8")
    List<Status> getProStatusExceptDraft();

    @Select("select status_desc from status limit 1,8")
    List<String> getProStatusDescExceptDraft();

    @Select("select * from status limit 10,3")
    List<Status> getResStatusExceptDraft();

    @Select("select status_desc from status limit 10,3")
    List<String> getResStatusDescExceptDraft();
}
