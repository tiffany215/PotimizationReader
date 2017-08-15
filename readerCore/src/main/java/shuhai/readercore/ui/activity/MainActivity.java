package shuhai.readercore.ui.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

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
        mCommonToolbar.setLogo(R.drawable.ic_favorite_black_24dp);
        mCommonToolbar.setTitle(R.string.app_name);
        mCommonToolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void configViews() {



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

        }


        return super.onOptionsItemSelected(item);
    }
}




















