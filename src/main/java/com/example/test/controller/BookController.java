package com.example.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.test.bean.BaseResult;
import com.example.test.bean.BookBean;
import com.example.test.mapper.BookMapper;
import com.example.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书管理 REST API
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper bookMapper;

    @GetMapping
    public BaseResult<IPage<BookBean>> listBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "12") int pageSize) {
        IPage<BookBean> page;
        if (keyword != null || category != null) {
            page = bookService.searchBooks(keyword, category, pageNum, pageSize);
        } else {
            page = bookService.queryAllBooks(pageNum, pageSize);
        }
        return BaseResult.ok(page);
    }

    @GetMapping("/{id}")
    public BaseResult<BookBean> getBook(@PathVariable String id) {
        return BaseResult.ok(bookService.queryBookById(id));
    }

    @PostMapping
    public BaseResult<?> addBook(@RequestBody BookBean bookBean) {
        int flag = bookService.addBook(bookBean);
        return flag == 1 ? BaseResult.ok(null, "新增图书成功") : BaseResult.fail("新增图书失败");
    }

    @PutMapping("/{id}")
    public BaseResult<?> updateBook(@PathVariable String id, @RequestBody BookBean bookBean) {
        bookBean.setId(Integer.parseInt(id));
        int flag = bookService.modifyBook(bookBean);
        return flag == 1 ? BaseResult.ok(null, "修改图书成功") : BaseResult.fail("修改图书失败");
    }

    @DeleteMapping("/{id}")
    public BaseResult<?> deleteBook(@PathVariable String id) {
        int flag = bookService.dropBook(id);
        return flag == 1 ? BaseResult.ok(null, "删除图书成功") : BaseResult.fail("删除图书失败");
    }

    @GetMapping("/categories")
    public BaseResult<List<String>> getCategories() {
        List<BookBean> books = bookMapper.selectList(null);
        List<String> categories = books.stream()
                .map(BookBean::getCategory)
                .filter(c -> c != null && !c.isEmpty())
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        return BaseResult.ok(categories);
    }
}