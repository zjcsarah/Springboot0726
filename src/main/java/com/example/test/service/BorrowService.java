package com.example.test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.test.bean.BorrowRecordBean;

/**
 * 借阅业务逻辑接口
 */
public interface BorrowService {

    /** 借阅图书，借阅期限30天 */
    int borrowBook(int userId, int bookId);

    /** 归还图书 */
    int returnBook(String recordId);

    /** 分页查询当前用户的借阅记录 */
    IPage<BorrowRecordBean> queryMyBorrows(int userId, int pageNum, int pageSize);

    /** 分页查询所有借阅记录 */
    IPage<BorrowRecordBean> queryAllBorrows(int pageNum, int pageSize);
}
