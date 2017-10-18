package shuhai.readercore.view.readview.factory;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.dataloader.ChapterLoaderImpl;
import shuhai.readercore.view.readview.FlipStatus;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public abstract class Factory {


    /**
     * 构造数据加载模式的构造方法
     * @param clz
     * @param <T>
     * @return
     */
    public abstract <T extends ChapterLoaderImpl> T createChapterLoader(Class<T> clz);

    /**
     * 获取上一章内容
     */
    public abstract void preChapter();

    /**
     * 获取当前章节内容
     */
    public abstract void curChapter();

    /**
     * 获取下一章节内容
     */
    public abstract void nextChapter();


    /**
     * 绘制章节内容到指定的Canvas中
     * @param canvas
     */
    public abstract void onDraw(Canvas canvas);

    /**
     * 设置阅读页背景
     * @param bitmap
     */
    public abstract void setBgBitmap(Bitmap bitmap);

    /**
     *  打开书籍加载缓存或网络中指定章节内容
     * @param articleID 书籍ID
     * @param chapterId 书籍章节ID
     * @param chapterOrder 书籍章节排序号
     * @param status 翻阅状态，标记当前页或下一页或上一页
     * @return
     */
    public abstract int openBook(int articleID, int chapterId, int chapterOrder,FlipStatus status);


    /**
     * 获取当前章中页数
     * @return
     */
    public abstract int getCountPage();


    public abstract void setAlpha(int alpha);


    /**
     * 设置当前页码
     */
    public abstract void setPageSize(int pageSize);


    /**
     * 获取指定页码内容
     * @param pageSize 指定页码
     * @return
     */
    public abstract BookStatus getPageContent(int pageSize);


}
