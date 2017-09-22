package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Canvas;

import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;
import shuhai.readercore.view.readview.displayview.OnReadStateChangeListener;

/**
 * @author 55345364
 * @date 2017/7/5.
 * <p>
 * 仿真效果翻页组件
 */

public class RealFlipPageWidget extends HorizontalBaseReadView {

    public RealFlipPageWidget(Context context, int bookId, int chapterId,OnReadStateChangeListener listener) {
        super(context, bookId, chapterId,listener);
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

    @Override
    protected void drawPrePageArea(Canvas canvas) {

    }

    @Override
    protected void drawPrePageShadow(Canvas canvas) {

    }

    @Override
    protected void drawCurPageArea(Canvas canvas) {

    }

    @Override
    protected void drawCurPageShadow(Canvas canvas) {

    }

    @Override
    protected void drawNextPageArea(Canvas canvas) {

    }

    @Override
    protected void drawNextPageShadow(Canvas canvas) {

    }
}
