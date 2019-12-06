package com.cn.gtool.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Auther: yg
 * @Date: 2019/12/6 18:41
 * @Description:
 */
@Mapper
public interface NoticeDOMapper {

    @Select("SELECT a.content from t_notice a ORDER BY a.create_time desc LIMIT 1")
    String getNews();
}
