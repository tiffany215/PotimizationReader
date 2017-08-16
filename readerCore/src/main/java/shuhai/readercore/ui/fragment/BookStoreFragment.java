package shuhai.readercore.ui.fragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;

import butterknife.InjectView;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseRVFragment;
import shuhai.readercore.dao.BookInfoEntity;
import shuhai.readercore.ui.activity.AppSettingsActivity;
import shuhai.readercore.ui.activity.ReadActivity;
import shuhai.readercore.ui.adapter.BookStoreAdapter;
import shuhai.readercore.ui.contract.BookStoreContract;
import shuhai.readercore.ui.presenter.BookStorePresenter;
import shuhai.readercore.utils.ToastUtils;

public class BookStoreFragment extends BaseRVFragment<BookStorePresenter,BookInfoEntity> implements BookStoreContract.View{


    private BookStorePresenter presenter = new BookStorePresenter();

    @InjectView(R.id.fab)
    public FloatingActionButton mFloatingActionButton;

    @InjectView(R.id.design_bottom_sheet)
    public BottomSheetLayout mBottomSheetLayout;

    private View sheetView;

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
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSheetView();
            }
        });
        if(null == sheetView){
            sheetView = LayoutInflater.from(mContext).inflate(R.layout.common_bottom_sheet,mBottomSheetLayout,false);
            sheetView.findViewById(R.id.bookstore_menu_night_model).setOnClickListener(onClickListener);
            sheetView.findViewById(R.id.bookstore_menu_local_reading).setOnClickListener(onClickListener);
            sheetView.findViewById(R.id.bookstore_menu_cloud_bookshelf).setOnClickListener(onClickListener);
            sheetView.findViewById(R.id.bookstore_menu_about_ours).setOnClickListener(onClickListener);
        }
        mBottomSheetLayout.setPeekOnDismiss(true);


    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bookstore_menu_night_model:
                    ToastUtils.showSingleToast("1");
                    break;
                case R.id.bookstore_menu_local_reading:
                    ToastUtils.showSingleToast("2");
                    break;
                case R.id.bookstore_menu_cloud_bookshelf:
                    ToastUtils.showSingleToast("4");
                    break;
                case R.id.bookstore_menu_about_ours:
                    startActivity(new Intent().setClass(mContext,AppSettingsActivity.class));
                    break;
            }
            mBottomSheetLayout.dismissSheet();
        }
    };

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


    private void showSheetView(){
        if(mBottomSheetLayout.isSheetShowing()){
            mBottomSheetLayout.dismissSheet();
        }else{
            mBottomSheetLayout.showWithSheetView(sheetView);
        }

    }
}
