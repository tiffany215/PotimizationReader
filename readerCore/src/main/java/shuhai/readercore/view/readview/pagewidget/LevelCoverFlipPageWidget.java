package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import shuhai.readercore.view.readview.displayview.BaseReadViewImpl;
import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;

/**
 * @author 55345364
 * @date 2017/7/18.
 * 水平覆盖翻页效果
 *
 */

public class LevelCoverFlipPageWidget extends HorizontalBaseReadView {


    public LevelCoverFlipPageWidget(Context context, int bookId, int chapterId) {
        super(context, bookId, chapterId);
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
