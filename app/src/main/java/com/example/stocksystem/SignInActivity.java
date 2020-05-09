package com.example.stocksystem;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class SignInActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageView imageView_sure;
    private EditText editText;
    private EditText editText_sure;

    private TextView textView;//两次输入密码提示
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        //赋值
        imageView = findViewById(R.id.sign_in_pwd_ShowOrHide);
        imageView_sure = findViewById(R.id.sign_in_pwd_ShowOrHide_sure);
        editText = findViewById(R.id.sign_in_password);
        editText_sure = findViewById(R.id.sign_in_sure_password);
        textView = findViewById(R.id.sign_in_tag);
        //调用显示或隐藏密码方法
        ShowOrHide(editText,imageView);
        ShowOrHide(editText_sure,imageView_sure);


        //设置确认密码监听
        editText_sure.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editText.getText()+"";
                String str_sure = editText_sure.getText()+"";
                if (!str.equals(str_sure))
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.GONE);
            }
        });


    }

    //设置隐藏或显示密码
    private boolean eyeOpen=false;
    public void ShowOrHide(final EditText editText , final ImageView imageView){


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eyeOpen) {
                    //密码 TYPE_CLASS_TEXT 和 TYPE_TEXT_VARIATION_PASSWORD 必须一起使用
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageView.setImageResource(R.drawable.logo_pwd_hide);
                    eyeOpen = false;
                } else {
                    //明文
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageView.setImageResource(R.drawable.logo_pwd_show);
                    eyeOpen = true;
                }
            }
        });

    }

    //按钮关闭当前activity
    public void close(View view){
        finish();
    }
}
