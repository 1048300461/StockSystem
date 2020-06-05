package com.example.stocksystem.dao.impl;

import com.example.stocksystem.bean.Transaction;
import com.example.stocksystem.dao.TransactionDao;
import com.example.stocksystem.util.DataBaseUtil;

import java.sql.Connection;
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
public class TransactionDaoImpl implements TransactionDao {
    @Override
    public boolean insertTransaction(Transaction transaction) {
        return false;
    }

    @Override
    public boolean deleteTransaction(int transId) {
        return false;
    }

    @Override
    public List<Transaction> queryTransactionBybuyOrderId(int buy_orderId) {
        List<Transaction> lists = new ArrayList<>();
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select * from transactions where buy_order_id="+buy_orderId;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                Transaction transaction = new Transaction();
                transaction.setTrans_id(rs.getInt("trans_id"));

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = formatter.parse(rs.getString("create_time"));
                transaction.setCreate_time(date);
                transaction.setBuy_order_id(buy_orderId);
                transaction.setSell_order_id(rs.getInt("sell_order_id"));
                transaction.setDealed(rs.getInt("dealed"));
                transaction.setPrice(rs.getDouble("price"));
                transaction.setTemp(rs.getInt("temp"));
                transaction.setStock_id(rs.getInt("stock_id"));
                lists.add(transaction);
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
    public Double queryNewestPriceByStockId(int stock_id) {
        Double result = null;
        try{
            Connection conn = DataBaseUtil.getSQLConnection();
            String sql = "select TOP(1) price from transactions where stock_id="+stock_id+" order by trans_id desc";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                result = rs.getDouble("price");
            }

            rs.close();
            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
