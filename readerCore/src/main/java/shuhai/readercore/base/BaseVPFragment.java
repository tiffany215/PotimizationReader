package shuhai.readercore.base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;

import java.lang.reflect.Constructor;

import butterknife.Bind;
import shuhai.readercore.R;
import shuhai.readercore.ui.adapter.ViewPagerAdapter;

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

    private Object createAdapter(Class<?> clz){
        Object object;
        try {
            Constructor constructor = clz.getDeclaredConstructor(Context.class);
            constructor.setAccessible(true);
            object = constructor.newInstance(mContext);
        } catch (Exception e) {
            e.printStackTrace();
            object = null;
        }
        return object;
    }


    @Override
    public void onRefresh() {
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void attachView() {

    }
}
