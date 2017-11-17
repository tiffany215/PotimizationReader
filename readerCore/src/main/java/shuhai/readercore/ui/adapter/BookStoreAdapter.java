package shuhai.readercore.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import shuhai.readercore.bean.BookEntity;
import shuhai.readercore.ui.holder.BookStoreHolder;

/**
 * @author 55345364
 * @date 2017/8/3.
 */

public class BookStoreAdapter extends RecyclerArrayAdapter<BookEntity>{


    public BookStoreAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookStoreHolder(parent);
    }
}
