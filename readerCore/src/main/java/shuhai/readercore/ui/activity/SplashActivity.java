package shuhai.readercore.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import shuhai.readercore.R;
import shuhai.readercore.ui.adapter.ViewPagerAdapter;
import shuhai.readercore.ui.presenter.BookRecommendPresenter;

/**
 * @author 55345364
 * @date 2017/7/26.
 */

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    private List<View> viewList = new ArrayList<>();

    private LayoutInflater mLayoutInflater;

    private Context mContext;

    private ViewPagerAdapter adapter;

    @InjectView(R.id.splash_viewpager)
    ViewPager viewPager;

    Button recommendBoy;
    Button recommendGrid;

    private BookRecommendPresenter recommendPresenter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        this.mContext = this;
        mLayoutInflater = LayoutInflater.from(mContext);
        initView();
        initData();
    }


    private void initView(){


        recommendPresenter = new BookRecommendPresenter();

        ImageView imageView01 = new ImageView(this);
        imageView01.setBackgroundResource(R.drawable.splash_01);

        ImageView imageView02 = new ImageView(this);
        imageView02.setBackgroundResource(R.drawable.splash_02);

        ImageView imageView03 = new ImageView(this);
        imageView03.setBackgroundResource(R.drawable.splash_03);

        View view = mLayoutInflater.inflate(R.layout.activity_landing_page,null);
        recommendBoy = (Button) view.findViewById(R.id.recommend_boy);
        recommendBoy.setOnClickListener(this);
        recommendGrid = (Button) view.findViewById(R.id.recommend_grid);
        recommendGrid.setOnClickListener(this);

        viewList.add(imageView01);
        viewList.add(imageView02);
        viewList.add(imageView03);
        viewList.add(view);

    }

    private void initData(){
        adapter = new ViewPagerAdapter();
        adapter.setData(viewList);
        viewPager.setAdapter(adapter);
    }


    private void GoMain() {
        recommendPresenter.getRecommendBook(2,5);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recommend_boy:
                GoMain();
                break;

            case R.id.recommend_grid:
                GoMain();
                break;
        }
    }
}
