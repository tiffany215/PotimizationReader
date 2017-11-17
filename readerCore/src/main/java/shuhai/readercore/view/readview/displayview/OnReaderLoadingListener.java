package shuhai.readercore.view.readview.displayview;


import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author 55345364
 * @date 2017/11/9.
 */

public interface OnReaderLoadingListener {

    void postInvalidatePage();

    void onStartLoading();

    void onEndLoading();

    void onDrawPositionPage(FlipStatus flipStatus);

    void onPageStatus(BookStatus bookStatus);


}
