package com.example.stocksystem.OrderShow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stocksystem.R;
import com.example.stocksystem.bean.Order;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class buyOrder_User_ListView_Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<Order> orderList;  //填充列表数据

    public buyOrder_User_ListView_Adapter(Context context, List<Order> orderList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Order gameReport = (Order)getItem(position);
        ViewHodler hodler;
        if (convertView == null)
        {
            //获取控件，实例化
            hodler = new ViewHodler();
            convertView = mInflater.inflate(R.layout.buy_order_user_lvitem,null);
            hodler.tv_num = convertView.findViewById(R.id.buy_order_user_lvitem_TVNum);
            hodler.tv_price = convertView.findViewById(R.id.buy_order_user_lvitem_TVprice);
            hodler.tv_count = convertView.findViewById(R.id.buy_order_user_lvitem_TVCount);
            convertView.setTag(hodler);
        }
        else
        {
            hodler = (ViewHodler) convertView.getTag();
        }

        //设置买入和卖出
        if (orderList.get(0).getType()==0)//0代表买入
        {
            hodler.tv_num.setText("买"+ (position+1));
        }else{
            hodler.tv_num.setText("卖"+ (position+1));
        }

        hodler.tv_price.setText(orderList.get(position).getPrice() + "");
        hodler.tv_count.setText(orderList.get(position).getDealed()+"");
        return convertView;
    }
    class ViewHodler{
        private TextView tv_num,tv_price,tv_count;
    }
}
