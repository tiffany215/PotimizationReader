package shuhai.readercore.ui.presenter;
import javax.inject.Inject;
import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.ui.contract.BookReadContract;
import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author wangxu
 * @date 2017/7/5.
 */

public class BookReadPresenter extends RxPresenter<BookReadContract.View> implements BookReadContract.Presenter<BookReadContract.View>{

    @Inject
    public BookReadPresenter(){

    }


    @Override
    public void getChapterRead(final int articleId, final int chapterId, int chapterOrder, final FlipStatus status) {

    }

    @Override
    public void getBookMixAToc() {
        mView.showBookToc();
    }

}
