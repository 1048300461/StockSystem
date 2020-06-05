package com.example.stocksystem.OrderShow;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stocksystem.R;
import com.example.stocksystem.bean.Order;

import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class UserHoldStock_ListView_Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<UserHoldStockAdapterStr> lists;  //填充列表数据
    public static String text_color_add = "#ff0000";
    public static String text_color_sub = "#3366CC";

    public UserHoldStock_ListView_Adapter(Context context, List<UserHoldStockAdapterStr> lists) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler;
        if (convertView == null)
        {
            //获取控件，实例化
            hodler = new ViewHodler();
            convertView = mInflater.inflate(R.layout.user_hold_stock_lvitem_table,null);
            hodler.tv_StockName = convertView.findViewById(R.id.userhs_lvItem_tvStockName);
            hodler.tv_Stock = convertView.findViewById(R.id.userhs_lvItem_tvStock);
            hodler.tv_AddSub = convertView.findViewById(R.id.userhs_lvItem_tvAddSub);
            hodler.tv_AddSubPre = convertView.findViewById(R.id.userhs_lvItem_tvAddSubPre);
            hodler.tv_Hold = convertView.findViewById(R.id.userhs_lvItem_tvHold);
            hodler.tv_Price = convertView.findViewById(R.id.userhs_lvItem_tvPrice);
            hodler.tv_PriceNow = convertView.findViewById(R.id.userhs_lvItem_tvPriceNow);
            convertView.setTag(hodler);
        }
        else
        {
            hodler = (ViewHodler) convertView.getTag();
        }


        //设置值
        if (lists.get(position).getAddSub()>=0)
        {
            hodler.tv_StockName.setTextColor(Color.parseColor(text_color_add));
            hodler.tv_Stock.setTextColor(Color.parseColor(text_color_add));
            hodler.tv_AddSub.setTextColor(Color.parseColor(text_color_add));
            hodler.tv_AddSubPre.setTextColor(Color.parseColor(text_color_add));
            hodler.tv_Hold.setTextColor(Color.parseColor(text_color_add));
            hodler.tv_Price.setTextColor(Color.parseColor(text_color_add));
            hodler.tv_PriceNow.setTextColor(Color.parseColor(text_color_add));
        }else
        {
            hodler.tv_StockName.setTextColor(Color.parseColor(text_color_sub));
            hodler.tv_Stock.setTextColor(Color.parseColor(text_color_sub));
            hodler.tv_AddSub.setTextColor(Color.parseColor(text_color_sub));
            hodler.tv_AddSubPre.setTextColor(Color.parseColor(text_color_sub));
            hodler.tv_Hold.setTextColor(Color.parseColor(text_color_sub));
            hodler.tv_Price.setTextColor(Color.parseColor(text_color_sub));
            hodler.tv_PriceNow.setTextColor(Color.parseColor(text_color_sub));
        }
        DecimalFormat df   = new DecimalFormat("######0.00");//保留两位小数
        hodler.tv_StockName.setText(lists.get(position).getStockName());
        hodler.tv_Stock.setText(df.format(lists.get(position).getStock()));
        hodler.tv_AddSub.setText(df.format(lists.get(position).getAddSub()));
        hodler.tv_AddSubPre.setText(df.format(lists.get(position).getAddSubPre()*100)+ "%");
        hodler.tv_Hold.setText(lists.get(position).getDealed()+"");
        hodler.tv_Price.setText(df.format(lists.get(position).getPrice()));
        hodler.tv_PriceNow.setText(df.format(lists.get(position).getPriceNow()));
        return convertView;
    }
    class ViewHodler{
        private TextView tv_StockName,tv_Stock,tv_AddSub,tv_AddSubPre,tv_Hold,tv_Price,tv_PriceNow;
    }

    //用于配置UserHoldStock_ListView_Adapter
    static class UserHoldStockAdapterStr{
        public String StockName;
        public Double Stock;
        public Double AddSub;
        public Double AddSubPre;
        public int Dealed;
        public Double Price;        //用户买的成交价
        public Double PriceNow;     //现在的成交价
        public int Stock_id;

        public UserHoldStockAdapterStr() {
        }

        @Override
        public String toString() {
            return "UserHoldStockAdapterStr{" +
                    "StockName='" + StockName + '\'' +
                    ", Stock=" + Stock +
                    ", AddSub=" + AddSub +
                    ", AddSubPre=" + AddSubPre +
                    ", Dealed=" + Dealed +
                    ", Price=" + Price +
                    ", PriceNow=" + PriceNow +
                    ", Stock_id=" + Stock_id +
                    '}';
        }

        public int getDealed() {
            return Dealed;
        }

        public void setDealed(int dealed) {
            Dealed = dealed;
        }

        public int getStock_id() {
            return Stock_id;
        }

        public void setStock_id(int stock_id) {
            Stock_id = stock_id;
        }

        public String getStockName() {
            return StockName;
        }

        public void setStockName(String stockName) {
            StockName = stockName;
        }

        public Double getStock() {
            return Stock;
        }

        public void setStock(Double stock) {
            Stock = stock;
        }

        public Double getAddSub() {
            return AddSub;
        }

        public void setAddSub(Double addSub) {
            AddSub = addSub;
        }

        public Double getAddSubPre() {
            return AddSubPre;
        }

        public void setAddSubPre(Double addSubPre) {
            AddSubPre = addSubPre;
        }

        public Double getPrice() {
            return Price;
        }

        public void setPrice(Double price) {
            Price = price;
        }

        public Double getPriceNow() {
            return PriceNow;
        }

        public void setPriceNow(Double priceNow) {
            PriceNow = priceNow;
        }
    }
}
