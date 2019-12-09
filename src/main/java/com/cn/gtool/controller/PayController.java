package com.cn.gtool.controller;

import com.cn.gtool.bean.dto.AddMachineDTO;
import com.cn.gtool.bean.dto.AddPayDTO;
import com.cn.gtool.bean.entity.PayDO;
import com.cn.gtool.bean.entity.UserDO;
import com.cn.gtool.bean.entity.UserPayLogDO;
import com.cn.gtool.service.PayService;
import com.cn.gtool.service.UserService;
import com.cn.gtool.util.ResJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/pay")
public class PayController {
    @Resource
    private PayService payService;
    @Resource
    private UserService userService;
    @Resource
    private MachineController machineController;

    @RequestMapping(value = "/query-by-payCode", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson queryByMachineCode(@RequestParam String payCode) {
        PayDO payDO = this.payService.queryByPayCode(payCode);
        return ResJson.success(payDO);
    }

    /**
     * 生成支付码
     * @param payDO
     * @return
     */
    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson add(@RequestBody AddPayDTO payDO) {
        Date date = new Date();
        payDO.setCreateTime(date);
        if (payDO.getPayType() ==1){//日卡
            payDO.setDayLength(1);
        }
        if (payDO.getPayType() ==2){//周卡
            payDO.setDayLength(7);
        }
        if (payDO.getPayType() ==3){//月卡
            payDO.setDayLength(31);
        }

        //校验用户是否存在
        UserDO params = new UserDO();
        params.setUsername(payDO.getUserName());
        UserDO userDO = this.userService.queryByName(params);
        if (userDO == null){
            return ResJson.failed("该用户不存在，请检查是否填写有误！");
        }

        payDO.setUserId(userDO.getId());
        //循环数量
        for (int i=0; i<payDO.getNum();i++){
            String payCode = UUID.randomUUID().toString().substring(24);
            payDO.setPayCode(payCode);
            this.payService.add(payDO);

            //添加支付码流水
            AddMachineDTO addMachineDTO = new AddMachineDTO();
            addMachineDTO.setPayCode(payCode);
            addMachineDTO.setUserId(userDO.getId());
            this.machineController.addPayLog(addMachineDTO, date, 0);//出生了
        }
        return ResJson.success(payDO);
    }


    @RequestMapping(value = "/query-list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson queryList(@RequestParam int page, @RequestParam int size, @RequestParam int userId) {
        List<PayDO> payDOList = this.payService.queryList(page,size, userId);
        int total = this.payService.queryListCount(userId);
        return ResJson.success(payDOList, total);
    }

    /**
     * 查询可用的支付码列表（固定10条）
     * @param userId
     * @return
     */
    @RequestMapping(value = "/query-list-for-usable", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson queryListForUsable(@RequestParam int userId) {
        List<PayDO> payDOList = this.payService.queryListForUsable(userId);
        return ResJson.success(payDOList);
    }

    /**
     * 转让支付码
     * @param payDO
     * @return
     */
    @RequestMapping(value = "/zr", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson update(@RequestBody PayDO payDO) {
        //校验用户是否存在
        UserDO params = new UserDO();
        params.setUsername(payDO.getUserName());
        UserDO userDO = this.userService.queryByName(params);
        if (userDO == null){
            return ResJson.failed("您要转让的用户不存在，请检查是否填写有误！");
        }

        payDO.setUserId(userDO.getId());
        this.payService.update(payDO);

        //添加支付码流水
        AddMachineDTO addMachineDTO = new AddMachineDTO();
        addMachineDTO.setPayCode(payDO.getPayCode());
        addMachineDTO.setUserId(payDO.getUserId());
        this.machineController.addPayLog(addMachineDTO, new Date(), 1);//转让
        return ResJson.success();
    }


}
