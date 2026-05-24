package com.example.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test.bean.BookBean;
import org.apache.ibatis.annotations.Param;

/**
 * 图书数据访问层
 */
public interface BookMapper extends BaseMapper<BookBean> {

    /** 更新图书可借数量（delta为正数增加，负数减少） */
    int updateAvailableQuantity(@Param("id") String id, @Param("delta") int delta);
}
