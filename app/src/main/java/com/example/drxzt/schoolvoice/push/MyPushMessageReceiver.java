package com.example.drxzt.schoolvoice.push;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.drxzt.schoolvoice.MainActivity;
import com.example.drxzt.schoolvoice.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2017/5/24.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {
    private NotificationManager managerl;
    NotificationCompat.Builder notifyBuilder;
    @SuppressWarnings("deprecation")

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {

            //检查Service状态
            boolean isServiceRunning = false;
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE)) {
                if("cn.bmob.push.action.MESSAGE".equals(service.service.getClassName()))
                //Service的类名
                {
                    isServiceRunning = true;
                }

            }
            if (!isServiceRunning) {
                Intent i = new Intent(context, MyPushMessageReceiver.class);
                context.startService(i);
            }
        }
        String message="";
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String msg=intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            JSONTokener js=new JSONTokener(msg);
            try{
                JSONObject object =(JSONObject) js.nextValue();
                message=object.getString("alert");

            }
            catch(JSONException e){

                e.printStackTrace();
            }

            managerl = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            //点击的意图ACTION是跳转到Intent
            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //   Bitmap bitmap = BitmapFactory.decodeResource(getResultCode(),R.mipmap.ic_launcher);
            notifyBuilder = new NotificationCompat.Builder(context);
            notifyBuilder .setSmallIcon(R.drawable.aixin1)
                    /*设置title*/
                    .setContentTitle("SchoolVoice")
                    /*设置详细文本*/
                    .setContentText(message)
                    //使用默认的声音

                    //直接进入主界面
                    //.setFullScreenIntent(pendingIntent, true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    /*设置发出通知的时间为发出通知时的系统时间*/
                    .setWhen(System.currentTimeMillis())
                    /*设置发出通知时在status bar进行提醒*/
                    //  .setTicker("来自问月的祝福")
                    /*setOngoing(boolean)设为true,notification将无法通过左右滑动的方式清除 * 可用于添加常驻通知，必须调用cancle方法来清除 */
                    .setOngoing(false)
                    /*设置点击后通知消失*/
                    .setAutoCancel(true)
                    /*设置通知数量的显示类似于QQ那种，用于同志的合并*/
                    .setNumber(2)
                    /*点击跳转到MainActivity*/
                    .setContentIntent(pendingIntent);

            managerl.notify(1, notifyBuilder.build());
        }

    }



}