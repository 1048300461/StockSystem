package com.example.stocksystem.ChatMainPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.stocksystem.R;

import java.util.ArrayList;
import java.util.List;

public class ChatMainPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edt_msg;
    private Button btn_send;
    private List<MsgEntity> list;//存放对话框信息实体的集合
    boolean flag=false;//记录是否在待跳转状态
    int index=0;//记录跳向哪个界面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_mainpage);
        initView();//初始化控件
        initMsg();//初始化界面

        final MsgAdapter msgAdapter=new MsgAdapter(this,list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(msgAdapter);

        //点击发送后的监听事件
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send_content=edt_msg.getText().toString().trim();
                if (!TextUtils.isEmpty(send_content)){
                    //模拟发送消息
                    MsgEntity send_msg=new MsgEntity(MsgEntity.SEND_MSG,send_content);
                    list.add(send_msg);
                    //刷新RecyclerView显示
                    msgAdapter.notifyItemInserted(list.size()-1);

                    //模拟接受消息
                    MsgEntity rcv_msg=null;
                    if(send_content.equals("buy")||send_content.equals("sell")){
                        rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,"请确认是否跳转到"+send_content+"功能界面"+
                                "\n"+"回复[yes]进行跳转");
                        switch (send_content){//记录用户选择要跳转到哪个功能界面
                            case "buy":
                                index=1;
                            case "sell":
                                index=2;
                            case "detail":
                                index=3;
                            case "orderlist":
                                index=4;
                        }
                        flag=true;//此时为待跳转状态
                        reciveMsg(rcv_msg,msgAdapter);
                    }else {
                        if(flag) {//如果此时是待跳转状态
                            flag = false;
                            if (send_content.equals("yes")) {//同意跳转，跳转界面
                                switch (index) {
                                    case 1:
//                                        startActivity(new Intent(ChatMainPageActivity.this, buyActivity.class));
                                    case 2:
//                                        startActivity(new Intent(ChatMainPageActivity.this, sellActivity.class));
                                    case 3:
//                                        startActivity(new Intent(ChatMainPageActivity.this, detailActivity.class));
                                    case 4:
//                                        startActivity(new Intent(ChatMainPageActivityy.this, orderlistActivity.class));
                                }
                            }else {
                                rcv_msg = new MsgEntity(MsgEntity.RCV_MSG, "您选择了不跳转"+"\n"+"请重新输入想要进行的操作");
                                reciveMsg(rcv_msg,msgAdapter);
                            }
                        }else {
                            rcv_msg = new MsgEntity(MsgEntity.RCV_MSG, "您的输入无效"+"\n"+"请重新输入想要进行的操作");
                            reciveMsg(rcv_msg,msgAdapter);
                        }
                    }
                    edt_msg.setText("");//清空消息输入框
                }
            }
        });
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
        MsgEntity msg1=new MsgEntity(MsgEntity.RCV_MSG,"欢迎您使用我们的股票软件！"+
                "\n"+"您可以通过发送指令跳转到相关界面进行操作"+
                "\n"+"想买入股票请回复[buy]"+
                "\n"+"想卖出股票请回复[sell]"+
                "\n"+"想查看股票详情请回复[detail]"+
                "\n"+"想查看您的订单信息请回复[orderlist]");
        list.add(msg1);
    }
    //初始化控件
    private void initView() {
        recyclerView=(RecyclerView)findViewById(R.id.recylerView);
        edt_msg=(EditText)findViewById(R.id.edt_msg);
        btn_send=(Button)findViewById(R.id.btn_send);
    }
}
