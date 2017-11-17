package shuhai.readercore.view.readview.displayview;


import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public interface BaseReadViewImpl {

    //设置主题
    void init(int theme);

    void openBook(int articleId,int chapterId,int chapterOrder,FlipStatus status);

    void closeBook();

}
