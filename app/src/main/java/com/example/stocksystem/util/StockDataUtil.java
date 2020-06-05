package com.example.stocksystem.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * author:zc
 * created on:2020/4/28 15:49
 * description:
 */
public class StockDataUtil {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    private static final String TAG = "StockDataUtil";
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/29.0.1547.66 Safari/537.36";


    /**
     * 获取沪深股市信息
     * @param whichStock sh为上海 sz为深圳
     * @param whichStock 股票代码
     * @return 结果
     * @author zc
     */
    public static String getLatestInfo(String whichStock){
        String result = null;
        //请求接口地址
        String url ="http://hq.sinajs.cn/";
        //请求参数
        Map params = new HashMap();
        //股票编号，上海股市以sh开头，深圳股市以sz开头
        params.put("list", whichStock);

        try{
            result = net(url, params, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.d(TAG, "getLatestInfo: " + result);
        System.out.println(result);
        return result;
    }

    /**
     * 解析获取到的数据
     * @param stockInfo 获取到的股票信息
     * @author zc
     */
    public static String[] parseStockInfo(String stockInfo){
        String[] infos = stockInfo.split(",");
        String[] correspondInfos = {
                "名字", "今日开盘价", "昨日收盘价", "当前价格", "今日最高价", "今日最低价",
                "竞买价", "竞卖价", "成交的股票数", "成交金额", "买一数", "买一价",
                "买二数", "买二价", "买三数", "买三价", "买四数", "买四价", "买五数", "买五价",
                "卖一数", "卖一价", "卖二数", "卖二价","卖三数", "卖三价","卖四数", "卖四价",
                "卖五数", "卖五价", "日期", "时间"};
        String[] parseResult = new String[correspondInfos.length - 1];
        for (int i = 1; i < correspondInfos.length; i++){
            //跳过第一个，第一个为股票名字，已经有了
            //parseResult[i - 1] = correspondInfos[i] + ": " + infos[i];
            parseResult[i - 1] = infos[i];
        }
        return parseResult;
    }

    /**
     * 获取沪深股市信息
     * @param which sh为上海 sz为深圳
     * @param stock_id 股票代码
     * @return result 结果
     * @author zc
     */
    public static String getBigPan(String which, int stock_id){
        String result = null;
        //请求接口地址
        String url ="http://hq.sinajs.cn/";
        //请求参数
        Map params = new HashMap();
        //股票编号，上海股市以sh开头，深圳股市以sz开头
        params.put("list", "s_" + which + stock_id);

        try{
            result = net(url, params, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析获取到的数据
     * @param bigPan 获取到的股票信息
     * @author zc
     */
    public static String[] parseBigPan(String bigPan){
        String[] infos = bigPan.split(",");
        String[] correspondInfos = {
                "指数名称", "当前点数", "当前价格", "涨跌率", "成交量（手）", "成交额（万元）"};
        String[] parseResult = new String[correspondInfos.length- 1];
        for (int i = 1; i < correspondInfos.length; i++){
            //跳过第一个，第一个为股票名字，已经有了
            parseResult[i - 1] = correspondInfos[i] + ": " + infos[i];
        }
        return parseResult;
    }

    public static String[] getGraphUrl(String whichStock){
        String[] graphUrls = new String[4];
        String[] graphTypes = {"min", "daily", "weekly", "monthly"};
        String begin = "http://image.sinajs.cn/newchart/";
        String middle = "/n/";
        String end = ".gif";
        for(int i = 0; i < graphTypes.length; i++){
            graphUrls[i] = begin + graphTypes[i] + middle + whichStock + end;
        }

        return graphUrls;
    }

    public static void main(String[] args) {

        String stockInfo = getLatestInfo("sh601006");
        String[] parseStockInfoReuslt = parseStockInfo(stockInfo);

        for(int i = 0; i < parseStockInfoReuslt.length; i++)
            System.out.println(parseStockInfoReuslt[i]);
    }


    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
