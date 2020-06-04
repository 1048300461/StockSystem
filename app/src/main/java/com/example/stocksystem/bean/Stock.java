package com.example.stocksystem.bean;

/**
 * author:zc
 * created on:2020/4/21 15:36
 * description: 数据库 stocks表
 */
public class Stock {
    /**
     * 股票id
     */
    private int stock_id;
    /**
     * 股票名字
     */
    private String name;
    /**
     * 标记时属于哪个股市
     */
    private int type;

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stock_id=" + stock_id +
                ", name='" + name + '\'' +
                ", temp=" + type +
                '}';
    }

    public void setType(int type) {
        this.type = type;
    }

}
