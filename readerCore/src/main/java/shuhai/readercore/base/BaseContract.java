package shuhai.readercore.base;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public interface BaseContract {

    interface BasePresenter<T>{
        void attachView(T view);
        void detachView();
    }


    interface BaseView{
        void showError();
        void complete();
    }

}
