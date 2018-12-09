package com.example.drxzt.schoolvoice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.drxzt.schoolvoice.Date.Notify;
import com.example.drxzt.schoolvoice.Date.Post;
import com.example.drxzt.schoolvoice.Date.User;
import com.example.drxzt.schoolvoice.adapter.NoticeAdapter;
import com.example.drxzt.schoolvoice.utils.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.lang.String;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NoticeActivity extends AppCompatActivity {

    private User user ;
    private Button not_put,fanhui,button8;
    private String platform;
    private RecyclerView recyclerView;
    private NoticeAdapter adapter;
    private AlertDialog al;
    private List<Notify> mlist;
    private SwipeRefreshLayout refresh;

    private RelativeLayout not_post;
    private MediaPlayer mp;//播放提示音
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    private TextView not_content,not_User,not_user;
    AlertDialog.Builder builder;
    CustomLinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        not_put=(Button)findViewById(R.id.not_put);
        button8=(Button)findViewById(R.id.button8);
        platform = getIntent().getStringExtra("platform");
        button8.setText(platform);

        init();
        initl();

        fanhui=(Button)findViewById(R.id.fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        not_put.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeActivity.this,NoticePutActivity.class);
                intent.putExtra("platform",platform);
                startActivity(intent);
            }
        });
    }
    void init(){

        user = new User();
        user= BmobUser.getCurrentUser(User.class);

        if (user.getRoot().contains(platform)) {
            not_put.setVisibility(View.VISIBLE);
        }else{
            not_put.setVisibility(View.GONE);//控制发布按钮不可见
        }
        refresh=(SwipeRefreshLayout)findViewById(R.id.not_refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override            public void onRefresh() {

                linearLayoutManager.setScrollEnabled(false);


                getData();

            }
        });

    }
    public void initl(){
        recyclerView=(RecyclerView)findViewById(R.id.not_recyclerView);
        not_content=(TextView)findViewById(R.id.not_neirong);
        refresh=(SwipeRefreshLayout)findViewById(R.id.not_refresh);
        not_post=(RelativeLayout)findViewById(R.id.not_post);
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager = new CustomLinearLayoutManager(NoticeActivity.this);

        BmobApplication.getInstance().addActivity(NoticeActivity.this);
        mlist=new ArrayList<Notify>();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new NoticeAdapter(NoticeActivity.this,mlist);
        recyclerView.setAdapter(adapter);

        getData();
        adapter.setOnItemClickListener(new NoticeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(NoticeActivity.this,AskActivity.class);
                intent.putExtra("name",data);
                startActivity(intent);
                // getActivity().finish();
            }
        });


    }
    private void getData() {
        // TODO Auto-generated method stub
        this.refresh.setRefreshing(true);
        final Notify post = new Notify();
        BmobQuery<Notify> bq = new BmobQuery<Notify>();
        bq.addWhereEqualTo("platform",platform);
        bq.setLimit(10);
        bq.order("-updatedAt");
        bq.include("author");
        bq.include("username");
        bq.include("time");
        bq.include("title");
        bq.include("img_url");
        bq.include("user.objectId");
        mlist.clear();
        bq.findObjects(new FindListener<Notify>() {
            @Override
            public void done(List<Notify> object, BmobException e) {
                if(e==null){
                    for (Notify p : object) {
                        p.getContent();
                        p.getName();
                        p.getTitle();
                        p.getTime();
                        mlist.add(p);

                        refresh.setRefreshing(false);
                        linearLayoutManager.setScrollEnabled(true);
                        adapter.notifyDataSetChanged();

                    }
                }else{
                    toast("加载失败" );
                    refresh.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }


    public void toast(String msg) {
        Toast.makeText(NoticeActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
