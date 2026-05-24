package com.example.test.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.test.bean.BorrowRecordBean;
import com.example.test.mapper.BookMapper;
import com.example.test.mapper.BorrowRecordMapper;
import com.example.test.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 借阅业务逻辑实现
 */
@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    BorrowRecordMapper borrowRecordMapper;

    @Autowired
    BookMapper bookMapper;

    /** 借阅图书，设置借阅日期和应还日期（30天），同时减少可借数量 */
    @Override
    @Transactional
    public int borrowBook(int userId, int bookId) {
        BorrowRecordBean record = new BorrowRecordBean();
        record.setUserId(userId);
        record.setBookId(bookId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        String dueDate = sdf.format(cal.getTime());

        record.setBorrowDate(now);
        record.setDueDate(dueDate);

        // 减少可借数量
        bookMapper.updateAvailableQuantity(String.valueOf(bookId), -1);
        return borrowRecordMapper.insert(record);
    }

    /** 归还图书，更新归还日期和状态，恢复可借数量 */
    @Override
    @Transactional
    public int returnBook(String recordId) {
        BorrowRecordBean record = borrowRecordMapper.selectById(recordId);
        if (record != null) {
            // 恢复可借数量
            bookMapper.updateAvailableQuantity(String.valueOf(record.getBookId()), 1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return borrowRecordMapper.returnBook(recordId, sdf.format(new Date()));
    }

    /** 分页查询当前用户的借阅记录 */
    @Override
    public IPage<BorrowRecordBean> queryMyBorrows(int userId, int pageNum, int pageSize) {
        return borrowRecordMapper.selectByUserId(new Page<>(pageNum, pageSize), userId);
    }

    /** 分页查询所有借阅记录 */
    @Override
    public IPage<BorrowRecordBean> queryAllBorrows(int pageNum, int pageSize) {
        return borrowRecordMapper.selectAllRecords(new Page<>(pageNum, pageSize));
    }
}
