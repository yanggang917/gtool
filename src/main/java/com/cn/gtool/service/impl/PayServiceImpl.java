package com.cn.gtool.service.impl;

import com.cn.gtool.bean.entity.PayDO;
import com.cn.gtool.dao.PayDOMapper;
import com.cn.gtool.service.PayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PayServiceImpl implements PayService {
    @Resource
    private PayDOMapper payDOMapper;


    @Override
    public PayDO queryByPayCode(String payCode) {
        return this.payDOMapper.queryByPayCode(payCode);
    }

    @Override
    public void add(PayDO payDO) {
        this.payDOMapper.add(payDO);
    }

    @Override
    public List<PayDO> queryList(int page, int size, int userId) {
        return this.payDOMapper.queryList(page*size, size, userId);
    }

    @Override
    public void update(PayDO payDO) {
        this.payDOMapper.update(payDO);
    }

    @Override
    public int queryListCount(int userId) {
        return this.payDOMapper.queryListCount(userId);
    }

    @Override
    public List<PayDO> queryListForUsable(int userId) {
        return this.payDOMapper.queryListForUsable(userId);
    }
}
