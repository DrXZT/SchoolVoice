package com.example.drxzt.schoolvoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.drxzt.schoolvoice.Date.User;

import cn.bmob.v3.BmobUser;

public class Main2Activity extends AppCompatActivity {
    private Button mGftzfjjm_button_xx,fanhui;
    private Button mGftzfjjm_button_xy;
    private Button mGftzfjjm_button_zy;
    private Button mGftzfjjm_button_st;

    private User user;
    private String major, college,association,banji,school;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mGftzfjjm_button_xx = (Button) findViewById(R.id.gftzfjjm_button_xx);
        mGftzfjjm_button_xy = (Button) findViewById(R.id.gftzfjjm_button_xy);
        mGftzfjjm_button_zy = (Button) findViewById(R.id.gftzfjjm_button_zy);
        mGftzfjjm_button_st = (Button) findViewById(R.id.gftzfjjm_button_st);
        user = new User();
        user= BmobUser.getCurrentUser(User.class);
        major=user.getMajor();
        college=user.getCollege();
        association=user.getAssociation();
        banji=user.getBanji();
        school=user.getSchool();
        fanhui=(Button)findViewById(R.id.fanhui2);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        mGftzfjjm_button_xx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this, NoticeActivity.class);
                intent.putExtra("platform",school);
                startActivity(intent);

            }
        });
        mGftzfjjm_button_xy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this, NoticeActivity.class);
                intent.putExtra("platform",college);
                startActivity(intent);

            }
        });

        mGftzfjjm_button_zy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this, NoticeActivity.class);
                intent.putExtra("platform",major);
                startActivity(intent);

            }
        });

        mGftzfjjm_button_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this, NoticeActivity.class);
                intent.putExtra(" platform", association);
                startActivity(intent);

            }
        });


    }

}
