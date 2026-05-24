package com.example.test.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.test.bean.BookBean;
import com.example.test.mapper.BookMapper;
import com.example.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 图书业务逻辑实现
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookMapper bookMapper;

    /** 分页查询所有图书，按ID倒序 */
    @Override
    public IPage<BookBean> queryAllBooks(int pageNum, int pageSize) {
        Page<BookBean> page = new Page<>(pageNum, pageSize);
        QueryWrapper<BookBean> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return bookMapper.selectPage(page, wrapper);
    }

    /** 分页搜索图书 */
    @Override
    public IPage<BookBean> searchBooks(String keyword, String category, int pageNum, int pageSize) {
        Page<BookBean> page = new Page<>(pageNum, pageSize);
        QueryWrapper<BookBean> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("title", keyword).or().like("author", keyword));
        }
        if (category != null && !category.isEmpty()) {
            wrapper.eq("category", category);
        }
        wrapper.orderByDesc("id");
        return bookMapper.selectPage(page, wrapper);
    }

    /** 根据ID查询图书详情 */
    @Override
    public BookBean queryBookById(String id) {
        return bookMapper.selectById(id);
    }

    /** 新增图书 */
    @Override
    public int addBook(BookBean bookBean) {
        return bookMapper.insert(bookBean);
    }

    /** 修改图书信息 */
    @Override
    public int modifyBook(BookBean bookBean) {
        return bookMapper.updateById(bookBean);
    }

    /** 根据ID删除图书 */
    @Override
    public int dropBook(String id) {
        return bookMapper.deleteById(id);
    }
}
