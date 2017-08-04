package shuhai.readercore.ui.fragment;

import shuhai.readercore.R;
import shuhai.readercore.base.BaseVPFragment;
import shuhai.readercore.ui.adapter.ViewPagerAdapter;
import shuhai.readercore.ui.presenter.BookShopPresenter;

/**
 * @author 55345364
 * @date 2017/8/4.
 */

public class BookShopFragment extends BaseVPFragment {

    BookShopPresenter presenter = new BookShopPresenter();


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_shop;
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
        pagerAdapter.setData(presenter.obtainView(mContext));
    }
}
