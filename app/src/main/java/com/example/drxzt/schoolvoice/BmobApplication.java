package com.example.drxzt.schoolvoice;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

//import com.imnjh.imagepicker.PickerConfig;
//import com.imnjh.imagepicker.SImagePicker;
import com.example.drxzt.schoolvoice.push.MyPushMessageReceiver;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

public class BmobApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static BmobApplication instance;
    //换上你自己的key
    public static String APPID = "72fd19877690a3a4fcf11333c2afe562";
    private BmobApplication() {
    }
    public synchronized static BmobApplication getInstance() {
        if (null == instance) {
            instance = new BmobApplication();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // System.exit(0);//去掉这个
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
    /**
     * SDK初始化也可以放到Application中
     */


    @Override
    public void onCreate() {
        super.onCreate();
        //TODO 集成：1.4、初始化数据服务SDK、初始化设备信息并启动推送服务
// 初始化BmobSDK
        Bmob.initialize(this, "你的Application Id");
// 使用推送服务时的初始化操作
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    //Logger.i(bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
                } else {
                  //  Logger.e(e.getMessage());
                }
            }
        });
// 启动推送服务
        BmobPush.startWork(this);
//设置BmobConfig
        BmobConfig config =new BmobConfig.Builder(BmobApplication.this)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(15)
                //文件分片上传时每片的大小（单位字节），默认512*1024

                .build();

    }


    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        IntentFilter filterl = new IntentFilter(Intent.ACTION_TIME_TICK);
        MyPushMessageReceiver receiverl = new MyPushMessageReceiver();
        registerReceiver(receiver, filterl);
        return super.registerReceiver(receiver, filter);
    }

}
