package com.example.drxzt.schoolvoice.adapter;


import java.util.List;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.drxzt.schoolvoice.R;
import com.example.drxzt.schoolvoice.Date.Message;


public class MesAdapter extends BaseAdapter {
    private List<Message> list;
    private Context context;

    public MesAdapter(List<Message> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mes, null);
            new CommentViewHolder(convertView);
        }
        CommentViewHolder holder = (CommentViewHolder) convertView.getTag();

        final Message message = list.get(position);

        holder.name.setText(message.getName());
        holder.content.setText(message.getContent());
        holder.time.setText(message.getTime());
        return convertView;

    }

    class CommentViewHolder {
        TextView name, content, time;

        public CommentViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.per_mingzi);
            content = (TextView) view.findViewById(R.id.com_neirong);
            time = (TextView) view.findViewById(R.id.com_time);
            view.setTag(this);
        }
    }
}
