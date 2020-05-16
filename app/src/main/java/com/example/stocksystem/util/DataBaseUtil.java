package com.example.stocksystem.util;
import com.example.stocksystem.bean.User;
import com.example.stocksystem.dao.LoginOrSignInDao;
import com.example.stocksystem.dao.impl.LoginOrSignInDaoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


/**
 * 连接 sql server 2005
 * @author zc
 * @date 2020/4/14
 */
public class DataBaseUtil {
    /**
     * 获得connection对象
     * @param ip 主机ip
     * @param user 用户名
     * @param pwd 密码
     * @param db 数据库名
     * @return connection对象
     */
    private static String IP="192.168.179.1";
    private static String USER="sa";
    private static String PASSWORD="123456";
    private static String DB="dbo";
    
    public static Connection getSQLConnection()
    {

        Connection con = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
              con = DriverManager.getConnection("jdbc:jtds:sqlserver://" + IP + ":1433/" + DB + ";useunicode=true;characterEncoding=UTF-8", USER, PASSWORD);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * 测试连接
     * @return
     */
    public static String testSQL()
    {
        String result = "条码区域  -  剩余条码数\n";
        try
        {
            Connection conn = getSQLConnection();
            String sql = "select name from users";
            Statement stmt = conn.createStatement();//
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {//
                String s1 = rs.getString("name");
                result += s1 + "\n";
                System.out.println(s1);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
            result += "查询数据异常!" + e.getMessage();
        }
        return result;
    }

    public static void main(String[] args)
    {
        testSQL();


    }

}

