package com.example.drxzt.schoolvoice;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drxzt.schoolvoice.Date.Notify;
import com.example.drxzt.schoolvoice.Date.Post;
import com.example.drxzt.schoolvoice.Date.User;

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

public class NoticePutActivity extends AppCompatActivity {
    private Button mButton_fanhui;
    private TextView mTextView;
    private Button mButton_aite;
    private Button mButton_tupian;
    private Button mButton_add;
    private EditText mEd_contents;
    private Button mButton;
    private ImageView mEdit_img;

    private String content, platform;
    private ImageView image;
    private String time;


    private String name;
    private Notify notify;
    private User user;
    ProgressDialog dialog = null;
    private FloatingActionButton fab;
    private static int RESULT_LOAD_IMAGE = 1;
    private AlertDialog al;
    BmobPushManager<BmobInstallation> bmobPush;
    public static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_put);
        platform = getIntent().getStringExtra("platform");
        bmobPush = new BmobPushManager<BmobInstallation>();
        user = new User();
        user = BmobUser.getCurrentUser(User.class);


        mButton_fanhui = (Button) findViewById(R.id.Button_fanhui);
        mTextView = (TextView) findViewById(R.id.textView);
        mButton_aite = (Button) findViewById(R.id.Button_aite);
        mButton_tupian = (Button) findViewById(R.id.button_tupian);
        mButton_add = (Button) findViewById(R.id.button_add);
        mEd_contents = (EditText) findViewById(R.id.ed_contents);
        mButton = (Button) findViewById(R.id.button);
        image = (ImageView) findViewById(R.id.edit_img);

        content = mEd_contents.getText().toString();
        notify = new Notify();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mButton_tupian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*选择图片  */
                if (ContextCompat.checkSelfPermission(NoticePutActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NoticePutActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }

            }
        });

        mButton_add.setOnClickListener(new View.OnClickListener() {

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
                if ( mEd_contents.getText().toString().equals("")) {
                    toast("内容不能为空");
                    return;

                }
                name = user.getUsername();
                getTime();
                reply();
            }
        });

    }

    /**
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     **/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent = new Intent();
//                intent.setClass(EditActivity.this,MainActivity.class);
//                startActivity(intent);
                NoticePutActivity.this.finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            image.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图像失败", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {

        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "你拒绝了相册使用权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    /*
    表表
     */

    private void reply() {
        if (path != null) {
            dialog = new ProgressDialog(NoticePutActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setTitle("上传图片中...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            String filepath = path;
            final BmobFile file = new BmobFile(new File(filepath));
            file.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException p1) {
                    if (p1 == null) {
                        notify = new Notify();
                        notify.setIcon(file);
                        String img_url = file.getUrl();
                        // showDialog();
                        String content =  mEd_contents.getText().toString();
                        notify.setImg_url(img_url);
                        notify.setPlatform(platform);
                        notify.setContent(content);
                        notify.setName(user.getNickname());
                        notify.setTime(time);
                        notify.setAuthor(user);
                        notify.save(new SaveListener<String>() {
                            @Override
                            public void done(String p1, BmobException p2) {
                                // TODO: Implement this method
                                if (p2 == null) {
                                    toast("发贴成功");
                                    //  al.dismiss();
                                    pushToAndroid(user.getUsername() + "发布了新状态，快去看看吧");
                                    NoticePutActivity.this.finish();
                                    NoticeActivity noticeActivity= new NoticeActivity();
                                    noticeActivity.finish();
                                } else {
                                    toast("发帖失败" + p2.toString());
                                    //al.dismiss();
                                }
                               /* if (p2 == null) {

                                    post.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                toast("发贴成功");
                                                al.dismiss();
                                                pushToAndroid(user.getUsername()+"发布了新状态，快去看看吧");
                                                EditActivity.this.finish();
                                            }else {
                                                toast("发帖失败"+e.toString());
                                                al.dismiss();
                                            }
                                        }
                                    });

                                    dialog.dismiss();
                                } else {
                                    toast(p2.toString() + "失败");
                                    dialog.dismiss();
                                }*/
                            }

                        });

                    } else {
                        toast(p1.toString() + "上传失败");
                    }

                    // TODO: Implement this method
                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                    dialog.setProgress(value);
                }

            });
        } else {
            //showDialog();
            String content =  mEd_contents.getText().toString();
            notify.setContent(content);
            notify.setName(user.getNickname());
            notify.setTime(time);
            notify.setAuthor(user);
            notify.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        toast("发贴成功");
                        al.dismiss();
                        pushToAndroid(user.getUsername() + "发布了新状态，快去看看吧");
                        NoticePutActivity.this.finish();
                        NoticeActivity noticeActivity= new NoticeActivity();
                        noticeActivity.finish();
                    } else {
                        toast("发帖失败" + e.toString());
                        al.dismiss();
                    }
                }
            });

        }
        // TODO Auto-generated method stub


    }


    private void pushToAndroid(String message) {
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("deviceType", "android");
        bmobPush.setQuery(query);
        bmobPush.pushMessage(message);
    }

    public void toast(String msg) {
        Toast.makeText(NoticePutActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 hh点 mm分");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        time = formatter.format(curDate);
    }
}