package com.example.stocksystem;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class LoginActivity extends AppCompatActivity {
    private ImageView imageView_pwd_SOH;
    private EditText editText_pwd;
    private boolean eyeOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        imageView_pwd_SOH = findViewById(R.id.login_pwd_ShowOrHide);
        editText_pwd = findViewById(R.id.password);

        //密码的显示与隐藏
        imageView_pwd_SOH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eyeOpen) {
                    //密码 TYPE_CLASS_TEXT 和 TYPE_TEXT_VARIATION_PASSWORD 必须一起使用
                    editText_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageView_pwd_SOH.setImageResource(R.drawable.logo_pwd_hide);
                    eyeOpen = false;
                } else {
                    //明文
                    editText_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageView_pwd_SOH.setImageResource(R.drawable.logo_pwd_show);
                    eyeOpen = true;
                }
            }
        });

    }

    public void goSignIn(View view){
        Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
        startActivity(intent);
    }
}
