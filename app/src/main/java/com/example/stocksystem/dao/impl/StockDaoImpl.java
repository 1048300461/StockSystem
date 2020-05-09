package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.dao.StockDao;

/**
 * author:zc
 * created on:2020/4/21 17:17
 * description:
 */
public class StockDaoImpl implements StockDao {
    @Override
    public boolean insertStock(Stock stock) {
        return false;
    }

    @Override
    public boolean deleteStockByStockId(int stockId) {
        return false;
    }

    @Override
    public Stock findOneStock(int stockId, String stockName) {
        return null;
    }
}
