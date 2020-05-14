package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.User;
import com.example.stocksystem.dao.LoginOrSignInDao;
import com.example.stocksystem.util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;


public class LoginOrSignInDaoImpl implements LoginOrSignInDao {
    @Override
    public boolean IsUser(User user) {

        boolean result = false;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from users where login_name='"+user.getLogin_name()+"';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                result = true;
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public User IsLogin(User user) {
        User u = null;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from users where login_name='" +user.getLogin_name()
                    +"' and passwd='" +user.getPasswd()
                    +"';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                u = user;
                u.setType(rs.getInt("type"));
                //u.setPasswd("");//隐藏返回密码
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public boolean IsSignIn(User user) {
        boolean result = false;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
           //设置时间格式
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(user.getCreate_date());

            String sql2 = "insert into users values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql2);
            pstmt.setInt(1,user.getUser_id());
            pstmt.setString(2,dateString);
            pstmt.setString(3,user.getName());
            pstmt.setString(4,user.getLogin_name());
            pstmt.setString(5,user.getPasswd());
            pstmt.setInt(6,user.getType());
            pstmt.setDouble(7,user.getCny_free());
            pstmt.setDouble(8,user.getCny_freezed());
            pstmt.setInt(9,user.getTemp());
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
    public int countUser() {
        int count = 0;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from users ";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                count ++;
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
}
