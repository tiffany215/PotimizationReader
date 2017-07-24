package shuhai.readercore.view.readview.factory;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.dataloader.ChapterLoaderImpl;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public abstract class Factory {



    public abstract <T extends ChapterLoaderImpl> T createChapterLoader(Class<T> clz);

    public abstract BookStatus nextPage();

    public abstract BookStatus prePage();

    public abstract BookStatus curPage();

    public abstract void onDraw(Canvas canvas);

    public abstract void setBgBitmap(Bitmap bitmap);

    public abstract int openBook(int articleID,int chapterId,int curPage);


}
