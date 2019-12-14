package com.cn.gtool.controller;

import com.cn.gtool.bean.dto.QueryUserDTO;
import com.cn.gtool.bean.dto.UpdatePassDTO;
import com.cn.gtool.bean.entity.UserDO;
import com.cn.gtool.service.UserService;
import com.cn.gtool.util.CodeUtil;
import com.cn.gtool.util.EncryptUtil;
import com.cn.gtool.util.ResJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson login(@RequestBody QueryUserDTO userDO) throws NoSuchAlgorithmException {
        //密码加密
        userDO.setPassword(EncryptUtil.md5(userDO.getPassword()));
        UserDO user = this.userService.queryByNameAndPass(userDO);
        if (user == null){
            return ResJson.failed("用户名密码错误！请重新输入");
        }
        return ResJson.success(user);
    }

    private boolean verifyCode(String code, HttpServletRequest request){
        String sessionCode = request.getSession().getAttribute("code").toString();
        if (code != null && !"".equals(code) && sessionCode != null && !"".equals(sessionCode)) {
            if (!code.equalsIgnoreCase(sessionCode)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/query-by-name", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson queryByName(@RequestBody UserDO userDO) {
        UserDO user = this.userService.queryByName(userDO);
        return ResJson.success(user);
    }

    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson register(@RequestBody QueryUserDTO userDO, HttpServletRequest request) throws NoSuchAlgorithmException {
        //校验用户名是否存在
        UserDO user = this.userService.queryByName(userDO);
        if (user != null){
            return ResJson.failed("该用户名已经存在！请重新输入");
        }

        // 验证验证码
        boolean bool = verifyCode(userDO.getCode(), request);
        if (!bool){
            return ResJson.failed("验证码错误！请重新输入");
        }

        //密码加密
        userDO.setPassword(EncryptUtil.md5(userDO.getPassword()));
        userDO.setCreateTime(new Date());
        this.userService.add(userDO);
        return ResJson.success();
    }

    @RequestMapping(value = "/updatePassword", method = {RequestMethod.POST})
    @ResponseBody
    public ResJson updatePassword(@RequestBody UpdatePassDTO updatePassDTO) throws NoSuchAlgorithmException {
        this.userService.updatePassword(EncryptUtil.md5(updatePassDTO.getNewPass()), EncryptUtil.md5(updatePassDTO.getOldPass()), updatePassDTO.getUserId());
        return ResJson.success();
    }


    @RequestMapping(value = "/getCode", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void getCode(HttpServletRequest req, HttpServletResponse resp) {
        // 调用工具类生成的验证码和验证码图片
        Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();

        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession();
        session.setAttribute("code", codeMap.get("code").toString());
        logger.info("code:{}",codeMap.get("code").toString());

        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", -1);

        resp.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos;
        try {
            sos = resp.getOutputStream();
            ImageIO.write((RenderedImage) codeMap.get("codePic"), "jpeg", sos);
            sos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
