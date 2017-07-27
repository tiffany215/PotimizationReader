package shuhai.readercore.ui.contract;

import shuhai.readercore.base.BaseContract;

/**
 * @author 55345364
 * @date 2017/7/27.
 */

public interface BookStoreContract {


    interface View extends BaseContract.BaseView{

       void showShelfBookList();

    }



    interface Presenter<T> extends BaseContract.BasePresenter<T>{

        void getShelfBookList();

    }


}
