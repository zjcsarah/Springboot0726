package com.example.test.service;

import com.example.test.bean.BookBean;

import java.util.List;

/**
 * 图书业务逻辑接口
 */
public interface BookService {

    /** 查询所有图书 */
    List<BookBean> queryAllBooks();

    /** 根据关键词和分类搜索图书 */
    List<BookBean> searchBooks(String keyword, String category);

    /** 根据ID查询图书详情 */
    BookBean queryBookById(String id);

    /** 新增图书 */
    int addBook(BookBean bookBean);

    /** 修改图书信息 */
    int modifyBook(BookBean bookBean);

    /** 根据ID删除图书 */
    int dropBook(String id);
}
