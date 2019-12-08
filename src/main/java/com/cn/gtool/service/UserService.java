package com.cn.gtool.service;

import com.cn.gtool.bean.entity.UserDO;

public interface UserService {
    UserDO queryByNameAndPass(UserDO userDO);

    UserDO queryByName(UserDO userDO);

    void add(UserDO userDO);

    void updatePassword(String newPass, String oldPass, int id);
}
