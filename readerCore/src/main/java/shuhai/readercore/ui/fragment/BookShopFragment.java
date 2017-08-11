package shuhai.readercore.ui.fragment;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseVPFragment;
import shuhai.readercore.ui.adapter.ViewPagerAdapter;
import shuhai.readercore.ui.presenter.BookShopPresenter;

/**
 * @author 55345364
 * @date 2017/8/4.
 */

public class BookShopFragment extends BaseVPFragment {

    @InjectView(R.id.navigation)
    public BottomNavigationView bottomNavigationView;
    BookShopPresenter presenter = new BookShopPresenter();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_shop;
    }

    @Override
    public void initData() {
        initAdapter(ViewPagerAdapter.class);
    }

    @Override
    public void configView() {
        onRefresh();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        if(null != pagerAdapter){
            pagerAdapter.setData(presenter.obtainView(mContext));
        }
    }

    @Override
    public void onPageSelectedPage(int position) {
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setCurrentItem(0);
                    return true;
                case R.id.navigation_serial:
                    setCurrentItem(1);
                    return true;
                case R.id.navigation_fans:
                    setCurrentItem(2);
                    return true;
                case R.id.navigation_person:
                    setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };


}
