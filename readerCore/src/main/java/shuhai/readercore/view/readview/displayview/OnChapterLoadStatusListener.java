package shuhai.readercore.view.readview.displayview;

import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author 55345364
 * @date 2017/11/8.
 */

public interface OnChapterLoadStatusListener {

    void onPageChanged(FlipStatus status);

    void onPageStatus(BookStatus bookStatus);

    void onStartLoading();

    void onEndLoading();




}
