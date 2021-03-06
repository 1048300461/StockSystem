package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.Order;
import com.example.stocksystem.dao.OrderDao;
import com.example.stocksystem.util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author:zc
 * created on:2020/4/21 17:16
 * description:
 */
public class OrderDaoImpl implements OrderDao {
    @Override
    public boolean insertOrder(Order order) {
        boolean result = false;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(order.getCreate_date());
            String sql = "insert into orders values(?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,queryOrdersMax()+1);
            pstmt.setString(2,dateString);
            pstmt.setInt(3,order.getUser_id());
            pstmt.setInt(4,order.getStock_id());
            pstmt.setInt(5,order.getType());
            pstmt.setDouble(6,order.getPrice());
            pstmt.setInt(7,order.getUndealed());
            pstmt.setInt(8,order.getDealed());
            pstmt.setInt(9,order.getCanceled());
            pstmt.setInt(10,order.getTemp());
            int res = pstmt.executeUpdate();
            if (res > 0)
            {
                result = true;
            }
            pstmt.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public boolean deleteOrder(int orderId) {
        return false;
    }

    /**
     *查询orders表中的行数，便于生成order_id
     * @return
     */
    private static int queryOrdersMax() {
        int Count = 0;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select MAX(order_id) from orders;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                Count =rs.getInt(1);
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return Count;
    }

    @Override
    public List<Order> findOneOrderByUserIdAndStockId(int userId, int stockId){
    List<Order> lists = new ArrayList<>();
        try{
        Connection conn = DataBaseUtil.getSQLConnection();
        String sql = "select * from orders where user_id="+userId+"and stock_id ="+stockId+";";
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
    public boolean CancelOrder(int order_id) {
        boolean result = false;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "update orders set canceled=1 where order_id="+order_id;
            Statement statement = conn.createStatement();
            int i = 0;
            i = statement.executeUpdate(sql);
            if (i!=0)
                result = true;
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
