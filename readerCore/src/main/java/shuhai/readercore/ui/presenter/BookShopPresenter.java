package shuhai.readercore.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import shuhai.readercore.R;
import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.ui.contract.BookShopContract;

/**
 * @author 55345364
 * @date 2017/8/4.
 */

public class BookShopPresenter extends RxPresenter<View> implements BookShopContract.Presents<View> {


    @Override
    public void attachView(View view) {

    }

    @Override
    public void detachView() {

    }


    @Override
    public List<View> obtainView(Context context) {
        List<View> list = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view1 = inflater.inflate(R.layout.item_book_store_layout,null);
        View view2 = inflater.inflate(R.layout.item_book_store_layout,null);
        View view3 = inflater.inflate(R.layout.item_book_store_layout,null);
        View view4 = inflater.inflate(R.layout.item_book_store_layout,null);

        list.add(view1);
        list.add(view2);
        list.add(view3);
        list.add(view4);
        return list;
    }
}
