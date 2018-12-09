package com.example.drxzt.schoolvoice;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drxzt.schoolvoice.Date.Note;
import com.example.drxzt.schoolvoice.Date.Post;
import com.example.drxzt.schoolvoice.Date.User;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PutNoteActivity extends AppCompatActivity {
    private Button mFanhui;
    private Button mBiaoti,fanhui;
    private TextView mTextView,currentime;
    private Switch mTiXin;
    private Button mWcbutton;
    private TextView mNeirong_pbq;
    private EditText mShuruchu;
    private TextView mTime_pbq;
    private RelativeLayout selectime;

    private String content;
    private String time,remindtime;


    private String name;
    private Note note;
    private User user;
    private TimePickerDialog mDialogAll,mDialogYearMonthDay;

    long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;

    SimpleDateFormat sf = new SimpleDateFormat("MM月dd日 HH点mm分");
    BmobPushManager<BmobInstallation> bmobPush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_note);

        bmobPush = new BmobPushManager<BmobInstallation>();
        user = new User();
        user = BmobUser.getCurrentUser(User.class);
        selectime=(RelativeLayout)findViewById(R.id.selectime);
        mFanhui = (Button) findViewById(R.id.fanhui);
        mBiaoti = (Button) findViewById(R.id.biaoti);
        mTextView = (TextView) findViewById(R.id.textView);
        mTiXin=(Switch)findViewById(R.id.txw);
        mWcbutton = (Button) findViewById(R.id.wcbutton);
        mNeirong_pbq = (TextView) findViewById(R.id.neirong_pbq);
        mShuruchu = (EditText) findViewById(R.id.shuruchu);
        //mTime_pbq = (TextView) findViewById(R.id.time_pbq);
        currentime =(TextView)findViewById(R.id.currentime);
        mTiXin.setChecked(false);
        mTiXin.setSwitchTextAppearance(PutNoteActivity.this,R.style.s_false);
        mTiXin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    mTiXin.setSwitchTextAppearance(PutNoteActivity.this,R.style.s_true);
                    selectime.setVisibility((View.VISIBLE));
                }else {
                    mTiXin.setSwitchTextAppearance(PutNoteActivity.this,R.style.s_false);
                    selectime.setVisibility((View.GONE));
                }
            }
        });

        note=new Note();
        currentime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialogAll = new TimePickerDialog.Builder()
                        .setType(Type.MONTH_DAY_HOUR_MIN)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                String text = getDateToString(millseconds);
                                remindtime=text;
                                currentime.setText(text);
                            }
                        })
                        .setCancelStringId("取消")
                        .setSureStringId("确定")
                        .setTitleStringId("自定义提醒时间")
                        .setMonthText("月")
                        .setDayText("日")
                        .setHourText("时")
                        .setMinuteText("分")
                        .setCyclic(false)
                        .setMinMillseconds(System.currentTimeMillis())
                        .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(getResources().getColor(R.color.accent_blue))
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.accent_blue))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.accent_blue))
                        .setWheelItemTextSize(12)
                        .build();

                mDialogAll.show(getSupportFragmentManager(), "time");

            }
        });


        mWcbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (user == null) {
                    toast("发表前先登录");
//                    Intent intent = new Intent();
//                    intent.setClass(EditActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                    return;
                }
                if ( mShuruchu.getText().toString().equals("")) {
                    toast("内容不能为空");
                    return;

                }
                name = user.getUsername();
                content =  mShuruchu.getText().toString();
                getTime();
                reply();
            }
        });
    }

    private void reply() {
        note.setContent(content);
        note.setTime(time);
        note.setRemindtime(remindtime);
        note.setUser(user);
        note.setComplete(false);
        note.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    toast("添加成功");
                    pushToAndroid(user.getUsername()+"有新的行程了，快去看看吧");
                    PutNoteActivity.this.finish();
                    MainActivity mainActivity=new MainActivity();
                    mainActivity.finish();
                }else {
                    toast("发帖失败"+e.toString());
                }
            }
        });

    }
    private void pushToAndroid(String message) {
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("deviceType", "android");
        bmobPush.setQuery(query);
        bmobPush.pushMessage(message);
    }



    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
    public void toast(String msg) {
        Toast.makeText(PutNoteActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    public void getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 hh点 mm分");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        time = formatter.format(curDate);
    }
}
