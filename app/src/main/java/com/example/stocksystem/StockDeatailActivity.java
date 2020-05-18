package com.example.stocksystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Observable;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.stocksystem.util.StockDataUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

public class StockDeatailActivity extends AppCompatActivity {

    private ImageView time_img, day_img, week_img, month_img;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private static final String TAG = "StockDeatailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_deatail);
        time_img = findViewById(R.id.time_img);
        day_img = findViewById(R.id.day_img);
        week_img = findViewById(R.id.week_img);
        month_img = findViewById(R.id.month_img);

        String[] urls = StockDataUtil.getGraphUrl("sh", 601006);
//        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.loading)
//                .cacheInMemory(false)
//                .cacheOnDisk(false)
//                .build();
//
//        imageLoader.displayImage(urls[0], day_img, options);
        Glide.with(this).asBitmap().load(urls[0]).into(time_img);
        Glide.with(this).asBitmap().load(urls[1]).into(day_img);
        Glide.with(this).asBitmap().load(urls[2]).into(week_img);
        Glide.with(this).asBitmap().load(urls[3]).into(month_img);
        Log.d(TAG, "onCreate: " + urls[0]);

//        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
//        final String[] path = new String[1];
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = imageLoader.loadImageSync(urls[1]);
//                path[0] = saveImageUrl(StockDeatailActivity.this, bitmap, "temp.jpg");
//                Log.d(TAG, "onCreate: " + path[0]);
//
//                Log.d(TAG, "onCreate: " + urls[0]);
//            }
//        }).start();
//
//        Glide.with(StockDeatailActivity.this).asBitmap().load(path[0]).into(time_img);
    }

}
