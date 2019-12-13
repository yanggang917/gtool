package com.cn.gtool.service;

import com.cn.gtool.bean.entity.SoftDO;

public interface SoftService {
    SoftDO getNewSoftVersion();

    void updateDownNum(int id);
}
