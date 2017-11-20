package shuhai.readercore.view.readview.factory;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.dataloader.ChapterLoaderStrategyImpl;
import shuhai.readercore.view.readview.status.FlipStatus;

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
    public static <T extends ChapterLoaderStrategyImpl> T createChapterLoader(Class<T> clz) {
        ChapterLoaderStrategyImpl chapterLoader = null;
        try {
            chapterLoader = (ChapterLoaderStrategyImpl) Class.forName(clz.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) chapterLoader;
    }


    public abstract boolean prePage(Canvas canvas);

    public abstract boolean curPage(Canvas canvas);

    public abstract boolean nextPage(Canvas canvas);

    public abstract void autoIncrease();

    public abstract void autoReduce();

    public abstract void postInvalidatePage(Canvas canvas,FlipStatus status);

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
    public abstract BookStatus openBook(int articleID, int chapterId, int chapterOrder,FlipStatus status);


//    /**
//     * 获取当前章中页数
//     * @return
//     */
    public abstract int getCountPage();


    public abstract int getPageSize();
//
//
//    public abstract String getChapterName();

    public abstract void setAlpha(int alpha);

    public abstract void closeBook();





}
