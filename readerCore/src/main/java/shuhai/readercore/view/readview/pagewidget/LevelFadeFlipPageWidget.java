package shuhai.readercore.view.readview.pagewidget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;


import shuhai.readercore.view.readview.status.FlipStatus;
import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;
import shuhai.readercore.view.readview.factory.Factory;

/**
 * @author 55345364
 * @date 2017/10/16.
 */

public class LevelFadeFlipPageWidget extends HorizontalBaseReadView {
    private static final String TAG = "LevelCoverFlipPageWidget";

    public LevelFadeFlipPageWidget(Context context,Dialog loadService) {
        super(context, loadService);
    }

    @Override
    protected void drawPageArea(Canvas canvas, FlipStatus status) {
        switch (status) {
            case ON_FLIP_PRE:
                canvas.drawBitmap(mCurPageBitmap,0,0,null);
                canvas.drawBitmap(mPrePageBitmap,currPageLeft,0,null);
                break;

            case ON_FLIP_CUR:
                canvas.drawBitmap(mCurPageBitmap,0,0,null);
                break;

            case ON_FLIP_NEXT:
                canvas.drawBitmap(mNextPageBitmap,0,0,null);
                canvas.drawBitmap(mCurPageBitmap,currPageLeft,0,null);
                break;
        }
    }

    @Override
    protected void drawPageShadow(Canvas canvas, FlipStatus status) {
        if (currPageLeft == 0 || currPageLeft == mScreenWidth)
            return;
        RectF rectF = new RectF(currPageLeft, 0, mScreenWidth, mScreenHeight);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        LinearGradient linearGradient = new LinearGradient(currPageLeft, 0,currPageLeft + 26, 0, 0xffaaaaaa, 0x00aaaaaa, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectF, paint);
    }

    @Override
    protected void fadePageArea(Factory factory) {



//        double percent = Math.abs(currPageLeft) / mScreenWidth ;
//
//
//
//        Log.e(TAG, "currPageLeft---------------->: " +   Math.abs(currPageLeft));
//        Log.e(TAG, "mScreenWidth---------------->: " +   mScreenWidth );
//
//        Log.e(TAG, "percent---------------->: " +   percent );
//        if(percent == 0){
//            percent = 1;
//        }
//        factory.setAlpha(Math.abs(currPageLeft));
    }
}
