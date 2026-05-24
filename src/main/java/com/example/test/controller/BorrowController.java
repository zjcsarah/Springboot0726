package com.example.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.test.bean.BaseResult;
import com.example.test.bean.BorrowRecordBean;
import com.example.test.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 借阅管理 REST API
 */
@RestController
@RequestMapping("/api")
public class BorrowController {

    @Autowired
    BorrowService borrowService;

    @PostMapping("/borrow/{bookId}")
    public BaseResult<?> borrowBook(@PathVariable String bookId, @RequestBody Map<String, Object> body) {
        int userId = Integer.parseInt(body.get("userId").toString());
        int flag = borrowService.borrowBook(userId, Integer.parseInt(bookId));
        return flag == 1 ? BaseResult.ok(null, "借阅成功") : BaseResult.fail("借阅失败");
    }

    @PostMapping("/return/{recordId}")
    public BaseResult<?> returnBook(@PathVariable String recordId) {
        int flag = borrowService.returnBook(recordId);
        return flag == 1 ? BaseResult.ok(null, "归还成功") : BaseResult.fail("归还失败");
    }

    @GetMapping("/borrows/my")
    public BaseResult<IPage<BorrowRecordBean>> myBorrows(
            @RequestParam int userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return BaseResult.ok(borrowService.queryMyBorrows(userId, pageNum, pageSize));
    }

    @PostMapping("/borrows")
    public BaseResult<?> addBorrow(@RequestBody BorrowRecordBean record) {
        int flag = borrowService.addBorrowRecord(record);
        return flag == 1 ? BaseResult.ok(null, "新增成功") : BaseResult.fail("新增失败");
    }

    @PutMapping("/borrows/{id}")
    public BaseResult<?> updateBorrow(@PathVariable String id, @RequestBody BorrowRecordBean record) {
        record.setId(Integer.parseInt(id));
        int flag = borrowService.updateBorrowRecord(record);
        return flag == 1 ? BaseResult.ok(null, "修改成功") : BaseResult.fail("修改失败");
    }

    @DeleteMapping("/borrows/{id}")
    public BaseResult<?> deleteBorrow(@PathVariable String id) {
        int flag = borrowService.deleteBorrowRecord(id);
        return flag == 1 ? BaseResult.ok(null, "删除成功") : BaseResult.fail("删除失败");
    }

    @GetMapping("/borrows/all")
    public BaseResult<IPage<BorrowRecordBean>> allBorrows(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return BaseResult.ok(borrowService.queryAllBorrows(pageNum, pageSize));
    }
}