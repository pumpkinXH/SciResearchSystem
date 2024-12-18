package com.xh.sys.mapper;

import com.xh.sys.entity.School;
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
public interface SchoolMapper extends BaseMapper<School> {
    @Select("select school_desc from school where school_id != 1")
    List<String> getSchoolDescExceptCollege();

    @Select("select school_desc from school")
    List<String> getSchoolDesc();

    @Select("select * from school where school_id != 1")
    List<School> getAllSchoolExceptCollege();

    @Select("select * from school where school_id = 1")
    List<School> getCollege();

    // 新增一个方法，用于重置 AUTO_INCREMENT
    void resetAutoIncrement();
}
