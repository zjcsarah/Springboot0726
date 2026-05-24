package com.example.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.test.bean.BorrowRecordBean;
import com.example.test.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 借阅管理 REST API
 */
@RestController
@RequestMapping("/api")
public class BorrowController {

    @Autowired
    BorrowService borrowService;

    /** 借阅图书 */
    @PostMapping("/borrow/{bookId}")
    public Map<String, Object> borrowBook(@PathVariable String bookId, @RequestBody Map<String, Object> body) {
        Map<String, Object> map = new HashMap<>();
        int userId = Integer.parseInt(body.get("userId").toString());
        int flag = borrowService.borrowBook(userId, Integer.parseInt(bookId));
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "借阅成功" : "借阅失败");
        return map;
    }

    /** 归还图书 */
    @PostMapping("/return/{recordId}")
    public Map<String, Object> returnBook(@PathVariable String recordId) {
        Map<String, Object> map = new HashMap<>();
        int flag = borrowService.returnBook(recordId);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "归还成功" : "归还失败");
        return map;
    }

    /** 分页查询当前用户的借阅记录 */
    @GetMapping("/borrows/my")
    public Map<String, Object> myBorrows(
            @RequestParam int userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> map = new HashMap<>();
        IPage<BorrowRecordBean> page = borrowService.queryMyBorrows(userId, pageNum, pageSize);
        map.put("code", 0);
        map.put("data", page);
        return map;
    }

    /** 新增借阅记录（管理员手动创建） */
    @PostMapping("/borrows")
    public Map<String, Object> addBorrow(@RequestBody BorrowRecordBean record) {
        Map<String, Object> map = new HashMap<>();
        int flag = borrowService.addBorrowRecord(record);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "新增成功" : "新增失败");
        return map;
    }

    /** 修改借阅记录 */
    @PutMapping("/borrows/{id}")
    public Map<String, Object> updateBorrow(@PathVariable String id, @RequestBody BorrowRecordBean record) {
        Map<String, Object> map = new HashMap<>();
        record.setId(Integer.parseInt(id));
        int flag = borrowService.updateBorrowRecord(record);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "修改成功" : "修改失败");
        return map;
    }

    /** 删除借阅记录 */
    @DeleteMapping("/borrows/{id}")
    public Map<String, Object> deleteBorrow(@PathVariable String id) {
        Map<String, Object> map = new HashMap<>();
        int flag = borrowService.deleteBorrowRecord(id);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "删除成功" : "删除失败");
        return map;
    }

    /** 分页查询所有借阅记录 */
    @GetMapping("/borrows/all")
    public Map<String, Object> allBorrows(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> map = new HashMap<>();
        IPage<BorrowRecordBean> page = borrowService.queryAllBorrows(pageNum, pageSize);
        map.put("code", 0);
        map.put("data", page);
        return map;
    }
}
