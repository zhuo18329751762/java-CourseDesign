package com.yangzhuo.book_management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yangzhuo.book_management.mapper")
public class Book_ManagementSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(Book_ManagementSpringbootApplication.class, args);
    }

}
