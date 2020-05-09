package com.example.stocksystem.dao;

import com.example.stocksystem.bean.Transaction;

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
}
