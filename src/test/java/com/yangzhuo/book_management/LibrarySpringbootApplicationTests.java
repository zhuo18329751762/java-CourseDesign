package com.yangzhuo.book_management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangzhuo.book_management.entity.Reader;
import com.yangzhuo.book_management.mapper.ReaderMapper;
import com.yangzhuo.book_management.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class bookSpringbootApplicationTests {
    @Resource
    ReaderMapper readerMapper;

    @Test
    public void a(){
        // 创建一个SimpleDateFormat对象，指定日期和时间的模式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间的Date对象
        Date now = new Date();
        // 使用format方法将Date对象转换为字符串
        String str = sdf.format(now);
        // 输出字符串
        System.out.println(str);
    }
    @Test
    public void login() {
        String id = "r001";
        String password = "r0012";
        QueryWrapper<Reader> wrapper = new QueryWrapper<>();
        Reader reader = readerMapper.selectOne(wrapper.eq("id", id));
        Map<String, String> map = new HashMap<>();
        if (reader == null || !reader.getPassword().equals(password)) {
            System.out.println("false");
        } else {
            System.out.println("true");
        }
    }

}
