package com.cn.gtool.service;

import com.cn.gtool.bean.entity.GameUserDO;

import java.util.List;

/**
 * @Auther: yg
 * @Date: 2019/12/4 09:17
 * @Description:
 */
public interface GameUserService {
    List<GameUserDO> selectList();

    void add(GameUserDO gameUserDO);

    void delete(Integer id);

    void update(GameUserDO gameUserDO);

    GameUserDO queryById(Integer id);
}
