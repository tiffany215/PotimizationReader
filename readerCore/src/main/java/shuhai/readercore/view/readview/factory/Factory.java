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



    public abstract <T extends ChapterLoaderImpl> T createChapterLoader(Class<T> clz);

    /**
     * 获取下一页内容
     * @return
     */
    public abstract BookStatus nextPage();

    /**
     * 获取上一页内容
     * @return
     */
    public abstract BookStatus prePage();

    /**
     * 获取当前页内容
     * @return
     */
    public abstract BookStatus curPage();


    public abstract void preChapter();

    public abstract void curChapter();

    public abstract void nextChapter();



    public abstract void onDraw(Canvas canvas);

    public abstract void setBgBitmap(Bitmap bitmap);

    public abstract int openBook(int articleID, int chapterId, int chapterOrder,FlipStatus status);


}
