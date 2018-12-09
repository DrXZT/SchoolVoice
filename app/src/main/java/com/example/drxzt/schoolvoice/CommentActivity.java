package com.example.drxzt.schoolvoice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drxzt.schoolvoice.adapter.CommentAdapter;
import com.example.drxzt.schoolvoice.Date.Comment;
import com.example.drxzt.schoolvoice.Date.Post;
import com.example.drxzt.schoolvoice.Date.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class CommentActivity extends AppCompatActivity {
    private TextView username, content,p_time,read;
    private ListView listView;
    private User user;
    private EditText ed_comm;
    private TextView tvPri;

    private int mTouchSlop;
    private int com_num;
    private float mFirstY;
    private float mCurrentY;
    private int direction;
    private boolean mShow = true;
    private AlertDialog al;
    private EditText commentContent;
    private Button btn_comm;
    private List<Comment> list = new ArrayList<Comment>();
    private CommentAdapter adapter;
    private Post weibo = new Post();
    private String obj;
    private String com_user_info, dialog_url;
    private String time;
    private LinearLayout boot_edit;
    private String post_obj, user_obj;
    private String user_data[] = {};
    private String url;
    private String img_url;
    private ImageView comm_img,com_touxiang;
    AlertDialog.Builder dialog;
    LinearLayout.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        username = (TextView) findViewById(R.id.com_mingzi);
        content = (TextView) findViewById(R.id.com_neirong);
        p_time = (TextView) findViewById(R.id.com_time);
        ed_comm = (EditText) findViewById(R.id.ed_comm);
        listView = (ListView) findViewById(R.id.lv_comm);
        com_touxiang = (ImageView) findViewById(R.id.com_touxiang);
        boot_edit = (LinearLayout) findViewById(R.id.area_commit);
        tvPri=(TextView)findViewById(R.id.com_read);
        comm_img = (ImageView) findViewById(R.id.com_tupian);
        adapter = new CommentAdapter(list, this);
        listView.setAdapter(adapter);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        String name = getIntent().getStringExtra("name");
        String[] sourceStrArray = name.split(",");
        String signName = "";
        // 最多分割出4个字符串
        int maxSplit = 8;
        sourceStrArray = name.split(",", maxSplit);
        img_url = sourceStrArray[4];

        if (img_url == null) {
            comm_img.setVisibility(View.GONE);

        }
//        0.post.getContent() + "," + 1.post.getName() + "," + 2.post.getObjectId() + "," + 3.post.getPraise()
//                            + "," + 4.post.getImg_url()+","+5.post.getRead()+","+6.post.getTime()+","+7.post.getAuthor().getImg_url()
        content.setText(sourceStrArray[0]);
        username.setText(sourceStrArray[1]);
        p_time.setText(sourceStrArray[6]);
        Glide.with(CommentActivity.this).load(R.drawable.default_logo).into(com_touxiang);
        obj = sourceStrArray[2];
        if (sourceStrArray[3] == null) {
            tvPri.setText("阅览："+0);
        }
       tvPri.setText("阅览："+sourceStrArray[5] );
        weibo.setObjectId(obj);
        weibo.increment("read", 1);
        weibo.update("obj", new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","更新成功");
                }else{
                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        if (img_url != null) {
            comm_img.setVisibility(View.VISIBLE);
            dialog_url = sourceStrArray[4];
            Glide.with(CommentActivity.this).load(sourceStrArray[4]).into(comm_img);

        }

        findComments();
        getUrl();
        update();
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
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
                            boot_edit.setVisibility(View.INVISIBLE);
                            //comm_img.setVisibility(View.GONE);
                            // tv_show_gone1.setVisibility(View.GONE);
                            //tv_show_gone2.setVisibility(View.VISIBLE);
                        } else if (direction == 0) {

                            //下滑todo
                            boot_edit.setVisibility(View.VISIBLE);


                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }
                return false;
            }
        });


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
    public void show_imgs(View view) {
        //∨
        //comm_img.setVisibility(View.VISIBLE);
        //tv_show_gone1.setVisibility(View.VISIBLE);
        // tv_show_gone2.setVisibility(View.GONE);

    }

    public void gone_imgs(View view) {
        //∨
       // comm_img.setVisibility(View.GONE);
        // tv_show_gone1.setVisibility(View.GONE);
        //tv_show_gone2.setVisibility(View.VISIBLE);

    }

    public void putcomm(View v) {

        String content = ed_comm.getText().toString();
        publishComment(content);
    }

    public void getUrl() {
        showDialog();
        final String[] obj_info = {""};
        final BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", username.getText().toString());
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> list, BmobException e) {
                al.dismiss();
                for (BmobUser data : list) {
                    obj_info[0] = data.getObjectId();
                    url = obj_info[0];
                }
            }

        });
    }

    public void user_info_to(View view) {
        Intent intent = new Intent(CommentActivity.this, PersonActivity.class);
        intent.putExtra("objId", url);
        startActivity(intent);
    }

    public void reply2(View v) {
        ed_comm.requestFocus();
        InputMethodManager imm = (InputMethodManager) ed_comm.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public void share(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, content.getText().toString() + "_" + username.getText() + "_ _来自iTell");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享"));
    }

    public void toast(String msg) {
        Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            CommentActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void reply1(View view) {
        ed_comm.requestFocus();

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(ed_comm, 0);
    }


    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(ed_comm.getWindowToken(), 0);
    }

    private void publishComment(String content) {
        getTime();
        user = new User();
        user = BmobUser.getCurrentUser(User.class);
        if (user == null) {
            toast("发表评论前请先登陆");
            return;
        } else if (TextUtils.isEmpty(content)) {
            toast("发表评论不能为空");
            return;
        }
        showDialog_com();
        final Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(weibo);
        comment.setUser(user);
        comment.setTime(time);
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    findComments();
                    al.dismiss();
                    toast("评论成功");
                    adapter.notifyDataSetInvalidated();
                    ed_comm.setText("");
                } else {
                    toast(e.toString());
                }
            }
        });

    }

    private void findComments() {

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        list.clear();
        Post post = new Post();
        post.setObjectId(obj);
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.include("user,,author,post.author,comment.time,comment.user");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> arg0, BmobException e) {
                if (e == null) {

                    list.addAll(arg0);
                    com_num = list.size();
                    adapter.notifyDataSetInvalidated();
                } else {

                    toast("查询评论失败");
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    //点赞功能
    /*public void praise(View v){
        if (user==null){
            toast("需要登录才能点赞哦");
            return;
        }
        Post post = new Post();
        BmobUser user = BmobUser.getCurrentUser();
        BmobQuery<Post> bq=new BmobQuery<Post>();
        bq.addWhereEqualTo("objectId", post.getObjectId());
        bq.addWhereContains("praiseInfo", user.getObjectId());
        bq.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> mlist, BmobException e) {
                if(mlist.size()>0&& mlist!=null){
                    toast("你已经赞过啦!");
                    return;
                }else{
                    update();
                }
            }
        });

    }*/

    /**
     * 更新点赞信息
     */
    public void update() {
        Post post = new Post();
        BmobUser user = BmobUser.getCurrentUser();
        post.setObjectId(obj);
        // TODO Auto-generated method stub
        post.increment("read", 1);
        post.addUnique("praiseInfo", user.getObjectId());
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //toast("点赞成功！");
                } else {
                    // toast("点赞失败！");
                }
            }
        });

    }

    public void showImg(View view) {
        show_dialogimg();
        if (img_url == null) {
            comm_img.setVisibility(View.GONE);

        }
    }

    public void show_dialogimg() {
        // 首先得到整个View
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_comm_img, null);
        ImageView diaimageView = (ImageView) view.findViewById(R.id.dialog_img);
        Glide.with(CommentActivity.this).load(dialog_url).placeholder(R.mipmap.loading).into(diaimageView);
        al = new AlertDialog.Builder(this)
                .setView(R.layout.dialog_comm_img)
                .setView(view)
                .show();
    }

    public void getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 hh点");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        time = formatter.format(curDate);
    }

    private void showDialog() {
        // 首先得到整个View
        LayoutInflater inflater = getLayoutInflater();
        al = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("数据装载中...")
                .setView(R.layout.dialog_com)
                .setCancelable(true)
                .show();

    }

    private void showDialog_com() {
        LayoutInflater inflater = getLayoutInflater();
        al = new AlertDialog.Builder(this)
                .setTitle("回复评论中...")
                .setView(R.layout.dialog_com)
                .show();

    }
}
