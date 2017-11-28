package shuhai.readercore.view.readview.pagewidget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;


import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;
import shuhai.readercore.view.readview.status.FlipStatus;
import shuhai.readercore.view.readview.factory.Factory;

/**
 * @author 55345364
 * @date 2017/7/5.
 *
 * 无效果翻页组件
 *
 */


public class NoEffectFlipPageWidget extends HorizontalBaseReadView {


    public NoEffectFlipPageWidget(Context context,Dialog loadService) {
        super(context,loadService);
        scrollerSpeed = 100;
    }

    @Override
    protected void drawPageArea(Canvas canvas, FlipStatus status) {
        switch (status){
            case ON_FLIP_PRE:
                canvas.drawBitmap(mNextPageBitmap,0,0,null);
                canvas.drawBitmap(mCurPageBitmap,0,0,null);
                canvas.drawBitmap(mPrePageBitmap,0,0,null);
                break;
            case ON_FLIP_CUR:
                canvas.drawBitmap(mCurPageBitmap,0,0,null);
                break;
            case ON_FLIP_NEXT:
                canvas.drawBitmap(mNextPageBitmap,0,0,null);
                canvas.drawBitmap(mCurPageBitmap,0,0,null);
                canvas.drawBitmap(mPrePageBitmap,0,0,null);
                break;
        }
    }

    @Override
    protected void drawPageShadow(Canvas canvas, FlipStatus status) {

    }

    @Override
    protected void fadePageArea(Factory factory) {

    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){

        }

    }
}
