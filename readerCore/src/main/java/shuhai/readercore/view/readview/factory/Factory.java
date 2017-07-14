package shuhai.readercore.view.readview.factory;

import android.graphics.Canvas;

import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.dataloader.ChapterLoaderImpl;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public abstract class Factory {

    public abstract <T extends ChapterLoaderImpl> T createChapterLoader(Class<T> clz);

     abstract BookStatus nextPage();

     abstract BookStatus prePage();

     abstract BookStatus curPage();

    public abstract void onDraw(Canvas canvas);

}
