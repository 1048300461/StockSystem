package com.example.stocksystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stocksystem.ChatMainPage.ChatMainPageActivity;
import com.example.stocksystem.bean.User;
import com.example.stocksystem.dao.LoginOrSignInDao;
import com.example.stocksystem.dao.UserDao;
import com.example.stocksystem.dao.impl.LoginOrSignInDaoImpl;
import com.example.stocksystem.dao.impl.UserDaoImpl;


public class LoginActivity extends AppCompatActivity {
    private ImageView imageView_pwd_SOH;
    private EditText editText_pwd;
    private EditText editText_username;
    private boolean eyeOpen = false;
    private Spinner spinner;

    private Button btn_login;   //登录按钮
    private ProgressDialog progressDialog;//访问数据进程框
    private User user;//用于存储sql返回的user
    private boolean IsUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        boolean isLogin = isLogin();
        if(isLogin){
            goMainActivity();
        }

        imageView_pwd_SOH = findViewById(R.id.login_pwd_ShowOrHide);
        editText_pwd = findViewById(R.id.login_editText_password);
        editText_username = findViewById(R.id.login_editText_username);

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

        //下拉框选择用户类型
        spinner = findViewById(R.id.login_spinner_type);
        String strType[] = {"点击选择用户类型","普通用户","管理员"};   //1为普通用户，2为管理员
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.login_simple_spinner_item,strType);
        adapter.setDropDownViewResource(R.layout.login_simple_spinner_item);
        spinner.setAdapter(adapter);

        //登录点击事件

        btn_login = findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //账号和密码为空，用户类型未选择--》异常判定
                if (editText_pwd.getText().toString().equals("") || editText_username.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this,"账号或密码不能为空！！！",Toast.LENGTH_LONG).show();
                else if (spinner.getSelectedItemId()==0){
                    Toast.makeText(LoginActivity.this,"请选择用户类型！！！",Toast.LENGTH_LONG).show();
                }else {
                    LoginAcsncTask loginAcsncTask = new LoginAcsncTask();
                    loginAcsncTask.execute();
                }
            }
        });

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

    //登录访问数据库的耗时操作
    private User login(){
        LoginOrSignInDao loginOrSignInDao = new LoginOrSignInDaoImpl();
        User user = new User();
        user.setLogin_name(editText_username.getText().toString());
        user.setPasswd(editText_pwd.getText().toString());
        user.setType(Integer.parseInt(spinner.getSelectedItemId()+""));
        Log.d("user=",user.toString());
        IsUser = loginOrSignInDao.IsUser(user);
        if (IsUser)
            return loginOrSignInDao.IsLogin(user);
        else
            return null;

    }

    private class LoginAcsncTask extends AsyncTask<String,Integer,String>{

        /**
         * onPreExecute方法用于执行后台任务前的UI操作
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
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
            user = login();
            return null;
        }

        /**
         * onProgressUpdata方法用于更新进度信息
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        /**
         * onPostExecute方法用于在执行完后更新UI，显示结果
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            if(IsUser)
            {
                if (user==null)
                    Toast.makeText(LoginActivity.this,"密码错误！请重新输入",Toast.LENGTH_SHORT).show();
                else if (user.getType()!=spinner.getSelectedItemId()-1)
                {
                    Toast.makeText(LoginActivity.this,"用户类型选择错误！请重新选择",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    goMainActivity();
                }
            }else
                Toast.makeText(LoginActivity.this,"用户名不存在！",Toast.LENGTH_SHORT).show();
        }

        /**
         * onCancelled()方法用于在取消执行中任务时更改UI
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }

    //登录跳转Activity
    private void goMainActivity(){
        Intent intent = new Intent(LoginActivity.this, ChatMainPageActivity.class);
        writeLogin();
        startActivity(intent);
        finish();
    }


    //注册跳转Activit
    public void goSignIn(View view){
        Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    /**
     * 判断是否登入过
     * @return
     */
    private boolean isLogin(){
        SharedPreferences sp = getSharedPreferences("isLogin",MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        return isLogin;
    }

    /**
     * 写入登入
     */
    private void writeLogin(){
        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.apply();
    }
}
