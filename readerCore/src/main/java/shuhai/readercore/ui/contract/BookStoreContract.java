package shuhai.readercore.ui.contract;

import java.util.List;

import shuhai.readercore.base.BaseContract;
import shuhai.readercore.bean.BookBean;
import shuhai.readercore.bean.BookEntity;
//import shuhai.readercore.dao.BookInfoEntity;

/**
 * @author 55345364
 * @date 2017/7/27.
 */

public interface BookStoreContract {


    interface View extends BaseContract.BaseView{

       void showShelfBookList();

    }



    interface Presenter<T> extends BaseContract.BasePresenter<T>{

        List<BookEntity> getShelfBookList();

    }


}
