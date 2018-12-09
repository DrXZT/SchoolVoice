package com.example.drxzt.schoolvoice.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import  com.example.drxzt.schoolvoice.R;
import  com.example.drxzt.schoolvoice.Date.Post;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class PersonAdapter extends BaseAdapter {
    private List<Post> list;
    private Context context;

    public PersonAdapter(List<Post> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_person, null);
            new PersonAdapter.CommentViewHolder(convertView);
        }
        PersonAdapter.CommentViewHolder holder = (PersonAdapter.CommentViewHolder) convertView.getTag();

        final Post post = list.get(position);
        Glide.with(context).load(post.getImg_url()).placeholder(R.mipmap.loading).thumbnail(0.3f).error(R.mipmap.loading).dontAnimate().into(holder.per_tupian);
        holder.name.setText(post.getTitle());
        holder.content.setText(post.getContent());
        holder.time.setText(post.getTime());
        return convertView;

    }

    class CommentViewHolder {
        TextView name, content, time;
        ImageView per_tupian;

        public CommentViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.per_mingzi);
            content = (TextView) view.findViewById(R.id.per_write);
            time = (TextView) view.findViewById(R.id.per_time);
            per_tupian = (ImageView)view.findViewById(R.id.per_tupian);
            view.setTag(this);
        }
    }
}



