package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.Order;
import com.example.stocksystem.dao.OrderDao;

/**
 * author:zc
 * created on:2020/4/21 17:16
 * description:
 */
public class OrderDaoImpl implements OrderDao {
    @Override
    public boolean insertOrder(Order order) {
        return false;
    }

    @Override
    public boolean deleteOrder(int orderId) {
        return false;
    }

    @Override
    public Order findOneOrderByUserIdAndStockId(int userId, int stockId) {
        return null;
    }
}
