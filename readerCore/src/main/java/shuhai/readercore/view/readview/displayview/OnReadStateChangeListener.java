package shuhai.readercore.view.readview.displayview;

/**
 * @author 55345364
 * @date 2017/9/22.
 */

public interface OnReadStateChangeListener {


    void onChapterChanged(int chapterId,int chapterOrder,int flipMark);

    void onPageChanged(int chapterId,int page);

}
