package com.xh.sys.mapper;

import com.xh.sys.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
public interface NoticeMapper extends BaseMapper<Notice> {
    public String getSchoolDescByNoticeId(Integer noticeId);
}
