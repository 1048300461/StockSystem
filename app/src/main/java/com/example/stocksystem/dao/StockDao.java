package com.example.stocksystem.dao;

import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.bean.UserPosition;

import java.util.List;

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

    /**
     * 根据Id找到股票名称
     * @param stockId 股票id
     * @return
     */
    Stock findStockById(int stockId);

    /**
     * 根据名字找到股票id
     * @param stockName 股票名字
     * @return
     */
    Stock findStockByName(String stockName);

    /**
     * 查询所有的股票id和名称
     * @return
     */
    List<Stock> queryAllStock();

    /**
     *查询某人所拥有的某股票的数量
     * @param user_id 用户id
     * @return 查询的结果
     */
    List<Stock> queryStockBelongUser(int user_id);

}
