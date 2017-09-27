package shuhai.readercore.view.readview.displayview;

/**
 * @author 55345364
 * @date 2017/9/22.
 */

public interface OnReadStateChangeListener {


    /**
     * 章节变化监听
     * @param chapterId 章节ID
     * @param chapterOrder 章节Order
     * @param flipMark 翻页标示
     */
    void onChapterChanged(int chapterId,int chapterOrder,int flipMark);

    /**
     * 页码变化监听
     * @param chapterId  章节ID
     * @param chapterOrder 章节Order
     * @param page 页码
     */
    void onPageChanged(int chapterId,int chapterOrder,int page);

}
