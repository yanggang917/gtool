package com.cn.gtool.controller;

import com.cn.gtool.bean.dto.AddMachineDTO;
import com.cn.gtool.bean.dto.UpdateMachineEndDateDTO;
import com.cn.gtool.bean.entity.MachineDO;
import com.cn.gtool.bean.entity.PayDO;
import com.cn.gtool.bean.entity.UserPayLogDO;
import com.cn.gtool.bean.vo.MachineForQueryByMachineCodeVO;
import com.cn.gtool.service.MachineService;
import com.cn.gtool.service.PayService;
import com.cn.gtool.util.ResJson;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    @Resource
    private PayService payService;

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

    @RequestMapping(value = "/query-list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson queryList(@RequestParam int page, @RequestParam int size, @RequestParam int userId) {
        List<MachineDO> machineDOList = this.machineService.queryList(page, size, userId);
        int count = this.machineService.queryListCount(userId);
        return ResJson.success(machineDOList, count);
    }

    /**
     * 添加机器码
     * @param addMachineDTO
     * @return
     */
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson add(@RequestBody AddMachineDTO addMachineDTO) {
        MachineForQueryByMachineCodeVO machineForQueryByMachineCodeVO = this.machineService.queryByMachineCode(addMachineDTO.getMachineCode());
        if (machineForQueryByMachineCodeVO != null){
            return ResJson.failed(-1, "该机器码已经存在！");
        }

        //校验该用户是否拥有这张支付卡
        PayDO payDO = this.payService.queryByPayCodeAndUserId(addMachineDTO);
        if (payDO == null){
            return ResJson.failed(-1, "对不起，您没有这个支付码！");
        }
        if (payDO.getIsUsed() == 1){
            return ResJson.failed(-1, "对不起，您这个支付码已经被使用了，请联系管理员！");
        }


        MachineDO machineDO = new MachineDO();
        BeanUtils.copyProperties(addMachineDTO, machineDO);

        Date date = new Date();
        machineDO.setCreateTime(date);
        machineDO.setEndTime(getAfterDay(date,payDO.getDayLength()));//根据支付码计算

        //添加机器码
        this.machineService.add(machineDO);

        //更新该支付码已被使用啦
        updatePayCode(addMachineDTO, date);

        return ResJson.success();
    }

    /**
     * 一个支付码的成长轨迹
     * @param addMachineDTO
     * @param date
     */
    public void addPayLog(AddMachineDTO addMachineDTO, Date date, int status){
        UserPayLogDO userPayLogDO = new UserPayLogDO();
        userPayLogDO.setUserId(addMachineDTO.getUserId());
        userPayLogDO.setCreateTime(date);
        userPayLogDO.setMachineCode(addMachineDTO.getMachineCode());
        userPayLogDO.setPayCode(addMachineDTO.getPayCode());
        userPayLogDO.setStatus(status);
        machineService.addPayLog(userPayLogDO);
    }

    /**
     * 消费一张支付码
     * @param addMachineDTO
     */
    private void updatePayCode(AddMachineDTO addMachineDTO, Date date) {
        PayDO param = new PayDO();
        param.setIsUsed(1);//已经使用
        param.setPayCode(addMachineDTO.getPayCode());
        param.setUseTime(date);
        param.setMachineName(addMachineDTO.getMachineName());
        param.setMachineCode(addMachineDTO.getMachineCode());
        param.setUserId(addMachineDTO.getUserId());
        param.setIsUsed(1);//使用了
        this.payService.update(param);

        //add log
        addPayLog(addMachineDTO, date, 2);//被使用了
    }

    /**
     * 续期
     * @param updateMachineEndDateDTO
     * @return
     */
    @RequestMapping(value = "/updateEndTime", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson updateEndTime(@RequestBody UpdateMachineEndDateDTO updateMachineEndDateDTO) {
        MachineForQueryByMachineCodeVO machineForQueryByMachineCode = this.machineService.queryByMachineCode(updateMachineEndDateDTO.getMachineCode());

        //校验该用户是否拥有这张支付卡
        PayDO payDO = this.payService.queryByPayCodeAndUserId(updateMachineEndDateDTO);
        if (payDO == null){
            return ResJson.failed("对不起，您没有这个支付码！");
        }
        if (payDO.getIsUsed() == 1){
            return ResJson.failed("对不起，您这个支付码已经被使用了，请联系管理员！");
        }

        MachineDO machineDO = new MachineDO();
        BeanUtils.copyProperties(updateMachineEndDateDTO, machineDO);

        Date date = new Date();
        int compareTo = date.compareTo(machineForQueryByMachineCode.getEndTime());
        if (compareTo == 1){//当前时间大于endTime
            machineDO.setEndTime(getAfterDay(date, payDO.getDayLength()));//过期了，拿当前时间去续期
        }else {
            machineDO.setEndTime(getAfterDay(machineForQueryByMachineCode.getEndTime(), payDO.getDayLength()));//未过期，拿到期时间去续期
        }

        //根据机器码去更新机器码使用期限
        this.machineService.updateEndTimeByCode(machineDO);

        //更新支付码状态
        updatePayCode(updateMachineEndDateDTO, date);

        return ResJson.success();
    }

    /**
     * 指定日期后几天
     *
     * @param count    天数
     * @return
     */
    Date getAfterDay(Date date, int count) {
        try {
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.DATE, count);
            return cl.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
