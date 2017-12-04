package shuhai.readercore.view.readview.pagewidget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;


import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;
import shuhai.readercore.view.readview.status.FlipStatus;
import shuhai.readercore.view.readview.factory.Factory;

/**
 * @author 55345364
 * @date 2017/7/18.
 * 水平覆盖翻页效果
 *
 */

public class LevelCoverFlipPageWidget extends HorizontalBaseReadView {


    private static final String TAG = "LevelCoverFlipPageWidget";

    private Paint paintShadow;

    public LevelCoverFlipPageWidget(Context context, Dialog loadService) {
       super(context,loadService);
        paintShadow = new Paint();
        paintShadow.setAntiAlias(true);
        paintShadow.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void drawPageArea(Canvas canvas, FlipStatus status) {
        switch (status) {
            case ON_FLIP_PRE:
                canvas.drawBitmap(mCurPageBitmap,0,0,null);
                canvas.drawBitmap(mPrePageBitmap,-mScreenWidth + currPageLeft,0,null);
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

        int pageShadow = 0;
        switch (status) {
            case ON_FLIP_NEXT:
                pageShadow = mScreenWidth + currPageLeft;
                break;
            case ON_FLIP_PRE:
                pageShadow = currPageLeft;
                break;
        }
        if (pageShadow == 0 || pageShadow == mScreenWidth)
            return;
        Log.e(TAG, "---------->>" + pageShadow );
        RectF rectF = new RectF(pageShadow, 0, mScreenWidth, mScreenHeight);
        LinearGradient linearGradient = new LinearGradient(pageShadow, 0,pageShadow + 26, 0, 0xff555555, 0x00aaaaaa, Shader.TileMode.CLAMP);
        paintShadow.setShader(linearGradient);
        canvas.drawRect(rectF, paintShadow);
    }

    @Override
    protected void fadePageArea(Factory factory) {

    }


}
