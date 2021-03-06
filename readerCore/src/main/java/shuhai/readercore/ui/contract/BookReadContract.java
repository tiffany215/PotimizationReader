package shuhai.readercore.ui.contract;

import shuhai.readercore.base.BaseContract;
import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public interface BookReadContract {

    interface View extends BaseContract.BaseView{
        void showBookToc();
        void showChapterRead(int chapterId, FlipStatus status);
        void netError();
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        /**
         * 获取章节内容
         * @param articleId 书籍id
         * @param chapterId 章节id
         * @param chapterOrder 章节order
         * @param status 翻页标识 当前章节:0   前一页:1    后一页:2
         */
        void getChapterRead(int articleId, int chapterId,int chapterOrder,FlipStatus status);
        void getBookMixAToc();
    }

}
