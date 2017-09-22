package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;
import shuhai.readercore.view.readview.displayview.OnReadStateChangeListener;

/**
 * @author 55345364
 * @date 2017/7/5.
 *
 * 无效果翻页组件
 *
 */


public class NoEffectFlipPageWidget extends HorizontalBaseReadView {


    public NoEffectFlipPageWidget(Context context, int bookId, int chapterId,OnReadStateChangeListener listener) {
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
        if(actiondownX < mScreenWidth / 2){
            canvas.drawBitmap(mPrePageBitmap,0,0,null);
        }
    }

    @Override
    protected void drawPrePageShadow(Canvas canvas) {

    }

    @Override
    protected void drawCurPageArea(Canvas canvas) {
        if(actiondownX == 0){
            canvas.drawBitmap(mCurPageBitmap,0,0,null);
        }
    }

    @Override
    protected void drawCurPageShadow(Canvas canvas) {

    }

    @Override
    protected void drawNextPageArea(Canvas canvas) {
        if(actiondownX > mScreenWidth / 2){
            canvas.drawBitmap(mNextPageBitmap,0,0,null);
        }
    }

    @Override
    protected void drawNextPageShadow(Canvas canvas) {

    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){

        }

    }
}
