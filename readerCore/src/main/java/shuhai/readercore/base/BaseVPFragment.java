package shuhai.readercore.base;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.jude.easyrecyclerview.swipe.SwipeRefreshLayout;

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
    public ViewPager viewPager;
    public ViewPagerAdapter pagerAdapter;

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
//        viewPager.setOnPageChangeListener(mOnPageChangeListener);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void attachView() {

    }


    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setSelectedItemId(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public void setCurrentItem(int position){
        viewPager.setCurrentItem(position);
    }


    public abstract void setSelectedItemId(int position);

}
