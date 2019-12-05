package com.cn.gtool.service.impl;

import com.cn.gtool.bean.vo.MachineForQueryByMachineCodeVO;
import com.cn.gtool.dao.MachineDOMapper;
import com.cn.gtool.service.MachineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
