package shuhai.readercore.ui.presenter;

import android.content.Context;

import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.net.api.CommonApi;
import shuhai.readercore.ui.contract.BookReadContract;

/**
 * @author wangxu
 * @date 2017/7/5.
 */

public class BookReadPresenter extends RxPresenter<BookReadContract.View> implements BookReadContract.Presenter<BookReadContract.View>{

    private Context mContext;
    private CommonApi bookApi;


    public BookReadPresenter(Context context,CommonApi api){
        this.mContext = context;
        this.bookApi = api;
    }


    @Override
    public void getChapterRead(String url, int chapter) {

    }
}
