package com.cn.gtool.controller;

import com.cn.gtool.bean.dto.UpdatePassDTO;
import com.cn.gtool.bean.entity.UserDO;
import com.cn.gtool.service.UserService;
import com.cn.gtool.util.ResJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson login(@RequestBody UserDO userDO) {
        UserDO user = this.userService.queryByNameAndPass(userDO);
        if (user == null){
            return ResJson.failed("用户名密码错误！请重新输入");
        }
        return ResJson.success(user);
    }

    @RequestMapping(value = "/query-by-name", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson queryByName(@RequestBody UserDO userDO) {
        UserDO user = this.userService.queryByName(userDO);
        return ResJson.success(user);
    }

    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson register(@RequestBody UserDO userDO) {
        UserDO user = this.userService.queryByName(userDO);
        if (user != null){
            return ResJson.failed("该用户名已经存在！请重新输入");
        }
        userDO.setCreateTime(new Date());
        this.userService.add(userDO);
        return ResJson.success();
    }

    @RequestMapping(value = "/updatePassword", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson updatePassword(@RequestBody UpdatePassDTO updatePassDTO) {
        this.userService.updatePassword(updatePassDTO.getNewPass(), updatePassDTO.getOldPass(), updatePassDTO.getUserId());
        return ResJson.success();
    }

}
