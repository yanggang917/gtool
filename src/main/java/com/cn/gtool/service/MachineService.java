package com.cn.gtool.service;

import com.cn.gtool.bean.entity.MachineDO;
import com.cn.gtool.bean.entity.UserPayLogDO;
import com.cn.gtool.bean.vo.MachineForQueryByMachineCodeVO;

import java.util.List;

/**
 * @Auther: yg
 * @Date: 2019/12/5 17:35
 * @Description:
 */
public interface MachineService {
    MachineForQueryByMachineCodeVO queryByMachineCode(String machineCode);

    List<MachineDO> queryList(int page, int size, int userId);

    int queryListCount(int userId);

    void add(MachineDO machineDO);

    void updateEndTimeByCode(MachineDO machineDO);

    void addPayLog(UserPayLogDO userPayLogDO);
}
