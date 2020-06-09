package com.example.stocksystem.ChatMainPage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stocksystem.LoginActivity;
import com.example.stocksystem.OrderShow.BuyOrderUserActivity;
import com.example.stocksystem.OrderShow.CancelOrderListActivity;
import com.example.stocksystem.OrderShow.OrdersListActivity;
import com.example.stocksystem.OrderShow.SellOrderUserActivity;
import com.example.stocksystem.OrderShow.UserHoldStockActivity;
import com.example.stocksystem.OrderShow.UserHoldStock_ListView_Adapter;
import com.example.stocksystem.R;
import com.example.stocksystem.StockDeatailActivity;
import com.example.stocksystem.TestVoiceActivity;
import com.example.stocksystem.bean.Order;
import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.bean.Transaction;
import com.example.stocksystem.bean.User;
import com.example.stocksystem.dao.StockDao;
import com.example.stocksystem.dao.TransactionDao;
import com.example.stocksystem.dao.UserDao;
import com.example.stocksystem.dao.impl.QueryOrdersTransDaoImpl;
import com.example.stocksystem.dao.impl.StockDaoImpl;
import com.example.stocksystem.dao.impl.TransactionDaoImpl;
import com.example.stocksystem.dao.impl.UserDaoImpl;
import com.example.stocksystem.util.StockDataUtil;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChatMainPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edt_msg;
    private Button btn_send, select_btn;
    private TextView voice_tv;
    private List<MsgEntity> list;//存放对话框信息实体的集合
    boolean flag=false;//记录是否在待跳转状态
    int index=0;//记录跳向哪个界面
    int times = 0;
    private ProgressDialog progressDialog;
    private Resources resources;
    private String[] operations = new String[]{"购买股票", "抛售股票", "查询股票信息", "查询财务信息", "查询成交订单",
                        "查看委托中订单", "查询股票名称", "查询股票代码", "随机股票名称", "退出登录"};
    private MsgEntity msg1=new MsgEntity(MsgEntity.RCV_MSG,"欢迎您本软件，小股为您服务！"+
            "\n"+"您可以通过发送指令或语音输入\n跳转到相关界面进行操作"+
            "\n"+"1. 购买股票 股票代码(股票名称)"+
            "\n"+"2. 抛售股票"+
            "\n"+"3. 查询股票信息 股票代码(股票名称)"+
            "\n"+"4. 查询财务信息"+
            "\n"+"5. 查询成交订单" +
            "\n"+"6. 查看委托中订单"+
            "\n"+"7. 查询股票名称 股票代码"+
            "\n"+"8. 查询股票代码 股票名称" +
            "\n"+"9. 随机股票名称" +
            "\n"+"10. 退出登录");
    private MsgAdapter msgAdapter;
    private boolean isLoadSuccess = false;
    private String searchInfo = "";
    private String searchResult = "";

    private static final String TAG = "ChatMainPageActivity";
    private MsgEntity rcv_msg = null;
    private List<Stock> stockList;
    private String codeInfo = "";
    private String content = "";
    private String code = "";
    private String showContent;
    private int codeInt;
    private String stockName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_mainpage);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5eccbf6b");

        initView();//初始化控件
        initMsg();//初始化界面
        initListener();

        stockList = new ArrayList<>();
    }


    private void initListener(){
        msgAdapter=new MsgAdapter(this,list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(msgAdapter);

        select_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Drawable drawable;
                if(times % 2 == 0){
                    drawable = resources.getDrawable(R.drawable.ic_keyboard_black_24dp);
                    voice_tv.setVisibility(View.GONE);
                    edt_msg.setVisibility(View.VISIBLE);
                }else{
                    drawable = resources.getDrawable(R.drawable.ic_keyboard_voice_black_24dp);
                    voice_tv.setVisibility(View.VISIBLE);
                    edt_msg.setVisibility(View.GONE);
                }
                select_btn.setBackground(drawable);
                times += 1;
            }
        });

        voice_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSpeech(ChatMainPageActivity.this);
            }
        });

        //点击发送后的监听事件
        btn_send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                content = edt_msg.getText().toString().trim();
                if(!TextUtils.isEmpty(content)){
                    MsgEntity send_msg=new MsgEntity(MsgEntity.SEND_MSG,content);
                    list.add(send_msg);
                    //刷新RecyclerView显示
                    msgAdapter.notifyItemInserted(list.size()-1);

                    //模拟接受消息
                    MsgEntity rcv_msg=null;
                    codeInt = getActivityCode(content);

                    if(codeInt != -1){
                        showContent = operations[codeInt];
                        content = content.replace(showContent, "");
                        content = content.replace(" ", "");
                        codeInfo = "";
                        if(codeInt == 0 || codeInt == 2){
                            if(content.length() == 0){
                                rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,"您未说明股票名称/股票代码哦。");
                                reciveMsg(rcv_msg, msgAdapter);
                            }else{

                                GetStockInfoAcsncTask getStockInfoAcsncTask = new GetStockInfoAcsncTask();
                                getStockInfoAcsncTask.execute();
                            }
                        }else if(codeInt == 6 || codeInt == 7){
                            searchInfo = content;
                            if(searchInfo.length() < 2){
                                rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,"小股提醒您：请附加要查询的内容！");
                                reciveMsg(rcv_msg, msgAdapter);
                            }else{
                                GetSQLAcsncTask getSQLAcsncTask = new GetSQLAcsncTask();
                                getSQLAcsncTask.execute();
                            }

                        }else if(codeInt == 9){
                            //退出登录
                            logout();
                        }else if(codeInt == 8){
                            //查看热门股票
                            findHotStock();
                        }else{
                            showDialog(showContent, codeInt, "");
                        }

                    }else{
                        rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,"小股不懂您的需求呢！");
                        reciveMsg(rcv_msg, msgAdapter);
                    }
                }
                edt_msg.setText("");
            }
        });
    }

    /**
     * 查询最热门的5个股票
     */
    private void findHotStock() {
        GetStockAcsncTask getStockAcsncTask = new GetStockAcsncTask();
        getStockAcsncTask.execute();

    }


    //访问数据库--》得到股票编号和名称,查询用户可用人民币数量
    private class GetStockAcsncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ChatMainPageActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("小股查询数据中。。。");
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
            Log.d(TAG, "stock: " + stockList.toString());
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

            String content = "随机显示5条股票：\n";



            for(int i = 0; i < 5; i++){
                int index = new Random().nextInt(1000*(i+1)-(1 + 1000*i))+(1 + 1000*i);
                if(index > 10){
                    index -= 5;
                }
                Log.d(TAG, "onPostExecute: " + index);
                Stock stock = stockList.get(index);
                String code = stock.getStock_id() + "";
                int length = 6 - code.length();
                String temp = "";
                for(int j = 0; j < length; j++){
                    temp += "0";
                }
                content += "股票名称: " + stock.getName() + "  股票代码: " + temp + code + "\n";
            }

            rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,content);
            reciveMsg(rcv_msg, msgAdapter);

        }

    }

    /**
     * 退出登录
     * @author zc
     */
    private void logout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ChatMainPageActivity.this);

        builder.setIcon(R.drawable.ic_question_answer_black_24dp);
        builder.setMessage("确定退出登录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //修改sp内容
                SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isLogin", false);
                editor.putInt("userid",-1);
                editor.putString("name","");
                editor.apply();
                //跳转至登录界面
                Intent intent = new Intent(ChatMainPageActivity.this, LoginActivity.class);
                startActivity(intent);
                //结束该活动
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    //访问数据库---》得到信息
    private class GetSQLAcsncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ChatMainPageActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("小股查询数据中。。。");
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
            Stock stock;

            try{
                int codeId = Integer.parseInt(searchInfo);
                stock = stockDao.findStockById(codeId);
                if(stock == null){
                    rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,"小股提醒您：查询失败。\n失败原因：不存在该股票代码。");
                }else{
                    searchResult = stock.getName();
                    rcv_msg = new MsgEntity(MsgEntity.RCV_MSG, "小股提醒您：\n股票代码" +
                            searchInfo + "\n对应的股票名称为：" + searchResult);
                }


            }catch (Exception e){
                stock = stockDao.findStockByName(searchInfo);
                if(stock != null){
                    searchResult = stock.getStock_id() + "";
                    rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,"小股提醒您：\n股票名称" +
                            searchInfo +"\n对应的股票代码为：" + searchResult);
                }else{
                    rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,"小股提醒您：查询失败。\n失败原因：不存在该股票名称。");
                }

            }


            Log.d(TAG, "doInBackground: " + searchResult);
            return null;
        }
        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();

            reciveMsg(rcv_msg, msgAdapter);
            super.onPostExecute(s);
        }
    }

    private void reciveMsg( MsgEntity rcv_msg,MsgAdapter msgAdapter){
        list.add(rcv_msg);
        msgAdapter.notifyItemInserted(list.size()-1);
        //将RecyclerView将显示的数据定位到最后一行
        recyclerView.scrollToPosition(list.size()-1);
    }

    //初始化界面
    private void initMsg() {
        list=new ArrayList<MsgEntity>();
        list.add(msg1);
    }

    //初始化控件
    private void initView() {
        recyclerView=(RecyclerView)findViewById(R.id.recylerView);
        edt_msg= findViewById(R.id.edt_msg);
        btn_send= findViewById(R.id.btn_send);
        select_btn = findViewById(R.id.select_btn);
        voice_tv = findViewById(R.id.voice_tv);
        resources = ChatMainPageActivity.this.getResources();
    }


    /**
     * 初始化语音识别
     */
    public void initSpeech(final Context context){
        //创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, null);
        //设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if(!isLast){
                    //解析语音
                    String result = parseVoice(recognizerResult.getResultString());
                    edt_msg.setText(result);
                    edt_msg.setVisibility(View.VISIBLE);
                    voice_tv.setVisibility(View.GONE);
                    select_btn.setBackground(getDrawable(R.drawable.ic_keyboard_black_24dp));
                    times = 0;
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //显示dialog，接收语音输入
        mDialog.show();
    }

    /**
     * 解析语音json
     * @param resultString
     * @return
     */
    public String parseVoice(String resultString){
        Gson gson = new Gson();
        TestVoiceActivity.Voice voiceBean = gson.fromJson(resultString, TestVoiceActivity.Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<TestVoiceActivity.Voice.WSBean> ws = voiceBean.ws;
        for (TestVoiceActivity.Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    /**
     * 语音对象封装
     */
    public class Voice {

        public ArrayList<TestVoiceActivity.Voice.WSBean> ws;

        public class WSBean {
            public ArrayList<TestVoiceActivity.Voice.CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }

    private void showDialog(String showContent, final int code, final String args){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ChatMainPageActivity.this);

        builder.setIcon(R.drawable.ic_question_answer_black_24dp);
        builder.setMessage("确定进入" + showContent + "页面吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (code){
                    case 0://购买股票
                        Bundle bundle0 = new Bundle();
                        bundle0.putString("codeInfo", args);
                        Intent intent0 = new Intent(ChatMainPageActivity.this, BuyOrderUserActivity.class);
                        intent0.putExtras(bundle0);
                        startActivity(intent0);
                        break;
                    case 1://出售股票
                        Intent intent1 = new Intent(ChatMainPageActivity.this, SellOrderUserActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:////查询股票
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("stockID", args);
                        Intent intent2 = new Intent(ChatMainPageActivity.this, StockDeatailActivity.class);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        break;
                    case 3://查询持股信息
                        Intent intent3 = new Intent(ChatMainPageActivity.this, UserHoldStockActivity.class);
                        startActivity(intent3);
                        break;
                    case 4://查看历史交易订单
                        Intent intent4 = new Intent(ChatMainPageActivity.this, OrdersListActivity.class);
                        startActivity(intent4);
                        break;
                    case 5://取消交易
                        Intent intent5 = new Intent(ChatMainPageActivity.this, CancelOrderListActivity.class);
                        startActivity(intent5);
                        break;
                    default:
                        break;

                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MsgEntity rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,"您已取消跳转。");
                reciveMsg(rcv_msg, msgAdapter);
                reciveMsg(msg1, msgAdapter);
            }
        });
        builder.show();
    }

    /**
     * 获取对应操作的代码
     * @param content
     * @return
     */
    private int getActivityCode(String content){
        for (int i = 0; i < operations.length; i++) {
            if(content.contains(operations[i])){
                return i;
            }
        }
        return -1;
    }

    //访问数据库--》得到股票编号和名称,查询用户可用人民币数量
    private class GetStockInfoAcsncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ChatMainPageActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("小股查询数据中。。。");
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
            code = "";
            Stock stock;
            try{
                int stockId = Integer.parseInt(content);
                stock = new StockDaoImpl().findStockById(stockId);

                if(stock != null){
                    code = stockId + "";
                    int length = 6 - code.length();
                    String temp = "";
                    for(int i = 0; i < length; i++){
                        temp += "0";
                    }
                    code = temp + code;
                    if(stock.getType() == 0){
                        code = "sh" + code;
                    }else{
                        code = "sz" + code;
                    }
                }else{
                    codeInfo = "";
                }
            }catch (Exception e){
                Log.d(TAG, "onClick: " + content);
                stock = new StockDaoImpl().findStockByName(content);
                if(stock != null){
                    code = stock.getStock_id() + "";
                    int length = 6 - code.length();
                    String temp = "";
                    for(int i = 0; i < length; i++){
                        temp += "0";
                    }
                    code = temp + code;

                    if(stock.getType() == 0){
                        code = "sh" + code;
                    }else{
                        code = "sz" + code;
                    }
                }else{
                    codeInfo = "";
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
            codeInfo = code;

            if(codeInfo.length() == 0){
                rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,"小股没有查到该股票哦。");
                reciveMsg(rcv_msg, msgAdapter);
            }else{
                showDialog(showContent, codeInt, codeInfo);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
