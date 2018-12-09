package com.example.drxzt.schoolvoice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drxzt.schoolvoice.Date.Person;
import com.example.drxzt.schoolvoice.adapter.MesAdapter;
import com.example.drxzt.schoolvoice.adapter.PersonAdapter;
import com.example.drxzt.schoolvoice.Date.Message;
import com.example.drxzt.schoolvoice.Date.Post;
import com.example.drxzt.schoolvoice.Date.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class PersonActivity extends AppCompatActivity {
    private Button mGrzx2_fanhui;
    private ImageView mGrzx2_mytouxiang;
    private Button mGrzx2_guanzhu,fanhui;
    private Button mGrzx2_fensi;
    private SwipeRefreshLayout mRefresh;
    private TextView show_data;

    private String objId;
    private ListView info_list;
    private List<Post> mlist;
    private String data_info[]={};
    private PersonAdapter adapter;
    private LinearLayout show_head;
    private SwipeRefreshLayout refresh;
    private LinearLayoutManager linearLayoutManager;
    private int mTouchSlop;
    private int com_num;
    private float mFirstY;
    private float mCurrentY;
    private int direction;
    private boolean mShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mGrzx2_mytouxiang = (ImageView) findViewById(R.id.grzx2_mytouxiang);
        mGrzx2_guanzhu = (Button) findViewById(R.id.grzx2_guanzhu);
        mGrzx2_fensi = (Button) findViewById(R.id.grzx2_fensi);
        show_data=(TextView) findViewById(R.id.show_data);
        refresh=(SwipeRefreshLayout)findViewById(R.id.per_refresh);
        info_list=(ListView) findViewById(R.id.info_list);
        fanhui=(Button)findViewById(R.id.fanhui);
        //gethandImage();

        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        mGrzx2_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonActivity.this,ConcernActivity.class);
                startActivity(intent);

            }
        });
        mGrzx2_fensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonActivity.this,FansActivity.class);
                startActivity(intent);

            }
        });

        linearLayoutManager = new LinearLayoutManager(PersonActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        objId=getIntent().getStringExtra("objId");
        mlist=new ArrayList<>();
        person_post();
        adapter= new PersonAdapter(mlist,this);
        info_list.setAdapter(adapter);
        adapter.notifyDataSetInvalidated();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override            public void onRefresh() {

                show_data.setText("已加载全部");
                adapter.notifyDataSetInvalidated();
                refresh.setRefreshing(false);
            }
        });
        info_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mFirstY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = event.getY();
                        if (mCurrentY - mFirstY > mTouchSlop) {
                            direction = 0;// down
                        } else if (mFirstY - mCurrentY > mTouchSlop) {
                            direction = 1;// up
                        }
                        if (direction == 1) {

                            //上滑todo

                        } else if (direction == 0) {

                            //下滑todo



                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }
                return false;
            }
        });

    }

    private void gethandImage() {
        User user =new User();
        user.setObjectId(objId);
        mGrzx2_mytouxiang.setImageBitmap(returnBitMap( user.getImg_url()));

    }
    public Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    void person_post(){
        User user =new User();
        user.setObjectId(objId);
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereEqualTo("author", user);    // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    for(Post p: list) {
                        p.getContent();
                        p.getTitle();
                        p.getTime();
                        p.getImg_url();
                        mlist.add(p);
                    }
                }else{
                    toast(e.toString());
                }
            }
        });

    }
    /**
    public void go_user_photo(View view){
        Intent intent =new Intent(PersonActivity.this,UserPhoto.class);
        intent.putExtra("objid",objId);
        startActivity(intent);
    }**/
    public void toast(String msg) {
        Toast.makeText(PersonActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                PersonActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

