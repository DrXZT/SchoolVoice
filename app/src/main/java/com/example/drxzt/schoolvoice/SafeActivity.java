package com.example.drxzt.schoolvoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drxzt.schoolvoice.Date.User;

import cn.bmob.v3.BmobUser;

public class SafeActivity extends AppCompatActivity {
    private TextView mZhyaq_nickname;
    private TextView mZhyaq_realname;
    private TextView mZhyaq_collage;
    private TextView mZhyaq_banji;
    private TextView mZhyaq_scid,zhyaq_changpw,zhyaq_landrecord;
    private String objId;
    private Button zhyaq_fanhui;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        mZhyaq_nickname = (TextView) findViewById(R.id.zhyaq_nickname);
        mZhyaq_realname = (TextView) findViewById(R.id.zhyaq_realname);
        mZhyaq_collage = (TextView) findViewById(R.id.zhyaq_collage);
        mZhyaq_banji = (TextView) findViewById(R.id.zhyaq_banji);
        mZhyaq_scid = (TextView) findViewById(R.id.zhyaq_scid);
        zhyaq_changpw=(TextView)findViewById(R.id.zhyaq_changpw);
        zhyaq_landrecord=(TextView)findViewById(R.id.zhyaq_landrecord);
        zhyaq_fanhui=(Button)findViewById(R.id.zhyaq_fanhui);
        user = new User();
        user= BmobUser.getCurrentUser(User.class);
                    mZhyaq_nickname.setText(user.getNickname());
                    mZhyaq_realname.setText(user.getRealname());
                    mZhyaq_banji.setText(user.getBanji());
                    mZhyaq_collage.setText(user.getCollege());
                    mZhyaq_scid.setText(user.getUsername());
        zhyaq_changpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SafeActivity.this, ChangePWActivity.class);
                startActivity(intent);
            }
        });

        zhyaq_landrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SafeActivity.this, LandingRecordActivity.class);
                startActivity(intent);

            }
        });

        zhyaq_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


    }



    public void toast(String msg) {
        Toast.makeText(SafeActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
