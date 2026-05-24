package com.example.test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.test.bean.BookBean;

/**
 * 图书业务逻辑接口
 */
public interface BookService {

    /** 分页查询所有图书 */
    IPage<BookBean> queryAllBooks(int pageNum, int pageSize);

    /** 分页搜索图书（关键词+分类） */
    IPage<BookBean> searchBooks(String keyword, String category, int pageNum, int pageSize);

    /** 根据ID查询图书详情 */
    BookBean queryBookById(String id);

    /** 新增图书 */
    int addBook(BookBean bookBean);

    /** 修改图书信息 */
    int modifyBook(BookBean bookBean);

    /** 根据ID删除图书 */
    int dropBook(String id);
}
