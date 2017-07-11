package shuhai.readercore.ui.presenter;

import android.content.Context;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.bean.ChapterRead;
import shuhai.readercore.net.api.BookApi;
import shuhai.readercore.ui.contract.BookReadContract;

/**
 * @author wangxu
 * @date 2017/7/5.
 */

public class BookReadPresenter extends RxPresenter<BookReadContract.View> implements BookReadContract.Presenter<BookReadContract.View>{

    private Context mContext;
    private BookApi bookApi;


    public BookReadPresenter(Context context,BookApi api){
        this.mContext = context;
        this.bookApi = api;
    }


    @Override
    public void getChapterRead(String url, int chapter) {

    }
}
