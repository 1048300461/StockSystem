package com.example.stocksystem.OrderShow;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocksystem.R;
import com.example.stocksystem.bean.Order;
import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.bean.Transaction;
import com.example.stocksystem.bean.User;
import com.example.stocksystem.dao.OrderDao;
import com.example.stocksystem.dao.StockDao;
import com.example.stocksystem.dao.TransactionDao;
import com.example.stocksystem.dao.UserDao;
import com.example.stocksystem.dao.impl.QueryOrdersTransDaoImpl;
import com.example.stocksystem.dao.impl.StockDaoImpl;
import com.example.stocksystem.dao.impl.TransactionDaoImpl;
import com.example.stocksystem.dao.impl.UserDaoImpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UserHoldStockActivity extends AppCompatActivity {

    //控件
    private TextView tv_user;
    private ListView listView;
    private ImageView imageView_showOrHide;     //显示或隐藏财务信息
    private ImageView imageView_refresh;
    private TextView tv_Cny_free;
    private TextView tv_AddSubCount;
    private TextView tv_AddNow;
    private TextView tv_AddNowPre;
    private TextView tv_StockCount;
    private TextView tv_CanUseCny_free;
    private ProgressDialog progressDialog;


    private boolean eyeOpen = false;//显示或隐藏资产信息

    private double moneyCount;  //总资产
    private double stockMoneyCount;     //总市值
    private double addSubCount;     //总营收

    public static String username = "未登录";
    public static int user_id = 10000001;

    //自定义显示class数据
    private List<UserHoldStock_ListView_Adapter.UserHoldStockAdapterStr> adapterlist = new  ArrayList<>();

    //查询暂时存储--》sql
    List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_hold_stock);

        //初始化用户信息
        initInfo();

        tv_user = findViewById(R.id.user_hold_stock_tvUser);
        listView = findViewById(R.id.user_hold_stock_listView);
        imageView_showOrHide = findViewById(R.id.user_hold_stock_ShowOrHide);
         tv_Cny_free = findViewById(R.id.user_hold_stock_tvCny_free);
         tv_AddSubCount = findViewById(R.id.user_hold_stock_tvAddSubCount);
         tv_AddNow = findViewById(R.id.user_hold_stock_tvAddNow);
         tv_AddNowPre = findViewById(R.id.user_hold_stock_tvAddNowPre);
         tv_StockCount = findViewById(R.id.user_hold_stock_tvStockCount);
         tv_CanUseCny_free = findViewById(R.id.user_hold_stock_tvCanUseCny_free);
         imageView_refresh = findViewById(R.id.user_hold_stock_IGrefresh);

         //刷新页面数据
         imageView_refresh.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 GetSQLAcsncTask getSQLAcsncTask = new GetSQLAcsncTask();
                 getSQLAcsncTask.execute();
             }
         });

         //显示或隐藏资产
        imageView_showOrHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eyeOpen) {

                    imageView_showOrHide.setImageResource(R.drawable.logo_pwd_show);
                    eyeOpen = false;
                    initUser();

                } else {

                    imageView_showOrHide.setImageResource(R.drawable.logo_pwd_hide);
                    eyeOpen = true;
                    tv_Cny_free.setText("*****");
                    tv_AddSubCount.setText("*****");
                    tv_AddNow.setText("*****");
                    tv_AddNowPre.setText("*****");
                    tv_StockCount.setText("*****");
                    tv_CanUseCny_free.setText("*****");
                    tv_AddSubCount.setTextColor(Color.parseColor("#000000"));
                    tv_AddNow.setTextColor(Color.parseColor("#000000"));
                    tv_AddNowPre.setTextColor(Color.parseColor("#000000"));
                }
            }
        });



         GetSQLAcsncTask getSQLAcsncTask = new GetSQLAcsncTask();
         getSQLAcsncTask.execute();



    }

    //加载账户数据
    private DecimalFormat df   = new DecimalFormat("######0.00");//保留两位小数
    private void initUser(){
        if (addSubCount > 0)
        {
            tv_AddSubCount.setTextColor(Color.parseColor(UserHoldStock_ListView_Adapter.text_color_add));
            tv_AddNow.setTextColor(Color.parseColor(UserHoldStock_ListView_Adapter.text_color_add));
            tv_AddNowPre.setTextColor(Color.parseColor(UserHoldStock_ListView_Adapter.text_color_add));
        }
        else
        {
            tv_AddSubCount.setTextColor(Color.parseColor(UserHoldStock_ListView_Adapter.text_color_sub));
            tv_AddNow.setTextColor(Color.parseColor(UserHoldStock_ListView_Adapter.text_color_sub));
            tv_AddNowPre.setTextColor(Color.parseColor(UserHoldStock_ListView_Adapter.text_color_sub));
        }
        tv_Cny_free.setText(df.format(moneyCount));
        tv_AddSubCount.setText(df.format(addSubCount));
        tv_AddNow.setText(df.format(addSubCount));
        tv_AddNowPre.setText(df.format(addSubCount/stockMoneyCount * 100)+"%");
        tv_StockCount.setText(df.format(stockMoneyCount));
        tv_CanUseCny_free.setText(df.format(moneyCount - stockMoneyCount));
        tv_user.setText("("+username+")");
    }

    //加载列表显示
    private void initListView(){
        UserHoldStock_ListView_Adapter adapter = new UserHoldStock_ListView_Adapter(UserHoldStockActivity.this,adapterlist);
        listView.setAdapter(adapter);
    }
    //访问数据库---》得到信息
    private class GetSQLAcsncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UserHoldStockActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("加载数据中。。。");
            progressDialog.setIndeterminate(true);  //设置ProgressDialog 的进度条是否不明确
            progressDialog.setCancelable(false); //设置ProgressDialog 是否可以按退回按键取消
            progressDialog.show();
            adapterlist.clear();
        }

        /**
         * doInBackground方法内部执行后台任务，不可在此方法中更新主UI
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {

            //得到listView信息
            QueryOrdersTransDaoImpl queryOrdersTransDao = new QueryOrdersTransDaoImpl();
            TransactionDao transactionDao = new TransactionDaoImpl();
            orderList = queryOrdersTransDao.findOneOrderByUserId(user_id);  //先获取buy订单编号
            for (Order order:orderList) {
                List<Transaction> transactions = new ArrayList<>();
                transactions = transactionDao.queryTransactionBybuyOrderId(order.getOrder_id());    //再获取buy订单的真实成交价格，真实成交量
                for (Transaction t:transactions)  //遍历transactions,,,该buy订单买了很多个sell订单的
                {
                    //给显示列表赋予值
                    UserHoldStock_ListView_Adapter.UserHoldStockAdapterStr userHoldStockAdapter = new UserHoldStock_ListView_Adapter.UserHoldStockAdapterStr();
                    //设置股票id和名称
                    //查询股票名称
                    StockDao stockDao = new StockDaoImpl();
                    Stock stock = new Stock();
                    stock = stockDao.findStockById(order.getStock_id());
                    userHoldStockAdapter.setStockName(stock.getName());
                    userHoldStockAdapter.setStock_id(stock.getStock_id());
                    //设置成交量和成交价
                    userHoldStockAdapter.setDealed(t.getDealed());
                    userHoldStockAdapter.setPrice(t.getPrice());


                    //-->软件特色：自动生成该股票市值，盈亏,现价对比
                    //设置最新的交易价格
                    userHoldStockAdapter.setPriceNow(transactionDao.queryNewestPriceByStockId(stock.getStock_id()));
                    //盈亏 = 持有量*（现价-成本）-->+为赚，-为亏
                    userHoldStockAdapter.setAddSub(userHoldStockAdapter.getDealed()*(userHoldStockAdapter.getPriceNow() - userHoldStockAdapter.getPrice()));
                    //盈亏率 = （现价-成本）/成本
                    userHoldStockAdapter.setAddSubPre((userHoldStockAdapter.getPriceNow()-userHoldStockAdapter.getPrice())/userHoldStockAdapter.getPrice());
                    //市值 = 现在价格 * 持有量
                    userHoldStockAdapter.setStock(userHoldStockAdapter.getPriceNow() * userHoldStockAdapter.getDealed());

                    //设置总市值
                    stockMoneyCount += userHoldStockAdapter.getStock();
                    //总盈亏
                    addSubCount+=userHoldStockAdapter.getAddSub();
                    adapterlist.add(userHoldStockAdapter);
                }
            }

            //获取users的数据
            UserDao userDao = new UserDaoImpl();
            User user = new User();
            user = userDao.findOneUser(user_id);
            //设置总资产 = 可用的 + 冻结 + 总市值
            moneyCount = user.getCny_free() + user.getCny_freezed() + stockMoneyCount;
            return null;
        }
        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {

            initListView();
            initUser();
            progressDialog.cancel();
            //显示完之后重置数据，避免刷新累加
            moneyCount = 0;
            stockMoneyCount = 0;
            addSubCount = 0;
            super.onPostExecute(s);
        }
    }

    public void finishThis(View view) {
        finish();
    }

    private void initInfo(){
        SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
        user_id = sp.getInt("userid", -1);
        username = sp.getString("name", "null");
    }
}
