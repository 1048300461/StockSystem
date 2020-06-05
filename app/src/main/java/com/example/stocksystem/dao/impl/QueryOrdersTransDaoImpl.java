package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.Order;
import com.example.stocksystem.dao.OrderDao;
import com.example.stocksystem.util.DataBaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 服务于UserHoldStockActivity
 * 避免冲突
 */
public class QueryOrdersTransDaoImpl implements OrderDao {

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

    @Override
    public boolean CancelOrder(int order_id) {
        return false;
    }

    public List<Order> findOneOrderByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from orders where user_id="+userId +"and type=0 and canceled = 0";    //买订单的订单信息
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
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
                orders.add(order);
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return orders;
    }

}
