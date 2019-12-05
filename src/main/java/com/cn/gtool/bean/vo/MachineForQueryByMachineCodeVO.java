package com.cn.gtool.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: yg
 * @Date: 2019/12/5 17:40
 * @Description:
 */
@Data
public class MachineForQueryByMachineCodeVO {

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy/MM/dd HH:mm:ss")
    private Date endTime;
    private String machineCode;
    private int isExpired;//0已过期，1未过期

}
