package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

import com.eschao.android.widget.pageflip.Page;
import com.eschao.android.widget.pageflip.PageFlipState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import shuhai.readercore.view.readview.displayview.GLHorizontalBaseReadView;

/**
 * @author 55345364
 * @date 2017/9/1.
 */

public class GLRealFlipPageWidget extends GLHorizontalBaseReadView {


    public GLRealFlipPageWidget(Context context,int bookId, int chapterId) {
        super(context,bookId,chapterId);
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
