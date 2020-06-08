package com.example.stocksystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Observable;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stocksystem.OrderShow.BuyOrderUserActivity;
import com.example.stocksystem.OrderShow.buyOrder_User_ListView_Adapter;
import com.example.stocksystem.bean.Order;
import com.example.stocksystem.util.StockDataUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class StockDeatailActivity extends AppCompatActivity {

    private ImageView time_img, day_img, week_img, month_img;
    private TextView current_price_tv, high_price_tv, low_price_tv, begin_price_tv,
                    yesterday_price_tv, deal_num_tv, deal_money_tv;

    private static final String TAG = "StockDeatailActivity";

    private static volatile boolean isLoadSuccess = false;
    private ProgressDialog progressDialog;
    private List<Order> showBuyOrderList = new ArrayList<>();
    private List<Order> showSellOrderList =  new ArrayList<>();

    private String codeInfo = "";
    private ListView listView_buy ;
    private ListView listView_sell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_deatail);

        Bundle bundle = this.getIntent().getExtras();
        codeInfo = bundle.getString("codeInfo");

        time_img = findViewById(R.id.time_img);
        day_img = findViewById(R.id.day_img);
        week_img = findViewById(R.id.week_img);
        month_img = findViewById(R.id.month_img);

        listView_buy = findViewById(R.id.buy_order_lvbuy);
        listView_sell = findViewById(R.id.buy_order_lvSell);

        current_price_tv = findViewById(R.id.current_price_tv);
        high_price_tv = findViewById(R.id.high_price_tv);
        low_price_tv = findViewById(R.id.low_price_tv);
        begin_price_tv = findViewById(R.id.begin_price_id);
        yesterday_price_tv = findViewById(R.id.yesterday_price_tv);
        deal_money_tv = findViewById(R.id.deal_money_tv);
        deal_num_tv = findViewById(R.id.deal_num_tv);

//        isLoadSuccess = false;
//

//
//        final String[] stockInfo = {null};
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                stockInfo[0] = StockDataUtil.getLatestInfo(codeInfo);
//                isLoadSuccess = true;
//            }
//        }).start();
//
//        //等待数据加载完毕
//        while (!isLoadSuccess){ }

//
//        String[] infos = StockDataUtil.parseStockInfo(stockInfo[0]);
//        String[] correspondInfos = {
//                "今日开盘价", "昨日收盘价", "当前价格", "今日最高价", "今日最低价",
//                "竞买价", "竞卖价", "成交的股票数", "成交金额", "买一数", "买一价",
//                "买二数", "买二价", "买三数", "买三价", "买四数", "买四价", "买五数", "买五价",
//                "卖一数", "卖一价", "卖二数", "卖二价","卖三数", "卖三价","卖四数", "卖四价",
//                "卖五数", "卖五价", "日期", "时间"};




        String[] urls = StockDataUtil.getGraphUrl(codeInfo);
        Glide.with(this).asBitmap().load(urls[0]).into(time_img);
        Glide.with(this).asBitmap().load(urls[1]).into(day_img);
        Glide.with(this).asBitmap().load(urls[2]).into(week_img);
        Glide.with(this).asBitmap().load(urls[3]).into(month_img);
        Log.d(TAG, "onCreate: " + urls[0]);

        GetBuySellAcsncTask getBuySellAcsncTask = new GetBuySellAcsncTask();
        getBuySellAcsncTask.execute();

    }

    //访问数据库---》得到某股票的近期交易信息
    private class GetBuySellAcsncTask extends AsyncTask<Integer,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(StockDeatailActivity.this);
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
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(Integer... StrockId) {

            codeInfo = StockDataUtil.getLatestInfo(codeInfo);
            Log.d(TAG, "doInBackground: " + codeInfo);
            return null;
        }
        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            String[] infos = StockDataUtil.parseStockInfo(codeInfo);
            showSellOrderList.clear();
            showBuyOrderList.clear();

            for(int i = 9; i < 19; i = i + 2){
                Order order = new Order();
                order.setDealed(Integer.parseInt(infos[i]));
                order.setPrice(Double.parseDouble(infos[i+1]));
                order.setType(0);
                Log.d(TAG, "getSellOrder: " + order.getDealed() + " " + order.getPrice());
                showBuyOrderList.add(order);
            }

            for(int i = 19; i < 29; i = i + 2){
                Order order = new Order();
                order.setDealed(Integer.parseInt(infos[i]));
                order.setPrice(Double.parseDouble(infos[i+1]));
                order.setType(1);
                Log.d(TAG, "getSellOrder: " + order.getDealed() + " " + order.getPrice());
                showSellOrderList.add(order);
            }
            begin_price_tv.setText(infos[0]);
            yesterday_price_tv.setText(infos[1]);
            current_price_tv.setText(infos[2]);
            high_price_tv.setText(infos[3]);
            low_price_tv.setText(infos[4]);

            BigDecimal bd = new BigDecimal(Double.parseDouble(infos[8]) / 10000.0);
            bd =  bd.setScale(2,BigDecimal.ROUND_HALF_UP);
            deal_money_tv.setText(bd + "万");
            deal_num_tv.setText(Double.parseDouble(infos[7]) / 100.0 + "百股");

            progressDialog.cancel();
            initViewListView();
            super.onPostExecute(s);

        }
    }

    private void initViewListView(){
        //买入
        buyOrder_User_ListView_Adapter buyLVAdapter = new buyOrder_User_ListView_Adapter(StockDeatailActivity.this,showBuyOrderList);
        listView_buy.setAdapter(buyLVAdapter);
        buyLVAdapter.notifyDataSetChanged();
        //卖出
        buyOrder_User_ListView_Adapter sellLVAdapter = new buyOrder_User_ListView_Adapter(StockDeatailActivity.this,showSellOrderList);
        listView_sell.setAdapter(sellLVAdapter);
        sellLVAdapter.notifyDataSetChanged();
    }

}
