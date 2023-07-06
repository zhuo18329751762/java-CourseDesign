package com.yangzhuo.book_management.service;

import com.yangzhuo.book_management.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getBookList(String bookName, String bookID);
    List<Book> getBookListByTypeId(String categoryID);

    Book findById(String bookID);

    int updateBook(Book book);

    int deleteBook(String bookID);

    int addBook(Book book);

    int returnBook(String bookID);

    int check(String bookID);

}
