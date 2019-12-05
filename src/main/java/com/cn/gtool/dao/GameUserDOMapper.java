package com.cn.gtool.dao;

import com.cn.gtool.bean.entity.GameUserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Auther: yg
 * @Date: 2019/12/4 09:18
 * @Description:
 */
@Mapper
public interface GameUserDOMapper {

    List<GameUserDO> selectList();

    void add(GameUserDO gameUserDO);

    void delete(Integer id);

    void update(GameUserDO gameUserDO);

    GameUserDO queryById(Integer id);
}
