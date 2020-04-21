package com.example.stocksystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stocksystem.util.DataBaseUtil;

public class MainActivity extends AppCompatActivity {

    private Button btnTest;
    private Button btnClean;
    private TextView tvTestResult;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest=findViewById(R.id.btnTestSql);
        btnClean=findViewById(R.id.btnClean);
        tvTestResult = findViewById(R.id.tvTestResult);
        btnTest.setOnClickListener(getClickEvent());
        btnClean.setOnClickListener(getClickEvent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private View.OnClickListener getClickEvent(){
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tvTestResult.setText("...");
                if(v==btnTest){
                    test();
                }
            }
        };
    }
    private void test()
    {
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                String ret = DataBaseUtil.testSQL();
                Message msg = new Message();
                msg.what=1001;
                Bundle data = new Bundle();
                data.putString("result", ret);
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        };
        new Thread(run).start();

    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what)
            {

                case 1001:
                    String str = msg.getData().getString("result");
                    tvTestResult.setText(str);
                    break;
                default:
                    break;
            }
        };
    };
}
