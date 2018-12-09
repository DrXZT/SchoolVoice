package com.example.drxzt.schoolvoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drxzt.schoolvoice.Date.Person;
import com.example.drxzt.schoolvoice.Date.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private String UserName ,Password ;
    private EditText name,password;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "72fd19877690a3a4fcf11333c2afe562");
        enter =(Button)findViewById(R.id.enter_button);
        name =(EditText) findViewById(R.id.username);
        password =(EditText) findViewById(R.id.password);
        enter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                login();
            }
        });
      /** BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        .setApplicationId("72fd19877690a3a4fcf11333c2afe562")
        ////请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
**/

/**
        enter =(Button)findViewById(R.id.enter_button);
        name =(EditText) findViewById(R.id.username);
        password =(EditText) findViewById(R.id.password);
        UserName=name.getText().toString();
        Password=password.getText().toString();

        User user = new User();
        user.setUsername(UserName);
        user.setPassword(Password);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.loginByAccount(UserName, Password, new LogInListener<BmobUser>() {

                    @Override
                    public void done(BmobUser user, BmobException e) {
                        if(user!=null){
                           toast("用户登陆成功");
                        }
                        else{
                            toast("用户登陆失败");
                        }
                    }
                });

            }
        });    **/

    }


    private void login() {
        // TODO Auto-generated method stub
        UserName=name.getText().toString();
        Password=password.getText().toString();
        if (UserName.isEmpty()) {
            toast("用户名为空");
            return;
        }
        if (Password.isEmpty()) {
            toast("密码为空");
            return;
        }
        // isChecked = true;


        User user = new User();
        user.setUsername(UserName);
        user.setPassword(Password);
        user.login(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    toast("登录成功！");
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                }else{
                    toast("登录失败！ " + e.toString());
                }
            }
        });

    }

    public void toast(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
