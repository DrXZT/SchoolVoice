package com.example.drxzt.schoolvoice.adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drxzt.schoolvoice.CommentActivity;
import com.example.drxzt.schoolvoice.Date.Note;
import  com.example.drxzt.schoolvoice.R;


import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/5/26.
 */

public class NoteAdapter extends BaseAdapter {
    private List<Note> list;
    private Context context;

    public NoteAdapter(List<Note> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_note, null);
            new NoteAdapter.CommentViewHolder(convertView);
        }
        final NoteAdapter.CommentViewHolder holder = (NoteAdapter.CommentViewHolder) convertView.getTag();

        final Note note = list.get(position);
        if (note.getComplete()){
            holder.mwcbutton.getBackground().setAlpha(255);
        }else{
            holder.mwcbutton.getBackground().setAlpha(0);
        }

        holder. mwcbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               holder.mwcbutton.setText("SUCCESS");
               holder.mwcbutton.getBackground().setAlpha(0);
               note.setComplete(true);
                note.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            toast("达成目标了！！");
                        } else {
                            toast("请重新点击:" + e.getMessage());
                        }
                    }
                });
            }
        });
        holder.content.setText(note.getContent());
        holder.time.setText(note.getTime());
        holder.remindtime.setText(note.getRemindtime());

        return convertView;

    }

    class CommentViewHolder {
        TextView content, time,remindtime;
        Button mwcbutton;

        public CommentViewHolder(View view) {
            remindtime=(TextView) view.findViewById(R.id.deadriqi) ;
            content = (TextView) view.findViewById(R.id.mainnr);
            time = (TextView) view.findViewById(R.id.outriqi);
            mwcbutton =(Button) view.findViewById(R.id.mwcbutton);
            view.setTag(this);
        }
    }
    public void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
