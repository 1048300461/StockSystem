package com.example.stocksystem.dao;

import com.example.stocksystem.bean.Transaction;

import java.util.List;

/**
 * author:zc
 * created on:2020/4/21 16:48
 * description: 数据库表transactions的操作
 */
public interface TransactionDao {
    /**
     * 插入一条transaction记录
     * @param transaction 需要插入的记录
     * @return 插入结果
     */
    boolean insertTransaction(Transaction transaction);

    /**
     * 删除一条transaction记录
     * @param transId 删除的transId
     * @return 删除的结果
     */
    boolean deleteTransaction(int transId);

    /**
     *查询transaction表，根据买订单id
     * @param buy_orderId
     * @return
     */
    List<Transaction> queryTransactionBybuyOrderId(int buy_orderId);

    /**
     * 查询最近最新某股票的成交价
     * @param stock_id
     * @return
     */
    Double queryNewestPriceByStockId(int stock_id);
}
