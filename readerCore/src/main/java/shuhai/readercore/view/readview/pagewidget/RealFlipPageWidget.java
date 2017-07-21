package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Canvas;

import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;

/**
 * @author 55345364
 * @date 2017/7/5.
 * <p>
 * 仿真效果翻页组件
 */

public class RealFlipPageWidget extends HorizontalBaseReadView {

    public RealFlipPageWidget(Context context,int bookId,int chapterId) {
        super(context,bookId,chapterId);
    }

    @Override
    protected void drawNextPageAreaAndShadow(Canvas canvas) {

    }

    @Override
    protected void drawCurrentPageArea(Canvas canvas) {

    }

    @Override
    protected void drawCurrentPageShadow(Canvas canvas) {

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
