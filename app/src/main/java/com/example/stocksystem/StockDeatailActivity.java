package com.example.stocksystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Observable;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stocksystem.util.StockDataUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;


public class StockDeatailActivity extends AppCompatActivity {

    private ImageView time_img, day_img, week_img, month_img;
    private TextView current_price_tv, high_price_tv, low_price_tv, begin_price_tv,
                    yesterday_price_tv, deal_num_tv, deal_money_tv;

    private static final String TAG = "StockDeatailActivity";

    private static volatile boolean isLoadSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_deatail);

        Bundle bundle = this.getIntent().getExtras();
        final String codeInfo = bundle.getString("codeInfo");

        time_img = findViewById(R.id.time_img);
        day_img = findViewById(R.id.day_img);
        week_img = findViewById(R.id.week_img);
        month_img = findViewById(R.id.month_img);

        current_price_tv = findViewById(R.id.current_price_tv);
        high_price_tv = findViewById(R.id.high_price_tv);
        low_price_tv = findViewById(R.id.low_price_tv);
        begin_price_tv = findViewById(R.id.begin_price_id);
        yesterday_price_tv = findViewById(R.id.yesterday_price_tv);
        deal_money_tv = findViewById(R.id.deal_money_tv);
        deal_num_tv = findViewById(R.id.deal_num_tv);

        isLoadSuccess = false;

        String[] urls = StockDataUtil.getGraphUrl(codeInfo);

        final String[] stockInfo = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                stockInfo[0] = StockDataUtil.getLatestInfo(codeInfo);
                isLoadSuccess = true;
            }
        }).start();

        //等待数据加载完毕
        while (!isLoadSuccess){ }

        String[] infos = StockDataUtil.parseStockInfo(stockInfo[0]);
        String[] correspondInfos = {
                "名字", "今日开盘价", "昨日收盘价", "当前价格", "今日最高价", "今日最低价",
                "竞买价", "竞卖价", "成交的股票数", "成交金额", "买一数", "买一价",
                "买二数", "买二价", "买三数", "买三价", "买四数", "买四价", "买五数", "买五价",
                "卖一数", "卖一价", "卖二数", "卖二价","卖三数", "卖三价","卖四数", "卖四价",
                "卖五数", "卖五价", "日期", "时间"};

        begin_price_tv.setText(infos[1]);
        yesterday_price_tv.setText(infos[2]);
        current_price_tv.setText(infos[3]);
        high_price_tv.setText(infos[4]);
        low_price_tv.setText(infos[5]);
        BigDecimal bd = new BigDecimal(Double.parseDouble(infos[8]) / 10000.0);
        bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        deal_money_tv.setText(bd + "万");
        deal_num_tv.setText(Double.parseDouble(infos[9]) / 100.0 + "百股");
        Log.d(TAG, "onCreate: " + infos[8]);
        Log.d(TAG, "onCreate: " + infos[9]);
        Log.d(TAG, "onCreate: " + Double.parseDouble(infos[9]) / 100.0 + "百");

        Glide.with(this).asBitmap().load(urls[0]).into(time_img);
        Glide.with(this).asBitmap().load(urls[1]).into(day_img);
        Glide.with(this).asBitmap().load(urls[2]).into(week_img);
        Glide.with(this).asBitmap().load(urls[3]).into(month_img);
        Log.d(TAG, "onCreate: " + urls[0]);

    }

}
