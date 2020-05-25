package com.example.stocksystem.OrderShow;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocksystem.R;
import com.example.stocksystem.bean.Order;
import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.dao.OrdersListDao;
import com.example.stocksystem.dao.StockDao;
import com.example.stocksystem.dao.UserDao;
import com.example.stocksystem.dao.impl.OrdersListDaoImpl;
import com.example.stocksystem.dao.impl.StockDaoImpl;
import com.example.stocksystem.dao.impl.UserDaoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersListActivity extends AppCompatActivity {
    public static String username = "wang";    //用户名--->admin为管理员

    private OrdersList_ListView_Adapter lvAdapter;//listView的自定义配置器
    private List<Order> orderList;  //数据库中数据
    private Map<String,String> stockNameMap;

    private ProgressDialog progressDialog;
    private TextView tv_username;
    private ListView listView;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_list);

        tv_username = findViewById(R.id.ordersList_textView_userName);
        listView = findViewById(R.id.orderList_listView_ShowTotal);

        tv_username.setText(username);
        OrdersListAsyncTask ordersListAT = new OrdersListAsyncTask();       //访问数据库的耗时操作
        ordersListAT.execute();


    }

    //根据用户名查询id
    private int getUserId(){
        UserDao userDao = new UserDaoImpl();
        return userDao.findOneUserByUserName(username);
    }

    //访问数据库获取数据
    private void getOrders(){
        setStockNameMap();
        OrdersListDao dao = new OrdersListDaoImpl();
        if (username.equals("admin"))       //管理员可以查询所有人的信息
        {
            orderList = dao.AllOrders();
        }else
        {
            orderList = dao.queryOrdersByUser(getUserId());
        }
    }
    //构建股票名称和编号索引
    public void setStockNameMap(){
        StockDao stockDao = new StockDaoImpl();
        List<Stock> stockList = stockDao.queryAllStock();       //数据库中查询表的操作
        for (Stock s: stockList) {
            stockNameMap = new HashMap<String, String>();
            stockNameMap.put(s.getStock_id()+"",s.getName());
        }
    }

    //异步任务方法-->访问数据的耗时操作
    private class OrdersListAsyncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(OrdersListActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("加载数据中。。。");
            progressDialog.setIndeterminate(true);  //设置ProgressDialog 的进度条是否不明确
            progressDialog.setCancelable(false); //设置ProgressDialog 是否可以按退回按键取消
            progressDialog.show();
        }

        /**
         * doInBackground方法内部执行后台任务，不可在此方法中更新主UI
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            getOrders();
            return null;
        }

        /**
         * onProgressUpdata方法用于更新进度信息
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            initView();
            progressDialog.cancel();
            super.onPostExecute(s);
        }

        /**
         * onCancelled()方法用于在取消执行中任务时更改UI
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }
    //更新UI，加载数据
    private void initView(){
        lvAdapter = new OrdersList_ListView_Adapter(OrdersListActivity.this,orderList,stockNameMap);
        listView.setAdapter(lvAdapter);
        lvAdapter.notifyDataSetChanged();
    }

    public void finishThis(View view) {
        finish();
    }
}
