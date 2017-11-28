package shuhai.readercore.view.readview.displayview;


import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author 55345364
 * @date 2017/11/9.
 */

public interface OnReaderLoadingListener {

    /**
     * 刷新View界面
     */
    void postInvalidatePage();


    /**
     * 刷新View界面(此方法会重新绘制：前一页、当前页、下一页的文字内容、背景等素材到指定的Bitmap上);
     * 主要用于阅读主题设置、字体大小设置、行段间距设置等。
     */
    void postOnDrawableInvalidatePage();



    void InvalidatePage();

    /**
     * 吊起加载页面
     */
    void onStartLoading();

    /**
     * 销毁加载页面
     */
    void onEndLoading();

    /**
     * 绘制指定页面（绘制文字内容到Bitmap上）
     * @param flipStatus
     */
    void onDrawPositionPage(FlipStatus flipStatus);


    /**
     * 页面状态（加载完成、是否有上一章、下一章等、、、）
     * @param bookStatus
     */
    void onPageStatus(BookStatus bookStatus);


}
