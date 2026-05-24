package com.example.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test.bean.BorrowRecordBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 借阅记录数据访问层
 */
public interface BorrowRecordMapper extends BaseMapper<BorrowRecordBean> {

    /** 归还图书（更新归还日期和状态） */
    int returnBook(@Param("id") String id, @Param("returnDate") String returnDate);

    /** 根据用户ID查询借阅记录（关联用户名和书名） */
    List<BorrowRecordBean> selectByUserId(@Param("userId") int userId);

    /** 查询所有借阅记录（关联用户名和书名） */
    List<BorrowRecordBean> selectAllRecords();
}
