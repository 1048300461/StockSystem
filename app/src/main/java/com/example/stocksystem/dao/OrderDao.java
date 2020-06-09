package com.example.stocksystem.dao;

import com.example.stocksystem.bean.Order;

import java.util.List;

/**
 * author:zc
 * created on:2020/4/21 16:19
 * description: 数据库表orders的操作
 */
public interface OrderDao {
    /**
     * 插入一条order记录-->order_id和create_date由插入语句自动生成
     * @param order 插入的数据
     * @return 插入的结果
     */
    boolean insertOrder(Order order);

    /**
     * 删除一条order记录
     * @param orderId 删除的orderId
     * @return 删除的结果
     */
    boolean deleteOrder(int orderId);

    /**
     * 通过userId和stockId查找指定一条记录
     * @param userId 用户id
     * @param stockId 股票id
     * @return 查找结果
     */
    List<Order> findOneOrderByUserIdAndStockId(int userId, int stockId);

    /**
     *取消订单
     * @param order_id 订单id
     * @return
     */
    boolean CancelOrder(int order_id);

}
