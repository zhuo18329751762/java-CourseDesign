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
        if ("admin".equals(role)) {
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
        String readerID = user.getId();
        user=readerServiceImpl.findReader(readerID);
        Borrow borrow = borrowServiceImpl.getBorrow(readerID, bookID, borrowTime);
        //判断是否逾期，逾期要交罚款
        Date returnTime = new Date();
        long intervalTime = returnTime.getTime() - borrow.getBorrowTime().getTime();
        int state;
        String msg;
        //判断这个时间差是比3个月大还是比三个月小
        long time = 90 * 24 * 60 * 60 * 1000L;
        if (intervalTime > time) {
            //用户还书时间已经超过三个月
            state = 0;
            double days = intervalTime / 24 / 60 / 60 / 1000;
            int money = (int) (days * 0.1);
            //判断账户中的钱够不够
            Integer account = Integer.parseInt(user.getAccount());
            Integer sub = account - money;
            if (sub < 0) {
                //如果账户钱不够，则提示用户前往线下缴费
                msg = "归还成功，借书已超过三个月，已自动扣费" + money + "元，当前账户欠费" + Math.abs(sub) + "元";
                if(sub<-300){
                    msg = "归还失败，每个人最多只能透支300元，请前往图书馆进行充值";
                    return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
                }
            } else {
                //账户钱够,提示用户当前余额
                msg = "归还成功，已自动扣费" + money + "元，当前账户余额" + sub + "元";
            }
            //扣钱
            //将用户可借阅书籍数量+1
            Reader reader = readerServiceImpl.findReader(readerID);
            reader.setResidue(reader.getResidue() + 1);
            //更改用户账户余额
            reader.setAccount(sub.toString());
            //更新用户信息
            readerServiceImpl.updateReader(reader);
            //将书籍借出数量bookLend-1
            Book byId = bookService.findById(bookID);
            byId.setBookLend(byId.getBookLend() - 1);
            //更新书籍信息
            bookService.updateBook(byId);
            borrow.setReturnTime(returnTime);
            borrowServiceImpl.updateBorrow(borrow);
            return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
        }else {
            //没有超过三个月
            if (borrow != null) {
                msg = "归还成功!";
                //将用户可借阅书籍数量+1
                Reader reader = readerServiceImpl.findReader(readerID);
                reader.setResidue(reader.getResidue() + 1);
                //更新用户信息
                readerServiceImpl.updateReader(reader);
                //将书籍借出数量bookLend-1
                Book byId = bookService.findById(bookID);
                byId.setBookLend(byId.getBookLend() - 1);
                //更新书籍信息
                bookService.updateBook(byId);
                borrow.setReturnTime(returnTime);
                state = borrowServiceImpl.updateBorrow(borrow);
            } else {
                state = 0;
                msg = "归还失败，请重试！";
            }
            System.out.println(msg);
            return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
        }
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
        int state = 0;
        String msg = "馆内暂时无货,请等待。";
        //先判断该用户账户所剩借阅次数
        Integer residue =Integer.parseInt(user.getResidue());
        if(residue<1){
            //不能借书
            msg="借阅失败！您的借阅数量已达上限！";
            return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
        }
        String readerID=user.getId();
        //获取当前时间
        // 创建一个SimpleDateFormat对象，指定日期和时间的模式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间的Date对象
        Date now = new Date();
        // 使用format方法将Date对象转换为字符串
        String borrowTime = sdf.format(now);
        //判断借出数是否小于书本总数
        if (bookService.check(bookID) == 1) {
            state = borrowServiceImpl.check(readerID);
            Reader reader = readerServiceImpl.findReader(readerID);
            if (reader != null) {
                if (state == 1) {
                    state = borrowServiceImpl.addBorrow(readerID, bookID, borrowTime);
                    msg = "借阅成功！";
                    //将这本书的剩余数量bookLend-1
                    //获取书本信息
                    Book byId = bookService.findById(bookID);
                    //给借出数量+1
                    byId.setBookLend(byId.getBookLend()+1);
                    //更新书本
                    bookService.updateBook(byId);
                    //借阅成功应将用户的最大借阅次数-1
                    residue--;
                    //更新用户信息
                    reader.setResidue(residue.toString());
                    readerServiceImpl.updateReader(reader);
                } else {
                    msg = "达到最大借书数量！请尽快归还！";
                }
            }
        }
        //System.out.println("ReaderController -> registerReader(49): " + msg);
        return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
    }
}
