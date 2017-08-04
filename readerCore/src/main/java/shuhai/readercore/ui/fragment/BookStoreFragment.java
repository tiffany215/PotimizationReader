package shuhai.readercore.ui.fragment;
import android.content.Intent;

import shuhai.readercore.R;
import shuhai.readercore.base.BaseRVFragment;
import shuhai.readercore.dao.BookInfoEntity;
import shuhai.readercore.ui.activity.ReadActivity;
import shuhai.readercore.ui.adapter.BookStoreAdapter;
import shuhai.readercore.ui.contract.BookStoreContract;
import shuhai.readercore.ui.presenter.BookStorePresenter;

public class BookStoreFragment extends BaseRVFragment<BookStorePresenter,BookInfoEntity> implements BookStoreContract.View{


    private BookStorePresenter presenter = new BookStorePresenter();


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
        initAdapter(BookStoreAdapter.class,false,false);
        onRefresh();

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

    @Override
    public void onItemClick(int position) {

        startActivity(new Intent(getActivity(), ReadActivity.class));


    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        mAdapter.clear();
        mAdapter.addAll(presenter.getShelfBookList());
        mAdapter.notifyDataSetChanged();
        recyclerView.setRefreshing(false);

    }
}
