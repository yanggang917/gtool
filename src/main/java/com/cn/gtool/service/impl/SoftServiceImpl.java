package com.cn.gtool.service.impl;

import com.cn.gtool.bean.entity.SoftDO;
import com.cn.gtool.dao.SoftDOMapper;
import com.cn.gtool.service.SoftService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SoftServiceImpl implements SoftService {
    @Resource
    private SoftDOMapper softDOMapper;

    @Override
    public SoftDO getNewSoftVersion() {
        return softDOMapper.getNewSoftVersion();
    }

    @Override
    public void updateDownNum(int id) {
        this.softDOMapper.updateDownNum(id);
    }
}
