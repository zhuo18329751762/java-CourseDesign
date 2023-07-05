package com.yangzhuo.book_management.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yangzhuo.book_management.utils.DateUtils;

import java.util.Date;

/**
 * @program: library
 * @description: 书籍实体类
 * @author:
 * @create: 2023-07-03 11:06
 **/
public class Book {
    //书籍ID
    @TableId(type = IdType.INPUT)
    private String bookID;
    //书籍类别ID
    private String categoryID;
    //书名
    private String bookName;
    //书籍作者
    private String bookAuthor;
    //出版社
    private String bookPublisher;
    //出版日期
    private Date publishTime;
    //单价
    private float bookPrice;
    //总数
    private int bookSum;
    //借出数量
    private int bookLend;
    //ISBN编号
    private String isbn;


    public Book() {
    }

    public Book(String bookID, String categoryID, String bookName, String bookAuthor, String bookPublisher, Date publishTime, float bookPrice, int bookSum, int bookLend, String isbn) {
        this.bookID = bookID;
        this.categoryID = categoryID;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.publishTime = publishTime;
        this.bookPrice = bookPrice;
        this.bookSum = bookSum;
        this.bookLend = bookLend;
        this.isbn = isbn;
    }

    public Book(String bookID, String categoryID, String bookName, String bookAuthor, String bookPublisher, String publishTime, String bookPrice, String bookSum, String bookLend, String isbn) {
        this.bookID = bookID;
        this.categoryID = categoryID;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.publishTime = new DateUtils().toDate(publishTime);
        this.bookPrice = Float.parseFloat(bookPrice);
        this.bookSum = Integer.parseInt(bookSum);
        this.bookLend = Integer.parseInt(bookLend);
        this.isbn = isbn;
    }

    /**
     * 获取
     * @return bookID
     */
    public String getBookID() {
        return bookID;
    }

    /**
     * 设置
     * @param bookID
     */
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    /**
     * 获取
     * @return categoryID
     */
    public String getCategoryID() {
        return categoryID;
    }

    /**
     * 设置
     * @param categoryID
     */
    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    /**
     * 获取
     * @return bookName
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * 设置
     * @param bookName
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * 获取
     * @return bookAuthor
     */
    public String getBookAuthor() {
        return bookAuthor;
    }

    /**
     * 设置
     * @param bookAuthor
     */
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    /**
     * 获取
     * @return bookPublisher
     */
    public String getBookPublisher() {
        return bookPublisher;
    }

    /**
     * 设置
     * @param bookPublisher
     */
    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    /**
     * 获取
     * @return publishTime
     */
    public Date getPublishTime() {
        return publishTime;
    }

    /**
     * 设置
     * @param publishTime
     */
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 获取
     * @return bookPrice
     */
    public float getBookPrice() {
        return bookPrice;
    }

    /**
     * 设置
     * @param bookPrice
     */
    public void setBookPrice(float bookPrice) {
        this.bookPrice = bookPrice;
    }

    /**
     * 获取
     * @return bookSum
     */
    public int getBookSum() {
        return bookSum;
    }

    /**
     * 设置
     * @param bookSum
     */
    public void setBookSum(int bookSum) {
        this.bookSum = bookSum;
    }

    /**
     * 获取
     * @return bookLend
     */
    public int getBookLend() {
        return bookLend;
    }

    /**
     * 设置
     * @param bookLend
     */
    public void setBookLend(int bookLend) {
        this.bookLend = bookLend;
    }

    /**
     * 获取
     * @return isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * 设置
     * @param isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String toString() {
        return "Book{bookID = " + bookID + ", categoryID = " + categoryID + ", bookName = " + bookName + ", bookAuthor = " + bookAuthor + ", bookPublisher = " + bookPublisher + ", publishTime = " + publishTime + ", bookPrice = " + bookPrice + ", bookSum = " + bookSum + ", bookLend = " + bookLend + ", isbn = " + isbn + "}";
    }
}
