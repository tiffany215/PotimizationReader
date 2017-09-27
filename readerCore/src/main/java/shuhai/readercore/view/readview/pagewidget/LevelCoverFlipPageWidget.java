package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Canvas;
import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;
import shuhai.readercore.view.readview.displayview.OnReadStateChangeListener;
import shuhai.readercore.view.readview.FlipStatus;

/**
 * @author 55345364
 * @date 2017/7/18.
 * 水平覆盖翻页效果
 *
 */

public class LevelCoverFlipPageWidget extends HorizontalBaseReadView {


    public LevelCoverFlipPageWidget(Context context, int bookId, int chapterId,OnReadStateChangeListener listener) {
        super(context, bookId, chapterId,listener);
    }

    @Override
    protected void drawPageArea(Canvas canvas, FlipStatus status) {

    }

    @Override
    protected void drawPageShadow(Canvas canvas, FlipStatus status) {

    }

    @Override
    public void startAnimation() {

    }

    @Override
    public void abortAnimation() {

    }

    @Override
    public void restoreAnimation() {

    }


}
