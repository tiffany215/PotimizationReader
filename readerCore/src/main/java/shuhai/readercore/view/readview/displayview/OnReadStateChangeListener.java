package shuhai.readercore.view.readview.displayview;

import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author 55345364
 * @date 2017/9/22.
 */

public interface OnReadStateChangeListener {


    /**
     * 章节变化监听
     * @param chapterId 章节ID
     * @param chapterOrder 章节Order
     * @param status 翻页标示
     */
    void onChapterChanged(int chapterId, int chapterOrder, FlipStatus status);

    /**
     * 页码变化监听
     * @param chapterId  章节ID
     * @param chapterOrder 章节Order
     */
    void onPageChanged(int chapterId,int chapterOrder, FlipStatus status);


    /**
     * 开始加载章节
     */
    void onStartLoadChapter();

    /**
     *
     */
    void onSuccessLoadChapter();



}
