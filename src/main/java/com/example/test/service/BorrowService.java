package com.example.test.service;

import com.example.test.bean.BorrowRecordBean;

import java.util.List;

/**
 * 借阅业务逻辑接口
 */
public interface BorrowService {

    /** 借阅图书（用户ID、图书ID），借阅期限30天 */
    int borrowBook(int userId, int bookId);

    /** 归还图书（根据借阅记录ID） */
    int returnBook(String recordId);

    /** 查询当前用户的借阅记录 */
    List<BorrowRecordBean> queryMyBorrows(int userId);

    /** 查询所有借阅记录（管理员用） */
    List<BorrowRecordBean> queryAllBorrows();
}
