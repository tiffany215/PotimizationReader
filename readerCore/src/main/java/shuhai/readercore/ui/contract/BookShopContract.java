package shuhai.readercore.ui.contract;


import android.content.Context;
import android.view.View;

import java.util.List;

import shuhai.readercore.base.BaseContract;

/**
 * @author 55345364
 * @date 2017/8/4.
 */

public interface BookShopContract {

    interface View extends BaseContract.BaseView{
        void showBookToc();
        void showChapterRead();
        void netError();
    }


    interface Presents<T> extends BaseContract.BasePresenter<T>{
        List<android.view.View> obtainView(Context context);
    }


}
