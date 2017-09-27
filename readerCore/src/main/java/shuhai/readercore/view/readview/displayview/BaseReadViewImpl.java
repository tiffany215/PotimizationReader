package shuhai.readercore.view.readview.displayview;

import shuhai.readercore.view.readview.FlipStatus;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public interface BaseReadViewImpl {
    //开始翻页动画
    void startAnimation();
    //终止翻页动画
    void abortAnimation();
    //恢复翻页动画
    void restoreAnimation();
    //设置主题
    void init(int theme);

    void openBook(int articleId,int chapterId,int chapterOrder,FlipStatus status);


}
