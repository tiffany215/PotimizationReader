package shuhai.readercore.view.readview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.BaseReadImpl;

/**
 * @author 55345364
 * @date 2017/7/6.
 *
 * 水平滚动基类
 */

public abstract class HorizontalBaseReadView extends View implements BaseReadImpl {

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;









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


    }

    protected abstract void drawNextPageAreaAndShadow(Canvas canvas);
    protected abstract void drawCurrentPageArea(Canvas canvas);
    protected abstract void drawCurrentPageShadow(Canvas canvas);


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
    public void init(int theme) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }
}
