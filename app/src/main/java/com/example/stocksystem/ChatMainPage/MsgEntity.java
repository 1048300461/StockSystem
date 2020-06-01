package com.example.stocksystem.ChatMainPage;

public class MsgEntity {
    //分别代表发送和接受消息的类型
    public static final int SEND_MSG=1;
    public static final int RCV_MSG=2;

    //消息内容
    private String content;
    //消息类型
    private int type;

    public MsgEntity(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
