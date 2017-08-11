package shuhai.readercore.ui.activity;

import android.support.v4.view.ViewPager;

import butterknife.InjectView;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.ui.adapter.ViewPagerFragmentAdapter;
import shuhai.readercore.ui.fragment.BookShopFragment;
import shuhai.readercore.ui.fragment.BookStoreFragment;

public class MainActivity extends BaseActivity {


    @InjectView(R.id.viewpager_book_store)
    ViewPager viewPager;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new BookStoreFragment());
        adapter.addFragment(new BookShopFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void configViews() {



    }

}




















