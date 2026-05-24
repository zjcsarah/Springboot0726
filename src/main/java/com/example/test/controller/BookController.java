package com.example.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.test.bean.BookBean;
import com.example.test.mapper.BookMapper;
import com.example.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /** 分页获取图书列表（支持关键词和分类筛选） */
    @GetMapping
    public Map<String, Object> listBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "12") int pageSize) {
        Map<String, Object> map = new HashMap<>();
        IPage<BookBean> page;
        if (keyword != null || category != null) {
            page = bookService.searchBooks(keyword, category, pageNum, pageSize);
        } else {
            page = bookService.queryAllBooks(pageNum, pageSize);
        }
        map.put("code", 0);
        map.put("data", page);
        return map;
    }

    /** 根据ID获取图书详情 */
    @GetMapping("/{id}")
    public Map<String, Object> getBook(@PathVariable String id) {
        Map<String, Object> map = new HashMap<>();
        BookBean book = bookService.queryBookById(id);
        map.put("code", 0);
        map.put("data", book);
        return map;
    }

    /** 新增图书 */
    @PostMapping
    public Map<String, Object> addBook(@RequestBody BookBean bookBean) {
        Map<String, Object> map = new HashMap<>();
        int flag = bookService.addBook(bookBean);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "新增图书成功" : "新增图书失败");
        return map;
    }

    /** 根据ID修改图书信息 */
    @PutMapping("/{id}")
    public Map<String, Object> updateBook(@PathVariable String id, @RequestBody BookBean bookBean) {
        Map<String, Object> map = new HashMap<>();
        bookBean.setId(Integer.parseInt(id));
        int flag = bookService.modifyBook(bookBean);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "修改图书成功" : "修改图书失败");
        return map;
    }

    /** 根据ID删除图书 */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteBook(@PathVariable String id) {
        Map<String, Object> map = new HashMap<>();
        int flag = bookService.dropBook(id);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "删除图书成功" : "删除图书失败");
        return map;
    }

    /** 获取所有图书分类列表（去重） */
    @GetMapping("/categories")
    public Map<String, Object> getCategories() {
        Map<String, Object> map = new HashMap<>();
        List<BookBean> books = bookMapper.selectList(null);
        List<String> categories = books.stream()
                .map(BookBean::getCategory)
                .filter(c -> c != null && !c.isEmpty())
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        map.put("code", 0);
        map.put("data", categories);
        return map;
    }
}
