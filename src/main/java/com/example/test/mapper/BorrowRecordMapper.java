package com.example.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.test.bean.BorrowRecordBean;
import org.apache.ibatis.annotations.Param;

/**
 * 借阅记录数据访问层
 */
public interface BorrowRecordMapper extends BaseMapper<BorrowRecordBean> {

    /** 归还图书 */
    int returnBook(@Param("id") String id, @Param("returnDate") String returnDate);

    /** 分页查询当前用户的借阅记录（关联用户名和书名） */
    IPage<BorrowRecordBean> selectByUserId(Page<BorrowRecordBean> page, @Param("userId") int userId);

    /** 分页查询所有借阅记录（关联用户名和书名） */
    IPage<BorrowRecordBean> selectAllRecords(Page<BorrowRecordBean> page);
}
