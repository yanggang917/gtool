package com.cn.gtool.dao;

import com.cn.gtool.bean.vo.MachineForQueryByMachineCodeVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: yg
 * @Date: 2019/12/5 17:35
 * @Description:
 */
@Mapper
public interface MachineDOMapper {


    MachineForQueryByMachineCodeVO queryByMachineCode(String machineCode);
}
