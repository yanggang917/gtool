package com.cn.gtool.dao;

import com.cn.gtool.bean.entity.MachineDO;
import com.cn.gtool.bean.entity.UserPayLogDO;
import com.cn.gtool.bean.vo.MachineForQueryByMachineCodeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Auther: yg
 * @Date: 2019/12/5 17:35
 * @Description:
 */
@Mapper
public interface MachineDOMapper {


    MachineForQueryByMachineCodeVO queryByMachineCode(String machineCode);

    List<MachineDO> queryList(int offset, int size, int userId);

    int queryListCount(int userId);

    void add(MachineDO machineDO);

    void updateEndTimeByCode(MachineDO machineDO);

    void addPayLog(UserPayLogDO userPayLogDO);
}
