package com.cn.gtool.dao;

import com.cn.gtool.bean.entity.PayDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PayDOMapper {


    PayDO queryByPayCode(String payCode);

    void add(PayDO payDO);

    List<PayDO> queryList(int offset, int size, int userId);

    void update(PayDO payDO);

    int queryListCount(int userId);

    List<PayDO> queryListForUsable(int userId);
}
