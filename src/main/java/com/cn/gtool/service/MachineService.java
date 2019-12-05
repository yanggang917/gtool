package com.cn.gtool.service;

import com.cn.gtool.bean.vo.MachineForQueryByMachineCodeVO;

/**
 * @Auther: yg
 * @Date: 2019/12/5 17:35
 * @Description:
 */
public interface MachineService {
    MachineForQueryByMachineCodeVO queryByMachineCode(String machineCode);
}
