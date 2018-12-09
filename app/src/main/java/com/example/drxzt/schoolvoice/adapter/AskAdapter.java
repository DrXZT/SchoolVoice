package com.example.drxzt.schoolvoice.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drxzt.schoolvoice.R;
import com.example.drxzt.schoolvoice.Date.Comment;

import java.util.List;

public class AskAdapter extends BaseAdapter {
    private List<Comment> list;
    private Context context;

    public AskAdapter(List<Comment> list, Context context) {
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
        CommentViewHolder holder;

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.ask_item, null);
            holder=new CommentViewHolder();
            holder.tvName=(TextView) convertView.findViewById(R.id.ask_textView4);
            holder.tvContent=(TextView) convertView.findViewById(R.id.ask_txt);
            holder.tvTime=(TextView) convertView.findViewById(R.id.ask_time);
            holder.tvimg=(ImageView)convertView.findViewById(R.id.ask_tx3) ;
            holder.tx_url = (TextView) convertView.findViewById(R.id.ask_url);
            convertView.setTag(holder);
        }else{
            holder=(CommentViewHolder)convertView.getTag();
        }
        final Comment comment=list.get(position);

        holder.tvName.setText(comment.getUser().getNickname());
        holder.tvContent.setText(comment.getContent());
        holder.tvTime.setText(comment.getTime());

        holder.tx_url.setText(comment.getUser().getImg_url());
        Glide.with(context).load(comment.getUser().getImg_url()).placeholder(R.drawable.default_logo).thumbnail(0.3f).error(R.drawable.default_logo).dontAnimate().into(holder.tvimg);

        return convertView;
    }

    class CommentViewHolder{
        TextView tvName,tx_url;
        TextView tvContent;
        TextView tvTime;
        ImageView tvimg;


    }

}
