package com.cn.gtool.service.impl;

import com.cn.gtool.bean.entity.UserDO;
import com.cn.gtool.dao.UserDOMapper;
import com.cn.gtool.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;


    @Override
    public UserDO queryByNameAndPass(UserDO userDO) {
        return this.userDOMapper.queryByNameAndPass(userDO);
    }

    @Override
    public UserDO queryByName(UserDO userDO) {
        return this.userDOMapper.queryByName(userDO);
    }

    @Override
    public void add(UserDO userDO) {
        this.userDOMapper.add(userDO);
    }

    @Override
    public void updatePassword(String newPass, String oldPass, int id) {
        this.userDOMapper.updatePassword(newPass,oldPass,id);
    }
}
