package com.example.drxzt.schoolvoice.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drxzt.schoolvoice.Date.Post;
import com.example.drxzt.schoolvoice.Date.User;
import com.example.drxzt.schoolvoice.R;
import com.example.drxzt.schoolvoice.Date.Notify;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by lmm on 2017/4/18.
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ItemViewHolder> implements View.OnClickListener {
    private LayoutInflater myInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<Notify> list;
    private String user_obj;
    private User user;
    private Context context;

    private String platform;

    public NoticeAdapter(Context context, List<Notify> list) {

        this.context = context;
        this.list = list;
        myInflater = LayoutInflater.from(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = myInflater.inflate(R.layout.item_notice, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return vh;


    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final Notify notify = list.get(position);
       // if (notify.getPlatform().equals(platform)){
            if (list.size() < 0) {
                return;
            }
        Glide.with(context).load(notify.getAuthor().getImg_url()).placeholder(R.drawable.default_logo).thumbnail(0.3f).error(R.drawable.default_logo).dontAnimate().into(holder.not_touxiang);
        holder.content.setText(notify.getContent());
        holder.time.setText(notify.getTime());
        holder.not_user.setText(notify.getName());
        holder.notRead.setText("阅读："+notify.getRead());
        if (notify.getImg_url() == null) {
            holder.not_image.setVisibility(View.GONE);

        } else {
            holder.not_url.setText(notify.getImg_url());
            Glide.with(context).load(notify.getImg_url()).placeholder(R.mipmap.loading).thumbnail(0.3f).error(R.mipmap.loading).dontAnimate().into(holder.not_image);
        }

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(this);
        //获取POST USER OBJID
        if (mOnItemClickListener != null) {

            holder.not_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = BmobUser.getCurrentUser(User.class);
                    if (user == null) {
                        Toast.makeText(context, "登陆后查看详情", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    mOnItemClickListener.onItemClick(holder.itemView, notify.getContent() + "," + notify.getName() + "," + notify.getObjectId() + "," + notify.getRead()
                            + "," + notify.getImg_url()+","+notify.getTime());

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView not_user, not_url;
        TextView content;
        TextView time;
        CardView not_post;
        TextView notRead;
        ImageView not_image,not_touxiang;

        ItemViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.not_neirong);
            time = (TextView) itemView.findViewById(R.id.not_time);
            not_user = (TextView) itemView.findViewById(R.id.not_name);
            notRead=(TextView)itemView.findViewById(R.id.not_read);
            not_post = (CardView) itemView.findViewById(R.id.not_card_view);
            not_image = (ImageView) itemView.findViewById(R.id.not_img);
            not_url = (TextView) itemView.findViewById(R.id.not_url);
            not_touxiang = (ImageView) itemView.findViewById(R.id.not_touxiang);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void getPlatform(String platform) {
        this.platform = platform;
    }
}
