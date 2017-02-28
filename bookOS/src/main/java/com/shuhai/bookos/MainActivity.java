package com.shuhai.bookos;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuhai.bookos.adapter.RecycleViewAdapter;
import com.shuhai.bookos.bean.NewsInfo;
import com.shuhai.bookos.common.ApiClient;
import com.shuhai.bookos.network.GenericsCallback;
import com.shuhai.bookos.network.JsonGenericsSerializable;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    private TextView content;
    private ImageView iv_main_photo;
    private Context mContext;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        mRecyclerView = (RecyclerView) findViewById(R.id.news_rv_contents);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        generateData();
    }


    private void generateData() {

        ApiClient.getNewsInfo(new GenericsCallback<NewsInfo>(new JsonGenericsSerializable()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(NewsInfo response, int id) {
                mRecyclerView.setAdapter(new RecycleViewAdapter(mContext, response.result.data));
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;

            case R.id.action_list:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void doPost(View view) {
        ApiClient.getNewsInfo(new GenericsCallback<NewsInfo>(new JsonGenericsSerializable()) {

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(NewsInfo response, int id) {
                content.setText(response.result.data.get(0).thumbnail_pic_s);
                Glide.with(mContext).load(response.result.data.get(0).thumbnail_pic_s).crossFade().into(iv_main_photo);
            }
        });
    }
}
