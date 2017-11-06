package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;

import shuhai.readercore.view.readview.displayview.GLHorizontalBaseReadView;
import shuhai.readercore.view.readview.displayview.OnReadStateChangeListener;
import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author 55345364
 * @date 2017/9/1.
 */

public class GLRealFlipPageWidget extends GLHorizontalBaseReadView {


    public GLRealFlipPageWidget(Context context,int bookId, int chapterId,OnReadStateChangeListener listener) {
        super(context,bookId,chapterId,listener);
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
