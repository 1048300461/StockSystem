package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.Order;
import com.example.stocksystem.dao.OrdersListDao;
import com.example.stocksystem.util.DataBaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrdersListDaoImpl implements OrdersListDao {
    @Override
    public List<Order> AllOrders() {
        List<Order> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from orders";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                Order order = new Order();
                order.setOrder_id(rs.getInt("order_id"));
                order.setStock_id(rs.getInt("stock_id"));
                order.setCreate_date(rs.getDate("create_date"));
                order.setUser_id(rs.getInt("user_id"));
                order.setType(rs.getInt("type"));
                order.setPrice(rs.getDouble("price"));
                order.setUndealed(rs.getInt("undealed"));
                order.setDealed(rs.getInt("dealed"));
                order.setCanceled(rs.getInt("canceled"));
                order.setTemp(rs.getInt("temp"));
                lists.add(order);
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return lists;
    }

    @Override
    public List<Order> queryOrdersByUser(int userId) {

        List<Order> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from orders where user_id="+userId;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                Order order = new Order();
                order.setOrder_id(rs.getInt("order_id"));
                order.setStock_id(rs.getInt("stock_id"));
                order.setCreate_date(rs.getDate("create_date"));
                order.setUser_id(rs.getInt("user_id"));
                order.setType(rs.getInt("type"));
                order.setPrice(rs.getDouble("price"));
                order.setUndealed(rs.getInt("undealed"));
                order.setDealed(rs.getInt("dealed"));
                order.setCanceled(rs.getInt("canceled"));
                order.setTemp(rs.getInt("temp"));
                lists.add(order);
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return lists;
    }

    @Override
    public List<Order> queryOrdersByStockId(int stockId) {
        List<Order> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from orders where stock_id="+stockId;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                Order order = new Order();
                order.setOrder_id(rs.getInt("order_id"));
                order.setStock_id(rs.getInt("stock_id"));
                order.setCreate_date(rs.getDate("create_date"));
                order.setUser_id(rs.getInt("user_id"));
                order.setType(rs.getInt("type"));
                order.setPrice(rs.getDouble("price"));
                order.setUndealed(rs.getInt("undealed"));
                order.setDealed(rs.getInt("dealed"));
                order.setCanceled(rs.getInt("canceled"));
                order.setTemp(rs.getInt("temp"));
                lists.add(order);
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return lists;
    }
}
