package shuhai.readercore.ui.fragment;

import shuhai.readercore.R;
import shuhai.readercore.base.BaseRVFragment;
import shuhai.readercore.bean.base.BookInfo;
import shuhai.readercore.ui.adapter.RecycleViewAdapter;
import shuhai.readercore.ui.contract.BookStoreContract;
import shuhai.readercore.ui.presenter.BookStorePresenter;

public class BookStoreFragment extends BaseRVFragment<BookStorePresenter,BookInfo> implements BookStoreContract.View{

    @Override
    protected void setupActivityComponent() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_store;
    }

    @Override
    public void initData() {

    }

    @Override
    public void configView() {
        initAdapter(RecycleViewAdapter.class,true,false);

    }

    @Override
    public void showShelfBookList() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }
}
