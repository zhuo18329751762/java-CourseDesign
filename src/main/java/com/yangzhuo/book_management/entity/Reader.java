package com.yangzhuo.book_management.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @program: library
 * @description: 读者实体类
 * @author:
 * @create: 2023-07-03 11:06
 **/
public class Reader {
    //id
    @TableId(type = IdType.INPUT)
    private String id;
    //身份id
    private String identityId;
    //剩余可借阅书本数量
    private String residue;
    //名称
    private String name;
    //密码
    private String password;
    //性别
    private String gender;
    //电话号码
    private String telephone;
    //邮箱地址
    private String email;
    //账户剩余金额
    private String account;

    public Reader() {
    }

    public Reader(String id, String identityId, String residue, String name, String password, String gender, String telephone, String email, String account) {
        this.id = id;
        this.identityId = identityId;
        this.residue = residue;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.telephone = telephone;
        this.email = email;
        this.account = account;
    }

    /**
     * 获取
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取
     * @return identityId
     */
    public String getIdentityId() {
        return identityId;
    }

    /**
     * 设置
     * @param identityId
     */
    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    /**
     * 获取
     * @return residue
     */
    public String getResidue() {
        return residue;
    }

    /**
     * 设置
     * @param residue
     */
    public void setResidue(String residue) {
        this.residue = residue;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 获取
     * @return telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     * @return account
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    public String toString() {
        return "Reader{id = " + id + ", identityId = " + identityId + ", residue = " + residue + ", name = " + name + ", password = " + password + ", gender = " + gender + ", telephone = " + telephone + ", email = " + email + ", account = " + account + "}";
    }
}
