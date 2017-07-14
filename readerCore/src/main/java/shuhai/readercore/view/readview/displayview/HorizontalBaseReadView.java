package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.factory.Factory;
import shuhai.readercore.view.readview.factory.PageFactroy;
import shuhai.readercore.view.readview.strategy.HorizantalComposing;

/**
 * @author 55345364
 * @date 2017/7/6.
 *
 * 水平滚动基类
 */

public abstract class HorizontalBaseReadView extends View implements BaseReadViewImpl {

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;

    private Factory factory;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public HorizontalBaseReadView(Context context) {
        super(context);

        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();

        mPrePageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);

        mPrePageCanvas =  new Canvas(mPrePageBitmap);
        mCurPageCanvas =  new Canvas(mCurPageBitmap);
        mNextPageCanvas =  new Canvas(mNextPageBitmap);

        factory = new PageFactroy(context,"1985");
        ((PageFactroy)factory).setChapterLoader();
        ((PageFactroy)factory).setComposingStrategy();




    }

    protected abstract void drawNextPageAreaAndShadow(Canvas canvas);
    protected abstract void drawCurrentPageArea(Canvas canvas);
    protected abstract void drawCurrentPageShadow(Canvas canvas);



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                factory.onDraw(mCurPageCanvas);

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }
}
