package com.example.stocksystem.bean;

import java.util.Date;

/**
 * author:zc
 * created on:2020/4/21 15:36
 * description: 数据库transactions表
 */
public class Transaction {
    /**
     * 成交id
     */
    private int trans_id;
    /**
     * 成交日期
     */
    private Date create_time;
    /**
     * 交易的股票代码
     */
    private int stock_id;
    /**
     * 买入订单id
     */
    private int buy_order_id;
    /**
     * 卖出订单id
     */
    private int sell_order_id;
    /**
     * 成交数量
     */
    private int dealed;
    /**
     * 成交单价
     */
    private double price;
    private int temp;

    public Transaction(){
        this.create_time = new Date(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "trans_id=" + trans_id +
                ", create_time=" + create_time +
                ", stock_id=" + stock_id +
                ", buy_order_id=" + buy_order_id +
                ", sell_order_id=" + sell_order_id +
                ", dealed=" + dealed +
                ", price=" + price +
                ", temp=" + temp +
                '}';
    }

    public int getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(int trans_id) {
        this.trans_id = trans_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getBuy_order_id() {
        return buy_order_id;
    }

    public void setBuy_order_id(int buy_order_id) {
        this.buy_order_id = buy_order_id;
    }

    public int getSell_order_id() {
        return sell_order_id;
    }

    public void setSell_order_id(int sell_order_id) {
        this.sell_order_id = sell_order_id;
    }

    public int getDealed() {
        return dealed;
    }

    public void setDealed(int dealed) {
        this.dealed = dealed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
