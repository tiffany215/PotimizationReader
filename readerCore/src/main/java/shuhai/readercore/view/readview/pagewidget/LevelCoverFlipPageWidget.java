package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import shuhai.readercore.view.readview.displayview.BaseReadViewImpl;

/**
 * @author 55345364
 * @date 2017/7/18.
 * 水平覆盖翻页效果
 *
 */

public class LevelCoverFlipPageWidget extends View implements BaseReadViewImpl{
    private static final String TAG = "Lever";

    public LevelCoverFlipPageWidget(Context context) {
        this(context,null);
    }

    public LevelCoverFlipPageWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LevelCoverFlipPageWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e(TAG, "onDraw: ------------------------------------>>" );

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
