package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Canvas;

import com.kingja.loadsir.core.LoadService;

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


    public NoEffectFlipPageWidget(Context context,LoadService loadService) {
        super(context,loadService);
    }

    @Override
    protected void drawPageArea(Canvas canvas, FlipStatus status) {
        switch (status){
            case ON_FLIP_PRE:
                canvas.drawBitmap(mPrePageBitmap,0,0,null);
                break;
            case ON_FLIP_CUR:
                canvas.drawBitmap(mCurPageBitmap,0,0,null);
                break;
            case ON_FLIP_NEXT:
                canvas.drawBitmap(mNextPageBitmap,0,0,null);
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
