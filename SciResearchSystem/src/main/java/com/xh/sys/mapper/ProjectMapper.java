package com.xh.sys.mapper;

import com.xh.sys.entity.Project;
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
public interface ProjectMapper extends BaseMapper<Project> {
    public String getSchoolDescByProId(Integer proId);

    public String getChargeNameByProId(Integer proId);

    public String getResultsTypeDescByProId(Integer proId);

    public String getProStatusDescByProId(Integer proId);

    @Select("select * from project where pro_status_id = 9")
    public List<Project> getPassedProList();

    @Select("SELECT * FROM `project` \n" +
            "WHERE pro_status_id >=4 and pro_status_id <=9")
    public List<Project> getDeclarationProList();
}
