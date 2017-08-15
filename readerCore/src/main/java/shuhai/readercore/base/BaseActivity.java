package shuhai.readercore.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import shuhai.readercore.R;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public abstract class BaseActivity extends AppCompatActivity{

    public Context mContext;

    public Toolbar mCommonToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        this.mContext = this;
        ButterKnife.inject(this);

        mCommonToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if(null != mCommonToolbar){
            initToolBar();
            setSupportActionBar(mCommonToolbar);
        }
        initData();
        configViews();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public abstract int getLayoutId();

    public abstract void initData();

    public abstract void initToolBar();

    public abstract void configViews();


    /**
     * 隐藏控件
     * @param views
     */
    protected void gone(final View... views){
        if(null != views && views.length > 0){

            for (View view : views){
                if(null != view){
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 显示控件
     * @param views
     */
    protected void visible(final View... views){
        if(null != views && views.length > 0){

            for (View view: views) {
                if(null != view){
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 判断传入控件的显示状态
     * @param view
     * @return
     */
    protected boolean isVisible(View view){
        return view.getVisibility() == View.VISIBLE;
    }



}
