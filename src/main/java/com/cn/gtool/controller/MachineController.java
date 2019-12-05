package com.cn.gtool.controller;

import com.cn.gtool.bean.vo.MachineForQueryByMachineCodeVO;
import com.cn.gtool.service.MachineService;
import com.cn.gtool.util.ResJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: yg
 * @Date: 2019/12/5 17:33
 * @Description:
 */
@Controller
@RequestMapping(value = "/machine")
public class MachineController {
    @Resource
    private MachineService machineService;

    @RequestMapping(value = "/query-by-machineCode", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson queryByMachineCode(@RequestParam String machineCode) {
        MachineForQueryByMachineCodeVO machineForQueryByMachineCodeVO = this.machineService.queryByMachineCode(machineCode);
        int compareTo = new Date().compareTo(machineForQueryByMachineCodeVO.getEndTime());
        if (compareTo == 1){//当前时间大于endTime
            machineForQueryByMachineCodeVO.setIsExpired(0);//过期了
        }else {
            machineForQueryByMachineCodeVO.setIsExpired(1);//未过期
        }
        return ResJson.success(machineForQueryByMachineCodeVO);
    }

}
