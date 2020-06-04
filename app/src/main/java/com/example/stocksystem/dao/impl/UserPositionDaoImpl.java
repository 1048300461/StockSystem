package com.example.stocksystem.dao.impl;

import android.util.Log;

import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.bean.UserPosition;
import com.example.stocksystem.dao.UserPositionDao;
import com.example.stocksystem.util.DataBaseUtil;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * author:zc
 * created on:2020/4/21 17:17
 * description:
 */
public class UserPositionDaoImpl implements UserPositionDao {
    @Override
    public UserPosition findOneUerPosition(int userId, int stockId) {
        return null;
    }

    @Override
    public boolean insertUserPosition(UserPosition userPosition) {
        return false;
    }

    @Override
    public boolean deleteUserPosition(int userId, int stockId) {
        return false;
    }

    @Override
    public List<Integer> findUserStockId(int userId) {
        List<Integer> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from user_position where user_id="+userId + " and num_free > 0";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                int stockid = rs.getInt("stock_id");
                lists.add(stockid);
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
