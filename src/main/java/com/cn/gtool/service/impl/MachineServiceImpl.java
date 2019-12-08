package com.cn.gtool.service.impl;

import com.cn.gtool.bean.entity.MachineDO;
import com.cn.gtool.bean.entity.UserPayLogDO;
import com.cn.gtool.bean.vo.MachineForQueryByMachineCodeVO;
import com.cn.gtool.dao.MachineDOMapper;
import com.cn.gtool.service.MachineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: yg
 * @Date: 2019/12/5 17:35
 * @Description:
 */
@Service
public class MachineServiceImpl implements MachineService {
    @Resource
    private MachineDOMapper machineDOMapper;


    @Override
    public MachineForQueryByMachineCodeVO queryByMachineCode(String machineCode) {
        return this.machineDOMapper.queryByMachineCode(machineCode);
    }

    @Override
    public List<MachineDO> queryList(int page, int size, int userId) {
        return machineDOMapper.queryList(page*size, size, userId);
    }

    @Override
    public int queryListCount(int userId) {
        return machineDOMapper.queryListCount(userId);
    }

    @Override
    public void add(MachineDO machineDO) {
        machineDOMapper.add(machineDO);
    }

    @Override
    public void updateEndTimeByCode(MachineDO machineDO) {
        machineDOMapper.updateEndTimeByCode(machineDO);
    }

    @Override
    public void addPayLog(UserPayLogDO userPayLogDO) {
        machineDOMapper.addPayLog(userPayLogDO);
    }
}
