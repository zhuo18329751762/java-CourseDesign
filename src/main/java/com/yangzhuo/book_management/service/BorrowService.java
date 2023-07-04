package com.yangzhuo.book_management.service;

import com.yangzhuo.book_management.entity.Borrow;

import java.util.List;

public interface BorrowService {

    List<Borrow> getBorrowList(String bookID, String readerID, String isNull);

    int updateBorrow(Borrow borrow);

    int check(String readerID);

    int addBorrow(String readerID, String bookID, String borrowTime);
}
