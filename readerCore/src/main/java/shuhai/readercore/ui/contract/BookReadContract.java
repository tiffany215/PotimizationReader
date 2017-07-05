package shuhai.readercore.ui.contract;

import shuhai.readercore.base.BaseContract;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public interface BookReadContract {

    interface View extends BaseContract.BaseView{
        void showChapterRead();
        void netError();
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getChapterRead(String url, int chapter);
    }

}
