package com.cn.gtool.controller;

import com.cn.gtool.service.NoticeService;
import com.cn.gtool.util.ResJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Auther: yg
 * @Date: 2019/12/6 17:21
 * @Description:
 */
@Controller
@RequestMapping(value = "/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;


    @RequestMapping(value = "/get-news", method = {RequestMethod.GET})
    @ResponseBody
    public ResJson getNews() {
        return ResJson.success(this.noticeService.getNews());
    }
}
