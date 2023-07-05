package com.yangzhuo.book_management.controller;

import com.yangzhuo.book_management.entity.Book;
import com.yangzhuo.book_management.entity.Borrow;
import com.yangzhuo.book_management.entity.Reader;
import com.yangzhuo.book_management.mapper.BorrowMapper;
import com.yangzhuo.book_management.service.BookService;
import com.yangzhuo.book_management.service.BorrowService;
import com.yangzhuo.book_management.service.ReaderService;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Controller
public class BorrowController {
    @Resource
    BorrowService borrowServiceImpl;
    @Resource
    BookService bookService;
    @Resource
    ReaderService readerServiceImpl;
    @Resource
    BorrowMapper borrowMapper;

    @RequestMapping("/admin/toBorrowList")
    public String toBorrowList(String bookID, String readerID, String isNull, Model model) {
        List<Borrow> borrowList = borrowServiceImpl.getBorrowList(bookID, readerID, isNull);
        model.addAttribute("borrowList", borrowList);
        return "admin/borrowList";
    }

    @RequestMapping("/reader/toReaderBorrow")
    public String toReaderBorrow(HttpSession httpSession, String isNull, Model model) {
        Reader reader = (Reader) httpSession.getAttribute("user");
        List<Borrow> borrowList = borrowServiceImpl.getBorrowList(null, reader.getId(), isNull);
        model.addAttribute("borrowList", borrowList);
        model.addAttribute("user", reader);
        return "reader/borrowList";
    }

    @RequestMapping("/admin/toReturnBook")
    @ResponseBody
    public String toReturnBook(String bookID,String borrowTime,HttpServletRequest request) {
        HttpSession session = request.getSession();
        //获取要还书的角色
        String role = (String) session.getAttribute("role");
        if("admin".equals(role)){
            //如果是管理员直接放行
            borrowTime = borrowTime.toString().replace("T", " ").replace(".000+08:00", "");
            Borrow borrow = new Borrow("r110", bookID, borrowTime, new Date());
            int state = bookService.returnBook(bookID);
            String msg = "归还失败，请重试！";
            if (state == 1) {
                state = borrowServiceImpl.updateBorrow(borrow);
                msg = "归还成功！";
            }
            return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
        }
        //强制类型转换
        Reader user = (Reader) session.getAttribute("user");
        String readerID=user.getId();
        Borrow borrow = borrowServiceImpl.getBorrow(readerID,bookID,borrowTime);
        //判断是否逾期，逾期要交罚款
        Date returnTime=new Date();
        long intervalTime=returnTime.getTime()-borrow.getBorrowTime().getTime();

        int state;
        String msg;
        //判断这个时间差是比3个月大还是比三个月小
        if(intervalTime>90*24*60*60*1000){
            //用户还书时间已经超过三个月
            state=0;
            System.out.println(intervalTime);
            double days=intervalTime/24/60/60/1000;
            int money= (int) (days*0.1);
            msg="归还失败，借书已超过三个月，需前往图书馆补交罚款"+money+"元(标准为每逾期一天，增加罚款0.1)";
            return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
        }
        //归还本书
        if(borrow!=null){
            borrow.setReturnTime(returnTime);
            state = borrowServiceImpl.updateBorrow(borrow);
            msg = "归还成功！";
        }else{
            state=0;
            msg="归还失败，请重试！";
        }
        return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
    }

    @RequestMapping("/admin/toAddBorrow")
    public String toAddBorrow(Model model) {
        List<Reader> readerList = readerServiceImpl.getReaderList(null, null);
        List<Book> bookList = bookService.getBookList(null, null);
        model.addAttribute("readerList", readerList);
        model.addAttribute("bookList", bookList);
        return "admin/addBorrow";
    }

    /**
     * 读者借阅书籍
     * @return
     */
    @RequestMapping("/admin/AddBorrow")
    @ResponseBody
    public String addBorrow(String bookID,HttpServletRequest request) {
        HttpSession session = request.getSession();
        //强制类型转换
        Reader user = (Reader) session.getAttribute("user");
        String readerID=user.getId();
        //获取当前时间
        // 创建一个SimpleDateFormat对象，指定日期和时间的模式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间的Date对象
        Date now = new Date();
        // 使用format方法将Date对象转换为字符串
        String borrowTime = sdf.format(now);
        System.out.println("readerID="+readerID);
        System.out.println("bookID="+bookID);
        System.out.println("borrowTime="+borrowTime);
        int state = 0;
        String msg = "馆内暂时无货,请等待。";
        //判断借出数是否小于书本总数
        if (bookService.check(bookID) == 1) {
            state = borrowServiceImpl.check(readerID);
            Reader reader = readerServiceImpl.findReader(readerID);
            if (reader != null) {
                if (state == 1) {
                    state = borrowServiceImpl.addBorrow(readerID, bookID, borrowTime);
                    msg = "借阅成功！";
                } else {
                    msg = "达到最大借书数量！请尽快归还！";
                }
            } else {
                state = 0;
                msg = "查无此用户！";
            }
        }
        //System.out.println("ReaderController -> registerReader(49): " + msg);
        return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
    }
}
