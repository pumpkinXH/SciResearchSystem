package com.xh.sys.service.impl;

import com.xh.sys.entity.Notice;
import com.xh.sys.entity.Project;
import com.xh.sys.mapper.NoticeMapper;
import com.xh.sys.service.INoticeService;
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
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Resource
    NoticeMapper noticeMapper;

    @Override
    public Notice getNoticeById(Integer noticeId) {
        Notice notice = this.baseMapper.selectById(noticeId);
        notice.setSchoolDesc(noticeMapper.getSchoolDescByNoticeId(noticeId));
        return notice;
    }
}
