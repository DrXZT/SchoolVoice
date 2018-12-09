package com.example.drxzt.schoolvoice.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drxzt.schoolvoice.CommentActivity;
import com.example.drxzt.schoolvoice.Date.User;
import com.example.drxzt.schoolvoice.MainActivity;
import com.example.drxzt.schoolvoice.R;
import com.example.drxzt.schoolvoice.Date.Post;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by lmm on 2017/4/18.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> implements View.OnClickListener {
        private LayoutInflater myInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<Post> list;
    private String user_obj;
    private User user;
    private Context context;

    public MyAdapter(Context context, List<Post> list) {

        this.context = context;
        this.list = list;
        myInflater = LayoutInflater.from(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = myInflater.inflate(R.layout.item_main, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return vh;


    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final Post post = list.get(position);
        if (list.size() < 0) {
            return;
        }
        holder.dlhjm_liuyan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("name", post.getContent() + "," + post.getName() + "," + post.getObjectId() + "," + post.getPraise()
                        + "," + post.getImg_url()+","+post.getRead()+","+post.getTime()+","+post.getAuthor().getImg_url());
                context.startActivity(intent);
            }
        });
        holder.dlhjm_aixin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = BmobUser.getCurrentUser(User.class);
                post.increment("praise", 1);

                post.addUnique("praiseInfo", user.getObjectId());
                holder.dlhjm_aixin1.setBackgroundResource(R.drawable.aixin1);

                post.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            toast("点赞成功！");
                        } else {
                            toast("点赞失败！");
                        }
                    }
                });
                holder.main_praise.setText(""+ post.getPraise());
            }
        });


         holder.main_url_tx.setText(post.getAuthor().getImg_url());
         Glide.with(context).load(post.getAuthor().getImg_url()).placeholder(R.drawable.default_logo).thumbnail(0.3f).error(R.drawable.default_logo).dontAnimate().into(holder.com_touxiang);

        holder.content.setText(post.getContent());
        holder.time.setText(post.getTime());
        holder.tv_user.setText(post.getName());
        holder.tvRead.setText("阅览:" + post.getRead());
        holder.main_praise.setText(""+ post.getPraise());

        if (post.getImg_url() == null) {
            holder.post_image.setVisibility(View.GONE);

        } else {
            holder.tv_url.setText(post.getImg_url());
            Glide.with(context).load(post.getImg_url()).placeholder(R.mipmap.loading).thumbnail(0.3f).error(R.mipmap.loading).dontAnimate().into(holder.post_image);
        }

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(this);
        //获取POST USER OBJID
        if (mOnItemClickListener != null) {

            holder.rl_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = BmobUser.getCurrentUser(User.class);
                    if (user == null) {
                        Toast.makeText(context, "登陆后查看详情", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    mOnItemClickListener.onItemClick(holder.itemView, post.getContent() + "," + post.getName() + "," + post.getObjectId() + "," + post.getPraise()
                            + "," + post.getImg_url()+","+post.getRead()+","+post.getTime()+","+post.getAuthor().getImg_url());

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
        TextView tv_user, tv_url,main_url_tx;
        TextView content;
        TextView time;
        CardView rl_post;
        TextView tvRead, main_praise;
        ImageView post_image,com_touxiang;
        Button dlhjm_liuyan1, dlhjm_aixin1;

        ItemViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.dlhjm_neirong);
            time = (TextView) itemView.findViewById(R.id.dlhjm_time);
            tv_user = (TextView) itemView.findViewById(R.id.pot_mingzi);
            tvRead = (TextView) itemView.findViewById(R.id.main_read);
            rl_post = (CardView) itemView.findViewById(R.id.card_view);
            post_image = (ImageView) itemView.findViewById(R.id.main_image);
            tv_url = (TextView) itemView.findViewById(R.id.main_url);
            dlhjm_liuyan1 = (Button) itemView.findViewById(R.id.dlhjm_liuyan1);
            main_praise = (TextView) itemView.findViewById(R.id.main_praise);
            dlhjm_aixin1 = (Button) itemView.findViewById(R.id.dlhjm_aixin1);
            com_touxiang = (ImageView) itemView.findViewById(R.id.pot_touxiang);
            main_url_tx =(TextView)itemView.findViewById(R.id.main_url_tx);

        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}