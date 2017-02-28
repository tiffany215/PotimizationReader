package com.shuhai.bookos.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuhai.bookos.R;
import com.shuhai.bookos.bean.NewsInfo;

import java.util.List;

/**
 * Created by 55345364 on 2017/2/28.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.NormalViewHolder> {


    private LayoutInflater layoutInflater;
    private List<NewsInfo.News> datas;
    private Context mContext;

    public RecycleViewAdapter(Context context, List<NewsInfo.News> list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.datas = list;
        this.mContext = context;
    }


    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(layoutInflater.inflate(R.layout.news_item_content, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalViewHolder holder, int position) {

        Glide.with(mContext).load(datas.get(position).thumbnail_pic_s).into(holder.imageView);
        holder.textView.setText(datas.get(position).title);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    class NormalViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView textView;

        public NormalViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.news_cv_content);
            imageView = (ImageView) itemView.findViewById(R.id.news_iv_photo);
            textView = (TextView) itemView.findViewById(R.id.news_tv_title);
        }
    }

}
