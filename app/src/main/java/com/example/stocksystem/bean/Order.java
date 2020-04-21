package com.example.stocksystem.bean;

import java.util.Date;

/**
 * author:zc
 * created on:2020/4/21 15:36
 * description: 数据库orders表
 */
public class Order {
    /**
     * 订单id
     */
    private int order_id;
    /**
     * 订单创建时间
     */
    private Date create_date;
    /**
     * 用户id
     */
    private int user_id;
    /**
     * 股票id
     */
    private int stock_id;
    /**
     * 订单类型
     * 0：买入
     * 1：卖出
     */
    private int type;
    /**
     * 单价
     */
    private double price;
    /**
     * 未成交数量
     */
    private int undealed;
    /**
     * 成交数量
     */
    private int dealed;
    /**
     * 取消数量
     */
    private int canceled;
    /**
     * 保留属性
     */
    private int temp;

    public Order(){
        this.create_date = new Date(System.currentTimeMillis());
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUndealed() {
        return undealed;
    }

    public void setUndealed(int undealed) {
        this.undealed = undealed;
    }

    public int getDealed() {
        return dealed;
    }

    public void setDealed(int dealed) {
        this.dealed = dealed;
    }

    public int getCanceled() {
        return canceled;
    }

    public void setCanceled(int canceled) {
        this.canceled = canceled;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", create_date=" + create_date +
                ", user_id=" + user_id +
                ", stock_id=" + stock_id +
                ", type=" + type +
                ", price=" + price +
                ", undealed=" + undealed +
                ", dealed=" + dealed +
                ", canceled=" + canceled +
                ", temp=" + temp +
                '}';
    }
}
