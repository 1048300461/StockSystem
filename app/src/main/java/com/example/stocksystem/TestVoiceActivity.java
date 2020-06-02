package com.example.stocksystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;

public class TestVoiceActivity extends AppCompatActivity {

    Button button;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_voice);

        //初始化sdk
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5eccbf6b");

        button = findViewById(R.id.test_bt);
        tv = findViewById(R.id.show_result);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSpeech(TestVoiceActivity.this);
            }
        });
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
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if(!isLast){
                    //解析语音
                    String result = parseVoice(recognizerResult.getResultString());
                    tv.setText(result);
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
        Voice voiceBean = gson.fromJson(resultString, Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    /**
     * 语音对象封装
     */
    public class Voice {

        public ArrayList<WSBean> ws;

        public class WSBean {
            public ArrayList<CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }

}
