package shuhai.readercore.views.readview.dataloader;

import shuhai.readercore.views.readview.BookStatus;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public interface ChapterLoaderImpl {


    BookStatus nextPage();

    BookStatus prePage();

    BookStatus curPage();



}
