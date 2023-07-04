package com.yangzhuo.book_management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangzhuo.book_management.entity.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMapper extends BaseMapper<Book> {
}
