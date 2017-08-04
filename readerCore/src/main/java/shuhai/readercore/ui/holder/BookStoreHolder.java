package shuhai.readercore.ui.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import shuhai.readercore.R;
import shuhai.readercore.dao.BookInfoEntity;

/**
 * @author 55345364
 * @date 2017/8/3.
 */

public class BookStoreHolder extends BaseViewHolder<BookInfoEntity> {

    private ImageView bookCover;
    private TextView bookName;
    private TextView readProgress;


    public BookStoreHolder(ViewGroup parent) {
        super(parent, R.layout.item_book_store_list);
        bookCover = $(R.id.book_cover);
        bookName = $(R.id.book_name);
        readProgress = $(R.id.book_progress);
    }


    @Override
    public void setData(BookInfoEntity data) {
        super.setData(data);
        Glide.with(getContext()).load(data.getBkbmurl()).into(bookCover);

//        bookCover.setBackgroundResource(R.drawable.icon01);

        bookName.setText(data.getArticlename());
        readProgress.setText("50%");
    }
}
