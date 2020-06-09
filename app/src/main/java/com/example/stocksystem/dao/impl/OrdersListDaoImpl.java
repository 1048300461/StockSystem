package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.Order;
import com.example.stocksystem.dao.OrdersListDao;
import com.example.stocksystem.util.DataBaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdersListDaoImpl implements OrdersListDao {
    @Override
    public List<Order> AllOrders() {
        List<Order> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from orders where undealed!=0 and canceled!=1";
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
            String sql = "select * from orders where user_id="+userId+"and undealed=0";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                Order order = new Order();
                order.setOrder_id(rs.getInt("order_id"));
                order.setStock_id(rs.getInt("stock_id"));
                //不能直接调用rs.getDate();这样只能获取yyyy-MM-dd
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = formatter.parse(rs.getString("create_date"));
                order.setCreate_date(date);

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
    public List<Order> queryOrdersByStockIdAndType(int stockId,int type) {
        List<Order> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from orders where stock_id="+stockId+" and type="+type+"and dealed!=0";
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
