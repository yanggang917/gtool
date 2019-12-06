package com.cn.gtool.service.impl;

import com.cn.gtool.dao.NoticeDOMapper;
import com.cn.gtool.service.NoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: yg
 * @Date: 2019/12/6 18:40
 * @Description:
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Resource
    private NoticeDOMapper noticeDOMapper;

    @Override
    public String getNews() {
        return this.noticeDOMapper.getNews();
    }
}
