package com.example.stocksystem.dao;

import com.example.stocksystem.bean.Stock;

/**
 * author:zc
 * created on:2020/4/21 16:47
 * description: 数据库表Stock的操作
 */
public interface StockDao {
    /**
     * 插入一条stock记录
     * @param stock 要插入的数据
     * @return 插入结果
     */
    boolean insertStock(Stock stock);

    /**
     * 通过stockId删除一条stock记录
     * @param stockId 股票id
     * @return 删除结果
     */
    boolean deleteStockByStockId(int stockId);

    /**
     * 查找指定一条股票数据
     * @param stockId 股票id
     * @param stockName 股票名字
     * @return 查找结果
     */
    Stock findOneStock(int stockId, String stockName);
}
