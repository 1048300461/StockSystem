package com.example.stocksystem.dao;

import com.example.stocksystem.bean.Order;

import java.util.List;

/**
 * Date:2020-5-17
 * Author:wx
 * Description:对Orders表中数据的查询-->用于显示买卖成功订单
 */

public interface OrdersListDao {
    /**
     * 查询orders表中所有的数据
     * @return
     */
    List<Order> AllOrders();

    /**
     * 按照用户名来查询
     * @Parm userId 用户id
     * @return  返回数据以时间先后进行排序
     */
    List<Order> queryOrdersByUser(int userId);

    /**
     *按照股票编号进行查询
     * @Parm stockId
     * @return
     */
    List<Order> queryOrdersByStockId(int stockId);
}
