package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.bean.UserPosition;
import com.example.stocksystem.dao.StockDao;
import com.example.stocksystem.util.DataBaseUtil;

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
public class StockDaoImpl implements StockDao {

    public static int getStockId(){
        int count = 0;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select MAX(stock_id) from stock;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
               count = rs.getInt(1)+1;
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public boolean insertStock(Stock stock){
        boolean result = false;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "insert into stock values("+stock.getStock_id()+",'"+stock.getName()+"',0);";
            Statement statement = conn.createStatement();
            int rs = statement.executeUpdate(sql);
            if (rs!=0)
            {
                result = true;
            }
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean deleteStockByStockId(int stockId) {
        return false;
    }

    @Override
    public Stock findOneStock(int stockId, String stockName) {
        return null;
    }


    @Override
    public Stock findStockById(int stockId) {
        Stock stock = null;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from stock where stock_id=" + stockId;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                stock = new Stock();
                stock.setName(rs.getString("name"));
                stock.setStock_id(rs.getInt("stock_id"));
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return stock;
    }

    @Override
    public Stock findStockByName(String stockName) {
        Stock stock = null;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from stock where name='"+stockName+"';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                stock = new Stock();
                stock.setName(rs.getString("name"));
                stock.setStock_id(rs.getInt("stock_id"));
                stock.setType(rs.getInt("type"));
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return stock;
    }

    @Override
    public List<Stock> queryAllStock() {
        List<Stock> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from stock";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                Stock stock = new Stock();
                stock.setName(rs.getString("name"));
                stock.setStock_id(rs.getInt("stock_id"));
                lists.add(stock);
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
    public List<Stock> queryStockBelongUser(int user_id) {
        List<Stock> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select s.name,s.stock_id from user_position as u,stock as s where u.user_id = "+user_id+" and u.num_free!=0 and u.stock_id=s.stock_id";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                Stock stock = new Stock();
                stock.setName(rs.getString("name"));
                stock.setStock_id(rs.getInt("stock_id"));
                stock.setType(rs.getInt("type"));
                lists.add(stock);
            }
            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return lists;
    }

    public List<UserPosition> queryStockBelongUserPosition(int user_id) {
        List<UserPosition> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select  * from user_position as u , stock as s where u.user_id = "+user_id+" and u.num_free!=0 and u.stock_id=s.stock_id";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                UserPosition up = new UserPosition();
                up.setUser_id(rs.getInt("user_id"));
                up.setStock_id(rs.getInt("stock_id"));
                up.setNum_free(rs.getInt("num_free"));
                up.setNum_freezed(rs.getInt("num_freezed"));
                up.setTemp(0);
                lists.add(up);
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
