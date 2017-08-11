package shuhai.readercore.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;


import javax.inject.Inject;

import butterknife.InjectView;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.manager.ChapterLoader;
import shuhai.readercore.ui.contract.BookReadContract;
import shuhai.readercore.ui.presenter.BookReadPresenter;
import shuhai.readercore.view.readview.displayview.BaseReadViewImpl;
import shuhai.readercore.view.readview.pagewidget.NoEffectFlipPageWidget;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public class ReadActivity extends BaseActivity implements BookReadContract.View{



    @InjectView(R.id.flReadWidget)
    FrameLayout lsReadWidget;

    private BaseReadViewImpl mPageWidget;

    @Inject
    BookReadPresenter mPresenter = new BookReadPresenter();


    private int mBookId;

    private int mChapterId;


    @Override
    public int getLayoutId() {
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

        mBookId = 41427;
        mChapterId = 2668827;

        initPagerWidget();

        mPresenter.attachView(this);

        mPresenter.getBookMixAToc();


    }





    /**
     * 将阅读内容绘制好的View添加到当前容器
     */
    private void initPagerWidget(){
        mPageWidget = new NoEffectFlipPageWidget(this,mBookId,mChapterId);
//        mPageWidget.init(ThemeManager.NORMAL);
        lsReadWidget.removeAllViews();
        lsReadWidget.addView((View) mPageWidget);
    }



    public void readCurrentChapter(){
        String str = ChapterLoader.getChapter(mBookId+""+mChapterId);
        if(!TextUtils.isEmpty(str)){
            showChapterRead();
        }else{
            mPresenter.getChapterRead(mBookId,mChapterId);
        }
    }


    @Override
    public void showBookToc() {
        readCurrentChapter();
    }

    @Override
    public void showChapterRead() {
        mPageWidget.init(0);
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
