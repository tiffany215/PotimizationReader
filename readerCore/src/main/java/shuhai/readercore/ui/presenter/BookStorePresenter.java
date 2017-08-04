package shuhai.readercore.ui.presenter;

import java.util.List;

import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.dao.BookInfoEntity;
import shuhai.readercore.manager.DataBaseManager;
import shuhai.readercore.ui.contract.BookStoreContract;

/**
 * @author 55345364
 * @date 2017/7/27.
 */

public class BookStorePresenter extends RxPresenter<BookStoreContract.View> implements BookStoreContract.Presenter<BookStoreContract.View>{


    @Override
    public List<BookInfoEntity> getShelfBookList() {
        return DataBaseManager.getInstance().queryBookInfoList();
    }
}
