package shuhai.readercore.ui.contract;

import shuhai.readercore.base.BaseContract;

/**
 * @author 55345364
 * @date 2017/7/27.
 */

public class MainContract {


    interface View extends BaseContract.BaseView{

        void loginSuccess();

        void syncBookShelfCompleted();
    }


    interface Presenter<T> extends BaseContract.BasePresenter<T>{

        void login();

        void syncBookShelf();

    }



}
