package com.example.stocksystem.bean;

import java.util.Date;

/**
 * author:zc
 * created on:2020/4/21 15:35
 * description: 数据库 users表
 */
public class User {
    /**
     * 用户id
     */
    private int user_id;
    /**
     * 用户创建日期
     */
    private Date create_date;

    /**
     * 用户名字
     */
    private String name;
    /**
     * 用户登录名
     */
    private String login_name;
    /**
     * 用户密码
     */
    private String passwd;
    /**
     * 用户类型
     * 0：普通用户
     * 1：系统管理员
     */
    private int type;
    /**
     * 可用人民币数量
     */
    private double cny_free;
    /**
     * 冻结人民币数量
     */
    private double cny_freezed;
    /**
     * 保留属性
     */
    private int temp;

    public User(){
        this.create_date = new Date(System.currentTimeMillis());
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getCny_free() {
        return cny_free;
    }

    public void setCny_free(double cny_free) {
        this.cny_free = cny_free;
    }

    public double getCny_freezed() {
        return cny_freezed;
    }

    public void setCny_freezed(double cny_freezed) {
        this.cny_freezed = cny_freezed;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", create_date=" + create_date +
                ", name='" + name + '\'' +
                ", login_name='" + login_name + '\'' +
                ", passwd='" + passwd + '\'' +
                ", type=" + type +
                ", cny_free=" + cny_free +
                ", cny_freezed=" + cny_freezed +
                ", temp=" + temp +
                '}';
    }
}
