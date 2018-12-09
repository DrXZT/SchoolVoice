package com.example.drxzt.schoolvoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drxzt.schoolvoice.Date.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * MainFragment 主Fragment
 * @author codingblock 2015/09/14
 *
 */
public class OldPWFragment extends Fragment {
private BmobUser user;
private Button btn_show_other;
private EditText old_pw,new_pw;
private String old_password,new_password;
    public OldPWFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_old_pw, container, false);



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = new BmobUser();
        btn_show_other=(Button) getView().findViewById(R.id.btn_show_other);
        old_pw=(EditText)getView().findViewById(R.id.old_pw);
        new_pw=(EditText)getView().findViewById(R.id.new_pw);

        btn_show_other.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                old_password=old_pw.getText().toString();
                new_password=new_pw.getText().toString();
                BmobUser.updateCurrentUserPassword(old_password,  new_password, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            toast("密码修改成功，可以用新密码进行登录啦");
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), SafeActivity.class);
                            startActivity(intent);

                        }else{
                            toast("失败:" + e.getMessage());
                        }
                    }

                });


            }
        });

    }
    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}