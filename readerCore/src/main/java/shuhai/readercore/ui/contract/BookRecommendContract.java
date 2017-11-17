package shuhai.readercore.ui.contract;

import shuhai.readercore.base.BaseContract;

/**
 * @author 55345364
 * @date 2017/8/3.
 */

public interface BookRecommendContract {



    interface View extends BaseContract.BaseView{
        void postView();
    }


    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getRecommendBook(int sex,int pageSize);
    }


}
