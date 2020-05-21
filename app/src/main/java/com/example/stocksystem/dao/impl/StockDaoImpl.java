package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.Stock;
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
    @Override
    public boolean insertStock(Stock stock) {
        return false;
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
        Stock stock = new Stock();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from stock where stock_id=" + stockId;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                stock.setName(rs.getString("name"));
                stock.setStock_id(rs.getInt("stock_id"));
                stock.setTemp(rs.getInt("temp"));
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
                stock.setTemp(rs.getInt("temp"));
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
}
