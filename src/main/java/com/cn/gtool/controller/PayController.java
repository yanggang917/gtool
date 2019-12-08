package com.cn.gtool.controller;

import com.cn.gtool.bean.dto.AddPayDTO;
import com.cn.gtool.bean.entity.PayDO;
import com.cn.gtool.bean.entity.UserDO;
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

    @RequestMapping(value = "/query-by-payCode", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson queryByMachineCode(@RequestParam String payCode) {
        PayDO payDO = this.payService.queryByPayCode(payCode);
        return ResJson.success(payDO);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson add(@RequestBody AddPayDTO payDO) {
        payDO.setCreateTime(new Date());
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
            payDO.setPayCode(UUID.randomUUID().toString().substring(24));
            this.payService.add(payDO);
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

    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson update(@RequestBody PayDO payDO) {
        this.payService.update(payDO);
        return ResJson.success();
    }


}
