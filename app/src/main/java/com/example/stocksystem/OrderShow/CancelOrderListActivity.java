package com.example.stocksystem.OrderShow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stocksystem.R;
import com.example.stocksystem.bean.Order;
import com.example.stocksystem.bean.Stock;
import com.example.stocksystem.dao.OrderDao;
import com.example.stocksystem.dao.OrdersListDao;
import com.example.stocksystem.dao.StockDao;
import com.example.stocksystem.dao.UserDao;
import com.example.stocksystem.dao.impl.OrderDaoImpl;
import com.example.stocksystem.dao.impl.OrdersListDaoImpl;
import com.example.stocksystem.dao.impl.StockDaoImpl;
import com.example.stocksystem.dao.impl.UserDaoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancelOrderListActivity extends AppCompatActivity {
    public static String username = "wang";    //用户名--->admin为管理员
    public static int user_id = 10000002;

    private CancelOrderList_ListView_Adapter lvAdapter;//listView的自定义配置器

    private List<Order> orderList;  //数据库中数据
    private Map<String,String> stockNameMap = new HashMap<String, String>();
    private boolean IsCancelOrder;  //取消订单是否成功
    private int cancelOrderId=0;

    private ProgressDialog progressDialog;
    private TextView tv_username;
    private ListView listView;
    private AlertDialog.Builder builder;    //自定义对话框
    private ImageView imageView;//背景图片


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_orders_list);
        initInfo();

        tv_username = findViewById(R.id.cancel_order_list_tvUser);
        listView = findViewById(R.id.cancel_orderList_lv_ShowTotal);
        imageView = findViewById(R.id.cancel_orderList_imgView_bg);

        tv_username.setText("("+username+")");
        GetCancelOrdersListAsyncTask cancelListAT = new GetCancelOrdersListAsyncTask();       //访问数据库的耗时操作
        cancelListAT.execute();

        //ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (orderList.get(position).getCanceled()==0)
                {
                    cancelOrderId = orderList.get(position).getOrder_id();
                    if (orderList.get(position).getType()==1)   //卖订单
                    {
                        IsDialog("撤卖出单",stockNameMap.get(orderList.get(position).getStock_id()+""),orderList.get(position).getStock_id()+"",orderList.get(position).getUndealed()+"",orderList.get(position).getPrice()+"");
                    }else
                    {
                        IsDialog("撤买入单",stockNameMap.get(orderList.get(position).getStock_id()+""),orderList.get(position).getStock_id()+"",orderList.get(position).getUndealed()+"",orderList.get(position).getPrice()+"");
                    }
                }
                else
                {

                }


            }
        });



    }
    //自定义对话框
    private void IsDialog(String type,String stock,String stockId,String undealed,String price){
        builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View IsSureView = layoutInflater.inflate(R.layout.cancel_orders_list_sure_dialog,null);
        TextView tv_type = IsSureView.findViewById(R.id.cancel_order_dialog_tvType);
        TextView tv_stock = IsSureView.findViewById(R.id.cancel_order_dialog_tvStock);
        TextView tv_stockId = IsSureView.findViewById(R.id.cancel_order_dialog_tvStockId);
        TextView tv_undealed = IsSureView.findViewById(R.id.cancel_order_dialog_tvUndealed);
        TextView tv_price = IsSureView.findViewById(R.id.cancel_order_dialog_tvPrice);
         tv_type.setText(type);
         tv_stock.setText(stock);
         tv_stockId.setText(stockId);
         tv_undealed.setText(undealed);
         tv_price.setText(price);


        TextView tv_Div  = new TextView(this);
        tv_Div.setText("委托撤单确定");
        tv_Div.setTextSize(22);//字体大小
        tv_Div.setPadding(30, 20, 10, 10);
        tv_Div.setGravity(Gravity.CENTER);
        builder.setCustomTitle(tv_Div);
        builder.setView(IsSureView)
                .setPositiveButton("撤单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CancelOrdersAsyncTask cancelOrder = new CancelOrdersAsyncTask();
                        cancelOrder.execute();
                    }
                })
                .setNegativeButton("取消",null);
        AlertDialog ad = builder.create();
        ad.show();
    }


    //访问数据库获取数据
    private void getOrders(){
        setStockNameMap();
        OrdersListDaoImpl dao = new OrdersListDaoImpl();
        if (username.equals("admin"))       //管理员可以查询所有人的信息
        {
            orderList = dao.AllOrders();
        }else
        {

            orderList = dao.queryOrdersInUnDealedByUser(user_id);
        }
    }
    //构建股票名称和编号索引
    public void setStockNameMap(){
        StockDao stockDao = new StockDaoImpl();
        List<Stock> stockList = stockDao.queryAllStock();       //数据库中查询表的操作
        for (Stock s: stockList) {
            stockNameMap.put(s.getStock_id()+"",s.getName());
        }

    }

    //异步任务--》》访问数据库取消某订单
    private class CancelOrdersAsyncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CancelOrderListActivity.this);
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
            OrderDao od = new OrderDaoImpl();
            IsCancelOrder = od.CancelOrder(cancelOrderId);
            return null;
        }
        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            if (IsCancelOrder)
            {
                Toast.makeText(CancelOrderListActivity.this,"取消成功！",Toast.LENGTH_SHORT).show();
            }

            else
            {
                Toast.makeText(CancelOrderListActivity.this,"取消失败！",Toast.LENGTH_SHORT).show();
            }
            GetCancelOrdersListAsyncTask cancelListAT = new GetCancelOrdersListAsyncTask();       //访问数据库的耗时操作
            cancelListAT.execute();

            super.onPostExecute(s);
        }

    }
    //异步任务方法-->访问数据的耗时操作
    private class GetCancelOrdersListAsyncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CancelOrderListActivity.this);
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
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            initView();
            progressDialog.cancel();
            super.onPostExecute(s);
        }

    }
    //更新UI，加载数据
    private void initView(){
        lvAdapter = new CancelOrderList_ListView_Adapter(CancelOrderListActivity.this,orderList,stockNameMap);
        listView.setAdapter(lvAdapter);
        lvAdapter.notifyDataSetChanged();
        if (!orderList.isEmpty())
            imageView.setVisibility(View.GONE);
        else
            imageView.setVisibility(View.VISIBLE);
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
