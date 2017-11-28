package shuhai.readercore.view.readview.pagewidget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;


import shuhai.readercore.view.readview.displayview.HorizontalBaseReadView;
import shuhai.readercore.view.readview.status.FlipStatus;
import shuhai.readercore.view.readview.factory.Factory;

/**
 * @author 55345364
 * @date 2017/7/5.
 * <p>
 * 仿真效果翻页组件
 */

public class RealFlipPageWidget extends HorizontalBaseReadView {

    public RealFlipPageWidget(Context context,Dialog loadService) {
        super(context,loadService);
    }

    @Override
    protected void drawPageArea(Canvas canvas, FlipStatus status) {

    }

    @Override
    protected void drawPageShadow(Canvas canvas, FlipStatus status) {

    }

    @Override
    protected void fadePageArea(Factory factory) {

    }

}
