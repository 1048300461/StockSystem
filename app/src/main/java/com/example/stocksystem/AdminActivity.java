package com.example.stocksystem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocksystem.OrderShow.UserHoldStockActivity;
import com.example.stocksystem.bean.Order;
import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.dao.OrderDao;
import com.example.stocksystem.dao.StockDao;
import com.example.stocksystem.dao.impl.OrderDaoImpl;
import com.example.stocksystem.dao.impl.StockDaoImpl;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    //控件
    private EditText editText_stockName;
    private EditText editText_stockUndealed;
    private EditText editText_stockPrice;
    private Button btn_go;
    private ListView listView;
    ProgressDialog progressDialog;

    private List<AdminListViewAdapter.AdminAdapterContent> adapterlists = new ArrayList<>();    //用于listView加载数据
    private List<Stock> stockList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();
    private String stockName = "";
    private int stockUndealed = 0;
    private double stockPrice = 0;
    private boolean IsInsert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        //列表显示
        getSql();

        //控件初始化
        editText_stockName = findViewById(R.id.admin_stockName);
        editText_stockUndealed = findViewById(R.id.admin_stockUnDealed);
         editText_stockPrice = findViewById(R.id.admin_stockPrice);
         btn_go = findViewById(R.id.admin_btn_ok);
         listView = findViewById(R.id.admin_listView);

         btn_go.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (editText_stockName.getText().toString().equals("") || editText_stockPrice.getText().equals("")
                 || editText_stockPrice.getText().equals("0") || editText_stockUndealed.equals("")||editText_stockUndealed.equals("0")){
                     Toast.makeText(AdminActivity.this,"输入不合法！！！",Toast.LENGTH_SHORT).show();
                 }else {
                     stockName = editText_stockName.getText().toString();
                     stockPrice  = Double.parseDouble(editText_stockPrice.getText().toString());
                     stockUndealed = Integer.parseInt(editText_stockUndealed.getText().toString());
                     WriteStockAcsncTask writeTask = new WriteStockAcsncTask();
                     writeTask.execute();
                 }
             }
         });


    }

    public void finishThis(View view) {
        finish();
    }

    //刷新数据
    public void refresh(View view) {
        getSql();
    }

    //调用写入代码
    private void getSql()
    {
        GetStockAcsncTask task = new GetStockAcsncTask();
        task.execute();
    }
    //初始化数据
    private void initListView(){
        AdminListViewAdapter adapter = new AdminListViewAdapter(AdminActivity.this,adapterlists);
        listView.setAdapter(adapter);
    }

    //加载数据库---》获得显示数据
    private class GetStockAcsncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AdminActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("加载数据中。。。");
            progressDialog.setIndeterminate(true);  //设置ProgressDialog 的进度条是否不明确
            progressDialog.setCancelable(false); //设置ProgressDialog 是否可以按退回按键取消
            progressDialog.show();
            adapterlists.clear();
        }

        /**
         * doInBackground方法内部执行后台任务，不可在此方法中更新主UI
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            StockDao stockDao = new StockDaoImpl();
            OrderDao orderDao = new OrderDaoImpl();
            stockList = stockDao.queryAllStock();
            for (Stock stock : stockList)
            {
                orderList = orderDao.findOneOrderByUserIdAndStockId(10000001,stock.getStock_id());//    10000001为admin
                for (Order order: orderList)
                {
                    AdminListViewAdapter.AdminAdapterContent apc = new AdminListViewAdapter.AdminAdapterContent();
                    apc.setStockName(stock.getName());
                    apc.setAdminNum(order.getUndealed());
                    apc.setUserNum(order.getDealed());
                    adapterlists.add(apc);
                }
            }
            return null;
        }
        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            initListView();
            progressDialog.cancel();

            super.onPostExecute(s);
        }
    }

    //访问数据库---》写入股票信息
    private class WriteStockAcsncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AdminActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("加载数据中。。。");
            progressDialog.setIndeterminate(true);  //设置ProgressDialog 的进度条是否不明确
            progressDialog.setCancelable(false); //设置ProgressDialog 是否可以按退回按键取消
            progressDialog.show();
            adapterlists.clear();
        }

        /**
         * doInBackground方法内部执行后台任务，不可在此方法中更新主UI
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            StockDao stockDao = new StockDaoImpl();
            OrderDao orderDao = new OrderDaoImpl();
            if (!stockName.equals("") && stockPrice!=0 && stockUndealed!=0)
            {
                Stock stock = new Stock();
                stock.setName(stockName);
                stock.setStock_id(StockDaoImpl.getStockId());
                stockDao.insertStock(stock);
                Order order = new Order();
                order.setUser_id(10000001);
                order.setStock_id(stock.getStock_id());
                order.setType(1);       //0为买入
                order.setPrice(stockPrice);
                order.setUndealed(stockUndealed);
                order.setDealed(0);
                order.setCanceled(0);
                order.setTemp(0);
                IsInsert = orderDao.insertOrder(order);
            }

            return null;
        }
        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            progressDialog.dismiss();
            getSql();
            super.onPostExecute(s);
        }
    }


}
