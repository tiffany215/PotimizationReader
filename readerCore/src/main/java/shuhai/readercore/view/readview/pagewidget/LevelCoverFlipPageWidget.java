package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;
import shuhai.readercore.view.readview.displayview.OnReadStateChangeListener;
import shuhai.readercore.view.readview.FlipStatus;

/**
 * @author 55345364
 * @date 2017/7/18.
 * 水平覆盖翻页效果
 *
 */

public class LevelCoverFlipPageWidget extends HorizontalBaseReadView {


    private static final String TAG = "LevelCoverFlipPageWidget";

    public LevelCoverFlipPageWidget(Context context, int bookId, int chapterId, OnReadStateChangeListener listener) {
        super(context, bookId, chapterId,listener);
    }

    @Override
    protected void drawPageArea(Canvas canvas, FlipStatus status) {
        switch (status) {
            case ON_FLIP_PRE:
                canvas.drawBitmap(mCurPageBitmap,0,0,null);
                canvas.drawBitmap(mPrePageBitmap,prePageLeft,0,null);
                Log.e(TAG, "prePageLeft--------------------->: " + prePageLeft);



                break;

            case ON_FLIP_CUR:
                canvas.drawBitmap(mCurPageBitmap,0,0,null);
                break;

            case ON_FLIP_NEXT:
                canvas.drawBitmap(mNextPageBitmap,0,0,null);
                canvas.drawBitmap(mCurPageBitmap,currPageLeft,0,null);
//                Log.e(TAG, "currPageLeft--------------------->: " + currPageLeft);
                break;
        }
    }

    @Override
    protected void drawPageShadow(Canvas canvas, FlipStatus status) {
        if (right == 0 || right == mScreenWidth)
            return;
        RectF rectF = new RectF(right, 0, mScreenWidth, mScreenHeight);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        LinearGradient linearGradient = new LinearGradient(right, 0,right + 26, 0, 0xffaaaaaa, 0x00aaaaaa, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectF, paint);
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
