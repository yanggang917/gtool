package com.cn.gtool.dao;

import com.cn.gtool.bean.entity.SoftDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SoftDOMapper {


    SoftDO getNewSoftVersion(int type);

    void updateDownNum(int id);
}
