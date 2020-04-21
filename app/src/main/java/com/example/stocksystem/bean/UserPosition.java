package com.example.stocksystem.bean;

/**
 * author:zc
 * created on:2020/4/21 15:36
 * description: 数据库 user_position表
 */
public class UserPosition {
    /**
     * 用户id
     */
    private int user_id;
    /**
     * 股票id
     */
    private int stock_id;
    /**
     * 拥有可卖股票数量
     */
    private int num_free;
    /**
     * 冻结的股票数量
     */
    private int num_freezed;
    /**
     * 保留属性
     */
    private int temp;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getNum_free() {
        return num_free;
    }

    public void setNum_free(int num_free) {
        this.num_free = num_free;
    }

    public int getNum_freezed() {
        return num_freezed;
    }

    public void setNum_freezed(int num_freezed) {
        this.num_freezed = num_freezed;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "UserPosition{" +
                "user_id=" + user_id +
                ", stock_id=" + stock_id +
                ", num_free=" + num_free +
                ", num_freezed=" + num_freezed +
                ", temp=" + temp +
                '}';
    }
}
