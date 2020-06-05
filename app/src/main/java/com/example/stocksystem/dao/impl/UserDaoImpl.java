package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.User;
import com.example.stocksystem.dao.UserDao;
import com.example.stocksystem.util.DataBaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;


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
        User user = new User();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from users where user_id='"+userId+"'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
               user.setUser_id(userId);
               user.setLogin_name(rs.getString("login_name"));
                //不能直接调用rs.getDate();这样只能获取yyyy-MM-dd
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               Date date = formatter.parse(rs.getString("create_date"));
               user.setCreate_date(date);
               user.setName(rs.getString("name"));
               user.setPasswd("");
               user.setType(rs.getInt("type"));
               user.setCny_free(rs.getDouble("cny_free"));
               user.setCny_freezed(rs.getDouble("cny_freezed"));
               user.setTemp(rs.getInt("temp"));
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
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
