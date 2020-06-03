package com.example.stocksystem.OrderShow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocksystem.R;
import com.example.stocksystem.bean.Order;
import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.dao.OrderDao;
import com.example.stocksystem.dao.OrdersListDao;
import com.example.stocksystem.dao.StockDao;
import com.example.stocksystem.dao.impl.OrderDaoImpl;
import com.example.stocksystem.dao.impl.OrdersListDaoImpl;
import com.example.stocksystem.dao.impl.StockDaoImpl;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class BuyOrderUserActivity extends AppCompatActivity {

    public static String username = "wang";
    public static int user_id = 10000001;
    //页面控件
    private TextView tv_username;
    private Spinner spinner;        //用于显示选择的股票
    private EditText editText_price;//价格
    private EditText editText_Count;//数量
    private ListView listView_buy ;
    private ListView listView_sell;
    private ProgressDialog progressDialog;
    private Button btn_buy;    //买入
    //private AutoCompleteTextView autoTv_stock;    //显示股票名称和id

    private List<Order> showBuyOrderList = new ArrayList<>();
    private List<Order> showSellOrderList =  new ArrayList<>();
    private boolean IsBuy = false;     //判断是否卖出成功
    private Order order  = new Order();

    //数据库中的股票编号和名称
    private List<Stock> stockList;
    //选中的股票编号
    private int OnItemStockId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_order_user);
        //实例页面控件
        tv_username = findViewById(R.id.buy_order_user_tvUser);
        spinner = findViewById(R.id.buyOrder_spinner_type);
        editText_price = findViewById(R.id.buy_order_user_edPrice);
        editText_Count = findViewById(R.id.buy_order_user_edCount);
        listView_buy = findViewById(R.id.buy_order_lvbuy);
        listView_sell = findViewById(R.id.buy_order_lvSell);
        btn_buy = findViewById(R.id.buy_order_buyOK);
       // autoTv_stock = (AutoCompleteTextView) findViewById(R.id.buyOrder_autoTv_type);

        //显示用户名
        tv_username.setText("("+username+")");

        //buy按钮监听  确定卖出
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.equals("未登录"))
                {
                    if (!editText_price.getText().toString().equals("") && !editText_Count.getText().toString().equals("")
                            &&!editText_Count.getText().equals("0")&&!editText_price.getText().equals("0")
                            && spinner.getSelectedItemPosition()!=0
                    )
                    {
                        BuyOrdersAcsncTask sellTask = new BuyOrdersAcsncTask();
                        order.setUser_id(user_id);
                        order.setStock_id(stockList.get((spinner.getSelectedItemPosition()-1)).getStock_id());
                        order.setType(0);       //0为买入
                        order.setPrice(Double.parseDouble(editText_price.getText().toString()));
                        order.setUndealed(0);
                        order.setDealed(Integer.parseInt(editText_Count.getText().toString()));
                        order.setCanceled(0);
                        order.setTemp(0);
                        sellTask.execute();

                    }else
                    {
                        Toast.makeText(BuyOrderUserActivity.this,"输入错误！",Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(BuyOrderUserActivity.this,"用户未登录！",Toast.LENGTH_SHORT).show();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0)
                {
                    OnItemStockId = stockList.get(i-1).getStock_id();
                    GetBuySellAcsncTask getBuySellAcsncTask = new GetBuySellAcsncTask();
                    getBuySellAcsncTask.execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //最近某股票的买入卖出交易量
        //设置列表监听
        listView_buy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    editText_Count.setText(showBuyOrderList.get(position).getDealed()+"");
                    editText_price.setText(showBuyOrderList.get(position).getPrice()+"");
            }
        });

        listView_sell.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    editText_Count.setText(showSellOrderList.get(position).getDealed()+"");
                    editText_price.setText(showSellOrderList.get(position).getPrice()+"");
            }
        });
        GetStockAcsncTask getStockAcsncTask = new GetStockAcsncTask();
        getStockAcsncTask.execute();
    }

    //输入选择框--->无法显示
    /*
    private void initAutoTextView()
    {
        //选择股票名称
        String []strType= new String[stockList.size()+1];
        for (int i=0;i<stockList.size();i++)
        {
            strType[i] = stockList.get(i).getStock_id()+"--"+stockList.get(i).getName();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,strType);
        autoTv_stock.setAdapter(arrayAdapter);
    }
     */


    //加载下拉框
    private void initSpinner(){
        //下拉框选择用户类型
        final String []strType= new String[stockList.size()+1];
        strType[0] = "请选择股票名称";
        for (int i=0;i<stockList.size();i++)
        {
            strType[i+1] = stockList.get(i).getStock_id()+"--"+stockList.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.login_simple_spinner_item,strType);
        adapter.setDropDownViewResource(R.layout.login_simple_spinner_item);
        spinner.setAdapter(adapter);
    }

    private void initViewListView(){
        //买入
        buyOrder_User_ListView_Adapter buyLVAdapter = new buyOrder_User_ListView_Adapter(BuyOrderUserActivity.this,showBuyOrderList);
        listView_buy.setAdapter(buyLVAdapter);
        buyLVAdapter.notifyDataSetChanged();
        //卖出
        buyOrder_User_ListView_Adapter sellLVAdapter = new buyOrder_User_ListView_Adapter(BuyOrderUserActivity.this,showSellOrderList);
        listView_sell.setAdapter(sellLVAdapter);
        sellLVAdapter.notifyDataSetChanged();

    }

    public void finishThis(View view) {
        finish();
    }

    //加减按钮的事件---》价格
    private DecimalFormat df   = new DecimalFormat("######0.00");//保留两位小数
    public void subPrice(View view) {

        if (!editText_price.getText().toString().equals("0") && !editText_price.getText().toString().equals(""))
        {
            double double1 = Double.parseDouble(editText_price.getText()+"");
            double1 = double1-0.01;
            editText_price.setText(df.format(double1));
        }
    }

    public void addPrice(View view) {
        if(!editText_price.getText().toString().equals(""))
        {
            double double1 = Double.parseDouble(editText_price.getText()+"");
            double1 = double1 + 0.01;
            editText_price.setText(df.format(double1));
        }

    }
    //--》数量
    public void subCount(View view) {
        if (!editText_Count.getText().toString().equals("0") && !editText_Count.getText().toString().equals(""))
        {
            int int1 = Integer.parseInt(editText_Count.getText()+"");
            int1 = int1-1;
            editText_Count.setText(int1 + "");
        }
    }

    public void addCount(View view) {
        if (!editText_Count.getText().toString().equals("")) {
            int int1 = Integer.parseInt(editText_Count.getText() + "");
            int1 = int1 + 1;
            editText_Count.setText(int1 + "");
        }
    }

    //访问数据库--》向orders表中插入买订单
    private class BuyOrdersAcsncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(BuyOrderUserActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("加载数据中。。。");
            progressDialog.setIndeterminate(true);  //设置ProgressDialog 的进度条是否不明确
            progressDialog.setCancelable(false); //设置ProgressDialog 是否可以按退回按键取消
            progressDialog.show();
        }

        /**
         * doInBackground方法内部执行后台任务，不可在此方法中更新主UI
         * order 需要写入的数据
         * @param orders
         * @return
         */
        @Override
        protected String doInBackground(String... orders) {
            OrderDao orderDao = new OrderDaoImpl();

            IsBuy =  orderDao.insertOrder(order);
            return null;
        }
        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();
            if (IsBuy)
                Toast.makeText(BuyOrderUserActivity.this,"卖出成功！",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(BuyOrderUserActivity.this,"卖出失败！",Toast.LENGTH_LONG).show();
            initSpinner();
            //initAutoTextView();
        }

    }

    //访问数据库--》得到股票编号和名称
    private class GetStockAcsncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(BuyOrderUserActivity.this);
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
            StockDao stockDao = new StockDaoImpl();
            stockList= stockDao.queryAllStock();
            return null;
        }
        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();
            initSpinner();
        }

    }

    //访问数据库---》得到某股票的近期交易信息
    private class GetBuySellAcsncTask extends AsyncTask<Integer,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(BuyOrderUserActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("加载数据中。。。");
            progressDialog.setIndeterminate(true);  //设置ProgressDialog 的进度条是否不明确
            progressDialog.setCancelable(false); //设置ProgressDialog 是否可以按退回按键取消
            progressDialog.show();
            showBuyOrderList.clear();
            showSellOrderList.clear();
        }

        /**
         * doInBackground方法内部执行后台任务，不可在此方法中更新主UI
         * @param StrockId
         * @return
         */
        @Override
        protected String doInBackground(Integer... StrockId) {
            OrdersListDao listDao = new OrdersListDaoImpl();
            List<Order> sqlBuyOrders = listDao.queryOrdersByStockIdAndType(OnItemStockId,0);//买入
            List<Order> sqlSellOrders = listDao.queryOrdersByStockIdAndType(OnItemStockId,1);//卖出
            //买入
            //查询出的结果后五项为最新的
            if (sqlBuyOrders.size()<5)
            {
                showBuyOrderList = sqlBuyOrders;
            }else
            {
                for (int i=1;i<6;i++)
                {
                    showBuyOrderList.add(sqlBuyOrders.get(sqlBuyOrders.size()-i));
                }
            }
            //卖出
            if (sqlSellOrders.size()<5)
            {
                showSellOrderList = sqlSellOrders;
            }else
            {
                for (int i=1;i<6;i++)
                {
                    showSellOrderList.add(sqlSellOrders.get(sqlSellOrders.size()-i));
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
            super.onPostExecute(s);
            progressDialog.cancel();
            initViewListView();
        }
    }

    //防止进程对话框出错（that was originally added here）
    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null ) {
            progressDialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}