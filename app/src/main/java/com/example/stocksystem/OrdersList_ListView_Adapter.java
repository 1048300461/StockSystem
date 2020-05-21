package com.example.stocksystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stocksystem.bean.Order;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class OrdersList_ListView_Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<Order> orderList;  //填充列表数据
    Map<String,String> stockMap;    //用于查询股票名称

    public OrdersList_ListView_Adapter(Context context, List<Order> orderList,Map<String,String> stockMap) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.orderList = orderList;
        this.stockMap = stockMap;
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
            convertView = mInflater.inflate(R.layout.orders_list_lvitem_table,null);
            hodler.tv_stockName = convertView.findViewById(R.id.ordersList_tv_stockName);
            hodler.tv_type = convertView.findViewById(R.id.ordersList_tv_type);
            hodler.tv_time = convertView.findViewById(R.id.ordersList_tv_time);
            hodler.tv_price = convertView.findViewById(R.id.ordersList_tv_price);
            hodler.tv_deaded = convertView.findViewById(R.id.ordersList_tv_dealed);
            hodler.tv_type2 = convertView.findViewById(R.id.ordersList_tv_type2);
            hodler.tv_total = convertView.findViewById(R.id.ordersList_tv_total);
            convertView.setTag(hodler);
        }
        else
        {
            hodler = (ViewHodler) convertView.getTag();
        }
        //设置值
        hodler.tv_stockName.setText(stockMap.get(orderList.get(position).getStock_id()+""));
        System.out.println(orderList.get(position).getStock_id()+"");
        //时间转换
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        hodler.tv_time.setText(formatter.format(orderList.get(position).getCreate_date()));
        //设置买(0)或者卖(1)的名称
        if (orderList.get(position).getType()==1)
        {
            hodler.tv_type.setText("卖");
            hodler.tv_type2.setText("卖出");
        }else
        {
            hodler.tv_type.setText("买");
            hodler.tv_type2.setText("买入");
        }
        //设置成交价
        hodler.tv_price.setText(orderList.get(position).getPrice() + "");
        //设置交易量
        hodler.tv_deaded.setText(orderList.get(position).getDealed()+"");
        //设置成交额
        hodler.tv_total.setText(orderList.get(position).getDealed()*orderList.get(position).getPrice()+"");
        return convertView;
    }
    class ViewHodler{
        private TextView tv_stockName,tv_type,tv_time,tv_deaded,tv_price,tv_type2,tv_total;
    }
}
