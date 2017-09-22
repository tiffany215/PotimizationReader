package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import shuhai.readercore.utils.ScreenUtils;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public abstract class VerticalBaseReadView extends View implements BaseReadViewImpl {

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;



    public VerticalBaseReadView(Context context) {
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


    @Override
    public void init(int theme) {

    }

    @Override
    public void openBook(int articleId,int chapterId,int chapterOrder,int curPage) {

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
