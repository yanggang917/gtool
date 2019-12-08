package com.cn.gtool.dao;

import com.cn.gtool.bean.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDOMapper {
    UserDO queryByNameAndPass(UserDO userDO);

    UserDO queryByName(UserDO userDO);

    void add(UserDO userDO);

    void updatePassword(String newPassword, String oldPassword, int id);
}
