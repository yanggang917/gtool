package com.cn.gtool.service;

import com.cn.gtool.bean.dto.AddMachineDTO;
import com.cn.gtool.bean.entity.PayDO;

import java.util.List;

public interface PayService {


    PayDO queryByPayCode(String payCode);

    void add(PayDO payDO);

    List<PayDO> queryList(int page, int size, int userId);

    void update(PayDO payDO);

    int queryListCount(int userId);

    List<PayDO> queryListForUsable(int userId);

    PayDO queryByPayCodeAndUserId(AddMachineDTO addMachineDTO);
}
