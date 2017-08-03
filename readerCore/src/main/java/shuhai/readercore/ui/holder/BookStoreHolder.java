package shuhai.readercore.ui.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import shuhai.readercore.R;
import shuhai.readercore.bean.BookInfoEntity;

/**
 * @author 55345364
 * @date 2017/8/3.
 */

public class BookStoreHolder extends BaseViewHolder<BookInfoEntity.MessageBean> {

    private ImageView bookCover;
    private TextView bookName;
    private TextView readProgress;


    public BookStoreHolder(ViewGroup parent) {
        super(parent, R.layout.item_book_store_layout);
        bookCover = $(R.id.book_cover);
        bookName = $(R.id.book_name);
        readProgress = $(R.id.book_progress);
    }


    @Override
    public void setData(BookInfoEntity.MessageBean data) {
        super.setData(data);
        Glide.with(getContext()).load("").into(bookCover);
        bookName.setText(data.getArticlename());
    }
}
