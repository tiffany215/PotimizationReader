package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Canvas;

import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;

/**
 * @author 55345364
 * @date 2017/7/5.
 * 水平翻页效果组件
 */

public class LevelFlipOverWidget extends HorizontalBaseReadView {

    public LevelFlipOverWidget(Context context) {
        super(context);
    }

    @Override
    protected void drawNextPageAreaAndShadow(Canvas canvas) {

    }

    @Override
    protected void drawCurrentPageArea(Canvas canvas) {

        canvas.drawBitmap(mCurPageBitmap,0,0,null);
//        canvas.translate(mTouch.x,mTouch.y);
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


    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            postInvalidate();
        }


    }
}
