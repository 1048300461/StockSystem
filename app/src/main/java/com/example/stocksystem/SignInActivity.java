package com.example.stocksystem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocksystem.bean.User;
import com.example.stocksystem.dao.LoginOrSignInDao;
import com.example.stocksystem.dao.impl.LoginOrSignInDaoImpl;


public class SignInActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageView imageView_sure;
    private EditText editText;
    private EditText editText_sure;
    private EditText editText_username;
    private Button btn_signIn;

    ProgressDialog progressDialog ;//访问数据库时的进程对话框
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
        editText_username = findViewById(R.id.sign_in_username);
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

        //注册按钮点击事件
        btn_signIn = findViewById(R.id.SignIn_btn_signIn);
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editText_username.getText()+"";
                pwd = editText_sure.getText()+"";
                SignInAcsncTask signInAcsncTask = new SignInAcsncTask();
                signInAcsncTask.execute();
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


    private boolean isUser = false; //sql中是否存在用户
    private boolean isSignSql = false;  //是否注册成功
    private  LoginOrSignInDao signInDao = new LoginOrSignInDaoImpl();
    private String username = "";
    private String pwd = "";
    //访问数据库操作
    private void SignIn(){

        User user = new User();     //时间由dao层直接赋值
        user.setUser_id(10000001+signInDao.countUser());
        user.setType(0);//为普通用户
        user.setLogin_name(username);
        user.setName(username);
        user.setPasswd(pwd);
        user.setCny_freezed(0); //资金默认为0
        user.setCny_free(0);
        user.setTemp(0);
        isSignSql = signInDao.IsSignIn(user);
    }
    //AsyncTask访问数据库
    private class SignInAcsncTask extends AsyncTask<String,Integer,String> {

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SignInActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);      //设置进度条风格，风格为圆形，旋转的
            progressDialog.setTitle("提示");
            progressDialog.setMessage("加载数据中。。。");
            progressDialog.setIndeterminate(true);  //设置ProgressDialog 的进度条是否不明确
            progressDialog.setCancelable(false); //设置ProgressDialog 是否可以按退回按键取消
            progressDialog.show();
        }

        /**
         * doInBackground方法内部执行后台任务，不可在此方法中更新主UI
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            User user = new User();//用于判断是否已经注册
            user.setLogin_name(username);
            if (signInDao.IsUser(user))
            {
                isUser = true;
                return null;
            }else{
                SignIn();
                return null;
            }


        }

        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();
            if(isUser) {
                Toast.makeText(SignInActivity.this,"用户已经存在！！！",Toast.LENGTH_SHORT).show();
            }else
            {
                if (isSignSql)
                {
                    Toast.makeText(SignInActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(SignInActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                }
            }


        }

    }

    //防止进程对话框出错（that was originally added here）
    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null ) {
            progressDialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
    //按钮关闭当前activity
    public void close(View view){
        finish();
    }
}
