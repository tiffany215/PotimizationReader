package shuhai.readercore.ui.presenter;

import android.content.Context;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shuhai.readercore.api.BookApi;
import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.bean.ChapterRead;
import shuhai.readercore.ui.contract.BookReadContract;

/**
 * @author 55345364
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
        Subscription rxSubscription = bookApi.getChapterRead(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChapterRead>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.netError();
                    }

                    @Override
                    public void onNext(ChapterRead chapterRead) {
                        mView.showChapterRead();
                    }
                });
        addSubscribe(rxSubscription);
    }
}
