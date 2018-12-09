package com.example.drxzt.schoolvoice;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drxzt.schoolvoice.Date.Note;
import com.example.drxzt.schoolvoice.Date.Post;
import com.example.drxzt.schoolvoice.Date.User;
import com.example.drxzt.schoolvoice.adapter.NoteAdapter;
import com.example.drxzt.schoolvoice.adapter.PersonAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NoteActivity extends AppCompatActivity {

    private TextView show_data2;
    private Button fanhui,bq_add;


    private ListView info_list;
    private List<Note> mlist;
    private String data_info[]={};
    private NoteAdapter adapter;
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
        setContentView(R.layout.activity_note);
        show_data2=(TextView)findViewById(R.id.show_data2);
        refresh=(SwipeRefreshLayout)findViewById(R.id.note_refresh);
        info_list=(ListView) findViewById(R.id.note_list);
        fanhui=(Button)findViewById(R.id.fanhui);
        bq_add =(Button)findViewById(R.id.bq_add);

        bq_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NoteActivity.this,PutNoteActivity.class);
                startActivity(intent);

            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        linearLayoutManager = new LinearLayoutManager(NoteActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        person_post();
        mlist=new ArrayList<>();
        adapter= new NoteAdapter(mlist,this);
        info_list.setAdapter(adapter);
        adapter.notifyDataSetInvalidated();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override            public void onRefresh() {

                show_data2.setText("已加载全部");
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





    void person_post(){
        User user =new User();
        user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Note> query = new BmobQuery<Note>();
        query.addWhereEqualTo("user", user);    // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.include("user");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.include("content");
        query.include("time");
        query.include("remindtime");
        query.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> list, BmobException e) {
                if (e==null){
                    for(Note p: list) {
                        p.getComplete();
                        p.getContent();
                        p.getRemindtime();
                        p.getTime();

                        mlist.add(p);
                    }
                }else{
                    toast(e.toString());
                }
            }
        });

    }
    public void toast(String msg) {
        Toast.makeText(NoteActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
