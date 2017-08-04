package shuhai.readercore.ui.fragment;

import shuhai.readercore.R;
import shuhai.readercore.base.BaseVPFragment;
import shuhai.readercore.ui.adapter.ViewPagerAdapter;

/**
 * @author 55345364
 * @date 2017/8/4.
 */

public class BookShopFragment extends BaseVPFragment {


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_store;
    }

    @Override
    public void initData() {

    }

    @Override
    public void configView() {
        initAdapter(ViewPagerAdapter.class);
        onRefresh();
    }


    @Override
    public void onRefresh() {
        super.onRefresh();

    }
}
