package com.cn.gtool.bean.dto;

import com.cn.gtool.bean.entity.UserDO;

/**
 * @Auther: yg
 * @Date: 2019/12/14 14:51
 * @Description:
 */
public class QueryUserDTO extends UserDO {

    private String Code;


    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
