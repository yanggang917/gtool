package com.cn.gtool.bean.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: yg
 * @Date: 2019/12/4 09:20
 * @Description:
 */
@Data
public class GameUserDO {
    private Integer ID;
    private String 用户名;
    private String 密码;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy/MM/dd HH:mm:ss")
    private Date 开始时间;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy/MM/dd HH:mm:ss")
    private Date 登录时间;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy/MM/dd HH:mm:ss")
    private Date 到期时间;

}
