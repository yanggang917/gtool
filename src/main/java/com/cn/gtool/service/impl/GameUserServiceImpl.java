package com.cn.gtool.service.impl;

import com.cn.gtool.bean.entity.GameUserDO;
import com.cn.gtool.dao.GameUserDOMapper;
import com.cn.gtool.service.GameUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: yg
 * @Date: 2019/12/4 09:17
 * @Description:
 */
@Service
public class GameUserServiceImpl implements GameUserService {
    @Resource
    private GameUserDOMapper gameUserDOMapper;

    @Override
    public List<GameUserDO> selectList() {
        return this.gameUserDOMapper.selectList();
    }

    @Override
    public void add(GameUserDO gameUserDO) {
        gameUserDOMapper.add(gameUserDO);
    }

    @Override
    public void delete(Integer id) {
        gameUserDOMapper.delete(id);
    }

    @Override
    public void update(GameUserDO gameUserDO) {
        gameUserDOMapper.update(gameUserDO);
    }

    @Override
    public GameUserDO queryById(Integer id) {
        return gameUserDOMapper.queryById(id);
    }
}
