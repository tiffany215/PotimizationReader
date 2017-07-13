package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.graphics.Canvas;

import shuhai.readercore.view.readview.view.HorizontalBaseReadView;

/**
 * @author 55345364
 * @date 2017/7/5.
 *
 * 无效果翻页组件
 *
 */


public class NoEffectFlipOverWidget extends HorizontalBaseReadView {

    public NoEffectFlipOverWidget(Context context) {
        super(context);
    }

    @Override
    protected void drawNextPageAreaAndShadow(Canvas canvas) {

    }

    @Override
    protected void drawCurrentPageArea(Canvas canvas) {

    }

    @Override
    protected void drawCurrentPageShadow(Canvas canvas) {

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