package com.yangzhuo.book_management.controller;

import com.yangzhuo.book_management.entity.Book;
import com.yangzhuo.book_management.entity.Reader;
import com.yangzhuo.book_management.service.BookService;
import com.yangzhuo.book_management.service.ReaderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ReaderController {
    @Resource
    ReaderService readerServiceImpl;
    @Resource
    BookService bookService;

    @RequestMapping("/reader")
    public String toIndex(HttpSession session, Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        return "reader/index";
    }

    @RequestMapping("/toRegister")
    public String toRegister() {
        return "register";
    }

    @RequestMapping("/RegisterReader")
    @ResponseBody
    public String registerReader(Reader reader) {
        System.out.println("reader.getIdentityId()"+reader.getIdentityId());
        int state = 0;
        String msg = "用户名已存在！";
        //设置初始可借阅书本数量
        Integer identityId = Integer.parseInt(reader.getIdentityId());
        //设置账户初始金额
        switch (identityId){
            case 1:reader.setResidue("5");
            case 2:reader.setResidue("10");
            case 3:reader.setResidue("8");
            case 4:reader.setResidue("6");
        }
        reader.setAccount("0");
        if (!readerServiceImpl.haveReader(reader.getId())) {
            state = readerServiceImpl.addReader(reader);
            if (state == 1) {
                msg = "注册成功！";//注册成功！
            } else {
                msg = "请重试！";//请重试！
            }
        }
        //System.out.println("ReaderController -> registerReader(49): " + msg);
        return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
    }

    @RequestMapping("/admin/toReaderList")
    public String toReaderList(String name, String id, Model model) {
        List<Reader> readerList = readerServiceImpl.getReaderList(name, id);
        model.addAttribute("readerList", readerList);
        return "admin/readerList";
    }
    @RequestMapping("/reader/toBookshelf")
    public String toBookshelf(String bookName, String bookID,HttpSession session, Model model) {
        List<Book> bookList = bookService.getBookList(bookName, bookID);
        model.addAttribute("bookList", bookList);
        Boolean allow = (Boolean) session.getAttribute("allow");
        model.addAttribute("allow", allow);
        return "reader/bookList";
    }

    @RequestMapping("/reader/toBooks")
    public String toBooks(String categoryID,String bookName, String bookID,HttpSession session, Model model) {
        System.out.println("------------------------------------");
        System.out.println(categoryID);
            List<Book> bookList = bookService.getBookListByTypeId(categoryID);
            model.addAttribute("bookList", bookList);
            Boolean allow = (Boolean) session.getAttribute("allow");
            model.addAttribute("allow", allow);
        return "reader/bookList";
    }

    @RequestMapping("/UpdateReader")
    @ResponseBody
    public String updateReader(String id, String identityId,String residue,String name, String password, String oldPassword, String gender, String telephone, String email,String account) {
        Reader reader = new Reader(id,identityId,residue, name, password == "" ? oldPassword : password, gender, telephone, email,account);
        int state = readerServiceImpl.updateReader(reader);
        String msg = "请重试";
        if (state == 1) {
            msg = "更新成功!";
        }
        return "{\"state\":" + state + ",\"msg\":\"" + msg + "\"}";
    }

    @RequestMapping("/toEditReader")
    public String toEditReader(HttpSession session, Model model) {
        Reader reader = (Reader) session.getAttribute("user");
        model.addAttribute("user", reader);
        return "reader/edit";
    }

    @RequestMapping("/admin/toEditReader")
    public String toEditReader(String id, Model model) {
        Reader reader = readerServiceImpl.findReader(id);
        model.addAttribute("user", reader);
        return "admin/editReader";
    }

    @RequestMapping("/admin/DeleteReader")
    public String deleteReader(String id) {
        int raw = readerServiceImpl.deleteAdminById(id);
        return "redirect:/admin/";
    }

}
