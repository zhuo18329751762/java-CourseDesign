package com.yangzhuo.book_management.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangzhuo.book_management.entity.Borrow;
import com.yangzhuo.book_management.mapper.BorrowMapper;
import com.yangzhuo.book_management.service.BorrowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Resource
    BorrowMapper borrowMapper;

    @Override
    public List<Borrow> getBorrowList(String bookID, String readerID, String isNull) {
        List<Borrow> borrowList = null;
        QueryWrapper<Borrow> wrapper = new QueryWrapper<>();
        //当bookID和readerID都为空，说明显示全部
        if ((bookID == null || bookID == "") && (readerID == null || readerID == "")) {
            //如果isNull为T，查询未归还的书籍
            if ("T".equals(isNull)) {
                borrowList = borrowMapper.selectList(wrapper.isNull("returnTime"));
            } else {
                borrowList = borrowMapper.selectList(null);
            }
        } else if (bookID == null || bookID == "") {
            //readerID不为空，查询某人所有借阅记录
            //如果isNull为T，查询此人未归还的书籍
            if ("T".equals(isNull)) {
                borrowList = borrowMapper.selectList(wrapper.eq("readerID", readerID).isNull("returnTime"));
            } else {
                borrowList = borrowMapper.selectList(wrapper.eq("readerID", readerID));
            }
        } else {
            //bookID不为空，查询某人所有借阅记录
            //如果isNull为T，查询此人未归还的书籍
            if ("T".equals(isNull)) {
                borrowList = borrowMapper.selectList(wrapper.eq("bookID", bookID).isNull("returnTime"));
            } else {
                borrowList = borrowMapper.selectList(wrapper.eq("bookID", bookID));
            }
        }
        return borrowList;
    }

    /**
     * 更新
     * @param borrow
     * @return
     */
    @Override
    public int updateBorrow(Borrow borrow) {
        QueryWrapper<Borrow> wrapper = new QueryWrapper();
        wrapper.eq("readerID", borrow.getReaderID())
                .eq("bookID", borrow.getBookID())
                .eq("borrowTime", borrow.getBorrowTime());
        return borrowMapper.update(borrow, wrapper);
    }

    /**
     * 判断用户是否超过8本未还，未超过返回1
     *
     * @param readerID
     * @return
     */
    @Override
    public int check(String readerID) {
        QueryWrapper<Borrow> wrapper = new QueryWrapper();
        wrapper.eq("readerID", readerID).isNull("returnTime");
        Long raw = borrowMapper.selectCount(wrapper);
        if (raw < 8) {
            return 1;
        }
        return 0;
    }

    @Override
    public int addBorrow(String readerID, String bookID, String borrowTime) {
        Borrow borrow = new Borrow(readerID, bookID, borrowTime);
        return borrowMapper.insert(borrow);
    }

    /**
     * 根据用户id，书本id，还有借阅时间来查询对应的借阅记录
     * @return
     */
    @Override
    public Borrow getBorrow(String readerID,String bookID,String borrowTime) {
        QueryWrapper<Borrow> wrapper=new QueryWrapper<>();
        wrapper.eq("readerID", readerID)
                .eq("bookID", bookID)
                .eq("borrowTime", borrowTime);
        return borrowMapper.selectOne(wrapper);
    }
}
