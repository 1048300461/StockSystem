package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.User;
import com.example.stocksystem.dao.UserDao;
import com.example.stocksystem.util.DataBaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * author:zc
 * created on:2020/4/21 16:18
 * description:
 */
public class UserDaoImpl implements UserDao {
    @Override
    public boolean insertUser(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(int userId) {
        return false;
    }

    @Override
    public User findOneUser(int userId) {
        return null;
    }

    @Override
    public int findOneUserByUserName(String username) {
        int userId = 0;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from users where login_name='"+username+"'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                userId = rs.getInt("user_id");
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return userId;
    }

    @Override
    public double QueryFreeMoneyByUserName(String username){
        double free_money=0;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from users where login_name='"+username+"'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                free_money = rs.getDouble("cny_free");
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  free_money;
    }
}
