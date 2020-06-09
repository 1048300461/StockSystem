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

public class AdminListViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<AdminAdapterContent> lists;  //填充列表数据

    public AdminListViewAdapter(Context context, List<AdminAdapterContent> orderList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.lists = orderList;
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
            convertView = mInflater.inflate(R.layout.admin_main_lvitem,null);
            hodler.tvStockName = convertView.findViewById(R.id.admin_lvitem_tvStockName);
            hodler.tvAdminUndealed = convertView.findViewById(R.id.admin_lvitem_tvAdminUndealed);
            hodler.tvUserUndealed = convertView.findViewById(R.id.admin_lvitem_tvUserUndealed);
            convertView.setTag(hodler);
        }
        else
        {
            hodler = (ViewHodler) convertView.getTag();
        }
        //设置值
        hodler.tvStockName.setText(lists.get(position).getStockName());
        hodler.tvAdminUndealed.setText(lists.get(position).getAdminNum()+"");
        hodler.tvUserUndealed.setText(lists.get(position).getUserNum()+"");

        return convertView;
    }
    class ViewHodler{
        private TextView tvStockName,tvAdminUndealed,tvUserUndealed;
    }
    static class AdminAdapterContent{
        public String stockName;
        public int adminNum;
        public int userNum;

        public AdminAdapterContent() {
        }

        @Override
        public String toString() {
            return "adminAdapterContent{" +
                    "stockName='" + stockName + '\'' +
                    ", adminNum=" + adminNum +
                    ", userNum=" + userNum +
                    '}';
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public int getAdminNum() {
            return adminNum;
        }

        public void setAdminNum(int adminNum) {
            this.adminNum = adminNum;
        }

        public int getUserNum() {
            return userNum;
        }

        public void setUserNum(int userNum) {
            this.userNum = userNum;
        }
    }
}
