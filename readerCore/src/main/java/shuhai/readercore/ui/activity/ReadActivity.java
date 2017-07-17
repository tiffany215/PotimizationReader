package shuhai.readercore.ui.activity;

import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import butterknife.Bind;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.contract.BookReadContract;
import shuhai.readercore.view.readview.displayview.BaseReadViewImpl;
import shuhai.readercore.view.readview.pagewidget.LevelFlipOverWidget;
import shuhai.readercore.view.readview.pagewidget.NoEffectFlipOverWidget;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public class ReadActivity extends BaseActivity implements BookReadContract.View{



    @Bind(R.id.flReadWidget)
    FrameLayout lsReadWidget;

    private BaseReadViewImpl mPageWidget;


    @Override
    public int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        return R.layout.activity_read;
    }

    @Override
    public void initData() {


    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void configViews() {
        initPagerWidget();
    }





    /**
     * 将阅读内容绘制好的View添加到当前容器
     */
    private void initPagerWidget(){
        mPageWidget = new LevelFlipOverWidget(this);
        mPageWidget.init(ThemeManager.NORMAL);
        lsReadWidget.removeAllViews();
        lsReadWidget.addView((View) mPageWidget);
    }


    @Override
    public void showChapterRead() {

    }

    @Override
    public void netError() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }
}
