package com.cn.gtool.controller;

import com.cn.gtool.bean.entity.GameUserDO;
import com.cn.gtool.service.GameUserService;
import com.cn.gtool.util.ResJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: yg
 * @Date: 2019/12/4 09:16
 * @Description:
 */
@Controller
@RequestMapping(value = "/game/user")
public class GameUserController {
    @Resource
    private GameUserService gameUserService;

    @RequestMapping(value = "/selectList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson selectList() {
        List<GameUserDO> gameUserDOList = this.gameUserService.selectList();
        return ResJson.success(gameUserDOList);
    }

    @RequestMapping(value = "/queryById", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson queryById(@RequestParam Integer ID) {
        GameUserDO gameUserDO = this.gameUserService.queryById(ID);
        return ResJson.success(gameUserDO);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson add(@RequestParam String 用户名,
                       @RequestParam String 密码,
                       @RequestParam String 开始时间,
                       @RequestParam String 登录时间,
                       @RequestParam String 到期时间) {
        GameUserDO gameUserDO = new GameUserDO();
        gameUserDO.set用户名(用户名);
        gameUserDO.set密码(密码);
        gameUserDO.set开始时间(stringTransteTime(开始时间));
        gameUserDO.set登录时间(stringTransteTime(登录时间));
        gameUserDO.set到期时间(stringTransteTime(到期时间));
        this.gameUserService.add(gameUserDO);
        return ResJson.success();
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson delete(@RequestParam Integer ID) {
        this.gameUserService.delete(ID);
        return ResJson.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResJson update(@RequestParam Integer ID,
                          @RequestParam String 用户名,
                          @RequestParam String 密码,
                          @RequestParam String 开始时间,
                          @RequestParam String 登录时间,
                          @RequestParam String 到期时间) {
        GameUserDO gameUserDO = new GameUserDO();
        gameUserDO.setID(ID);
        gameUserDO.set用户名(用户名);
        gameUserDO.set密码(密码);
        gameUserDO.set开始时间(stringTransteTime(开始时间));
        gameUserDO.set登录时间(stringTransteTime(登录时间));
        gameUserDO.set到期时间(stringTransteTime(到期时间));

        this.gameUserService.update(gameUserDO);
        return ResJson.success();
    }

    private Date stringTransteTime(String str){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }
}
