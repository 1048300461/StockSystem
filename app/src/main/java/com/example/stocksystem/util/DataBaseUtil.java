package com.example.stocksystem.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
    private static Connection getSQLConnection(String ip, String user, String pwd, String db)
    {
        Connection con = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
              con = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":1433/" + db + ";useunicode=true;characterEncoding=UTF-8", user, pwd);
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
            Connection conn = getSQLConnection("192.168.92.145", "sa", "Zc19981221", "gp");
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

//    public static void main(String[] args)
//    {
//        testSQL();
//    }

}

