package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import shuhai.readercore.view.readview.displayview.GLHorizontalBaseReadView;

/**
 * @author 55345364
 * @date 2017/9/1.
 */

public class GLRealFlipPageWidget extends GLHorizontalBaseReadView {

    public GLRealFlipPageWidget(Context context, int bookId, int chapterId) {
        super(context,bookId,chapterId);
    }


    @Override
    public void drawPage(Canvas canvas,Bitmap bitmap) {
        Paint p = new Paint();
        p.setFilterBitmap(true);

        // 1. draw background bitmap
        Rect rect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bitmap, null, rect, p);
        bitmap.recycle();
        bitmap = null;
    }

    @Override
    protected void drawPrePageArea(Canvas canvas) {
        this.drawPage(canvas,mPrePageBitmap);
    }

    @Override
    protected void drawCurPageArea(Canvas canvas) {
        this.drawPage(canvas,mCurPageBitmap);
    }

    @Override
    protected void drawNextPageArea(Canvas canvas) {
        this.drawPage(canvas,mNextPageBitmap);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

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
    public void init(int theme) {

    }
}
