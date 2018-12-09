package com.example.drxzt.schoolvoice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drxzt.schoolvoice.Date.Post;
import com.example.drxzt.schoolvoice.adapter.MyAdapter;
import com.example.drxzt.schoolvoice.Date.Post;
import com.example.drxzt.schoolvoice.Date.User;
import com.example.drxzt.schoolvoice.utils.CustomLinearLayoutManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {
    private Button mDlhjm_geren, menu_sz;
    private Button mDlhjm_guanfang;
    private Button mDlhjm_button4;
    private Button wdgz,wdsc,wdtz;
    private ImageView mDlhjm_touxiang1,touxiang;


    private User user ;

    private RecyclerView recyclerView;
    private String content;
    private MyAdapter adapter;
    private AlertDialog al;
    private List<Post> mlist;
    private ListView lv;
    private SwipeRefreshLayout refresh;

    private RelativeLayout rl_post;
    private MediaPlayer mp;//播放提示音
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    private TextView tv_content,tv_User,nav_user,idname;
    AlertDialog.Builder builder;
    CustomLinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        idname=(TextView)findViewById(R.id.idname);
        mDlhjm_geren = (Button) findViewById(R.id.dlhjm_geren);
        mDlhjm_guanfang = (Button) findViewById(R.id.dlhjm_guanfang);
        mDlhjm_button4 = (Button) findViewById(R.id.dlhjm_button4);
        mDlhjm_touxiang1 = (ImageView) findViewById(R.id.dlhjm_touxiang1);
        menu_sz=(Button)findViewById(R.id.menu_sz);
        wdgz=(Button)findViewById(R.id.wdgz);
        touxiang=(ImageView)findViewById(R.id.touxiang) ;
        wdsc =(Button)findViewById(R.id.wdsc);
        wdtz =(Button)findViewById(R.id.wdtz);
            gethandImage();

        init();
        initl();

        mDlhjm_geren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NoteActivity.class);
                startActivity(intent);

            }
        });
        wdgz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ConcernActivity.class);
                startActivity(intent);

            }
        });
        wdtz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MyNoticeActivity.class);
                startActivity(intent);

            }
        });
        wdsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CollectionActivity.class);
                startActivity(intent);

            }
        });

        menu_sz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,UserActivity.class);
                startActivity(intent);

            }
        });

        mDlhjm_guanfang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);

            }
        });

        mDlhjm_touxiang1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, PersonActivity.class);
                startActivity(intent);

            }
        });
        mDlhjm_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, PutActivity.class);
                startActivity(intent);

            }
        });

    }

    private void gethandImage() {
        user = new User();
        user = BmobUser.getCurrentUser(User.class);
        idname.setText(user.getNickname());
        mDlhjm_touxiang1.setImageBitmap(returnBitMap( user.getImg_url()));
        touxiang.setImageBitmap(returnBitMap( user.getImg_url()));

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
    void init(){

        user = new User();
        user= BmobUser.getCurrentUser(User.class);
        refresh=(SwipeRefreshLayout)findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override            public void onRefresh() {

                linearLayoutManager.setScrollEnabled(false);


                getData();

            }
        });

    }
    public void initl(){
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        tv_content=(TextView)findViewById(R.id.dlhjm_neirong);
        refresh=(SwipeRefreshLayout)findViewById(R.id.refresh);
        rl_post=(RelativeLayout)findViewById(R.id.rl_post);
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager = new CustomLinearLayoutManager(MainActivity.this);

        BmobApplication.getInstance().addActivity(MainActivity.this);
        mlist=new ArrayList<Post>();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MyAdapter(MainActivity.this,mlist);
        recyclerView.setAdapter(adapter);

        getData();
        adapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(MainActivity.this,CommentActivity.class);
                intent.putExtra("name",data);
                startActivity(intent);
                // getActivity().finish();
            }
        });


    }
    private void getData() {
        // TODO Auto-generated method stub
        this.refresh.setRefreshing(true);
        final Post post = new Post();
        BmobQuery<Post> bq = new BmobQuery<Post>();
        bq.setLimit(8);
        bq.order("-updatedAt");
        bq.include("author");
        bq.include("read");
        bq.include("praise");
        bq.include("username");
        bq.include("time");
        bq.include("title");
        bq.include("img_url");
        bq.include("user.objectId");
        mlist.clear();
        bq.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null) {
                    for (Post p : list) {
                        p.getAuthor();
                        p.getRead();
                        p.getPraise();
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
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Material Design Dialog");

        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

}
