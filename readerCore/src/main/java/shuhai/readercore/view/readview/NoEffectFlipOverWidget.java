package shuhai.readercore.view.readview;

import android.content.Context;
import android.graphics.Canvas;

/**
 * @author 55345364
 * @date 2017/7/5.
 *
 * 无效果翻页组件
 *
 */


public class NoEffectFlipOverWidget extends BaseReadView{

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
    protected void startAnimation() {

    }

    @Override
    protected void abortAnimation() {

    }
}
