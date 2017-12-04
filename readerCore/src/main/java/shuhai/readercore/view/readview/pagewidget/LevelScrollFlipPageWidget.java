package shuhai.readercore.view.readview.pagewidget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;


import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;
import shuhai.readercore.view.readview.status.FlipStatus;
import shuhai.readercore.view.readview.factory.Factory;

/**
 * @author 55345364
 * @date 2017/7/5.
 * 水平翻页效果组件
 */

public class LevelScrollFlipPageWidget extends HorizontalBaseReadView {


    private static final String TAG = "LevelScrollFlipPageWidget";

    public LevelScrollFlipPageWidget(Context context, Dialog loadService) {
        super(context,loadService);
    }

    @Override
    protected void drawPageArea(Canvas canvas, FlipStatus status) {
        switch (status) {
            case ON_FLIP_PRE:
                  canvas.drawBitmap(mCurPageBitmap, currPageLeft, 0, null);
                  canvas.drawBitmap(mPrePageBitmap, -mScreenWidth + currPageLeft, 0, null);
                break;
            case ON_FLIP_CUR:
                canvas.drawBitmap(mCurPageBitmap, 0, 0, null);
                break;
            case ON_FLIP_NEXT:
                canvas.drawBitmap(mNextPageBitmap, mScreenWidth + currPageLeft, 0, null);
                canvas.drawBitmap(mCurPageBitmap, currPageLeft, 0, null);
                break;
        }
    }

    @Override
    protected void drawPageShadow(Canvas canvas, FlipStatus status) {
    }

    @Override
    protected void fadePageArea(Factory factory) {
    }


}
