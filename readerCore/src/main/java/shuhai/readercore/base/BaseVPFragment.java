package shuhai.readercore.base;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import shuhai.readercore.R;
import shuhai.readercore.ui.adapter.ViewPagerAdapter;
import shuhai.readercore.ui.adapter.ViewPagerFragmentAdapter;
import shuhai.readercore.ui.presenter.BookRecommendPresenter;

/**
 * @author 55345364
 * @date 2017/8/4.
 */

public abstract  class BaseVPFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{


    @Bind(R.id.view_pager)
    protected ViewPager viewPager;
    protected ViewPagerAdapter pagerAdapter;




    @Override
    protected void setupActivityComponent() {

    }

    protected void initAdapter(Class<? extends PagerAdapter> clz){
        pagerAdapter = (ViewPagerAdapter)createAdapter(clz);
        if(null != viewPager){
            viewPager.setAdapter(pagerAdapter);
        }
    }

    private Object createAdapter(Class clz){
        Object object;
        try {
            Constructor constructor = clz.getDeclaredConstructor();
            constructor.setAccessible(true);
            object = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            object = null;
        }
        return object;
    }


    @Override
    public void onRefresh() {


    }

    @Override
    public void attachView() {

    }
}
