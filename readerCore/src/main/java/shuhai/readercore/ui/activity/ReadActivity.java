package shuhai.readercore.ui.activity;

import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.dyhdyh.widget.loading.dialog.LoadingDialog;

import javax.inject.Inject;

import butterknife.InjectView;
import shuhai.readercore.common.Constants;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.ui.contract.BookReadContract;
import shuhai.readercore.ui.presenter.BookReadPresenter;
import shuhai.readercore.ui.sharedp.ReaderSP;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.utils.ActivityUtils;
import shuhai.readercore.view.readview.displayview.BaseReadViewImpl;
import shuhai.readercore.view.readview.pagewidget.GLRealFlipPageWidget;
import shuhai.readercore.view.readview.pagewidget.LevelCoverFlipPageWidget;
import shuhai.readercore.view.readview.pagewidget.LevelScrollFlipPageWidget;
import shuhai.readercore.view.readview.pagewidget.NoEffectFlipPageWidget;
import shuhai.readercore.view.readview.status.FlipStatus;

/**
 *
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

    private int mChapterOrder;

    private int mFlipMark;


    @Override
    public int getLayoutId() {
        return R.layout.activity_read;
    }


    @Override
    protected void onResume() {
        super.onResume();
        initPagerWidget();
        mPresenter.attachView(this);
        mPageWidget.openBook(mBookId,mChapterId,mChapterOrder,FlipStatus.ON_FLIP_CUR);
    }

    @Override
    public void initData() {
        mBookId = getIntent().getIntExtra("read.book.id",0);
        mChapterId = getIntent().getIntExtra("read.chapter.id",0);
        mChapterOrder = getIntent().getIntExtra("read.chapter.order",0);
        if(mChapterId  == 0 || mChapterOrder == 0){
            mChapterId = UserSP.getInstance().getLastReaderChapterId(mBookId);
            mChapterOrder = UserSP.getInstance().getLastReaderChapterOrder(mBookId);
        }
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void configViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mFlipMark = 0;
    }

    /**
     * 将阅读内容绘制好的View添加到当前容器
     */
    private void initPagerWidget(){
        switch (ReaderSP.getInstance().getFlipModel()) {
            case Constants.FLIP_CONFIG.LEVEL_NO_FLIP:
                mPageWidget = new NoEffectFlipPageWidget(this,loadingDialog);
                break;
            case Constants.FLIP_CONFIG.LEVEL_COVER_FLIP:
                mPageWidget = new LevelCoverFlipPageWidget(this,loadingDialog);
                break;
            case Constants.FLIP_CONFIG.LEVEL_SCROLLER_FLIP:
                mPageWidget = new LevelScrollFlipPageWidget(this,loadingDialog);
                break;
            case Constants.FLIP_CONFIG.LEVEL_REAL_FLIP:
                mPageWidget = new GLRealFlipPageWidget(this,loadingDialog);
                break;
        }
        mPageWidget.init(ReaderSP.getInstance().getReaderTheme());
        lsReadWidget.removeAllViews();
        lsReadWidget.addView((View) mPageWidget);
    }




    @Override
    public void showBookToc() {

    }


    /**
     * 加载书籍内容
     */
    @Override
    public void showChapterRead(int chapterId,FlipStatus status) {
        mPageWidget.openBook(mBookId,chapterId,UserSP.getInstance().getLastReaderChapterOrder(mBookId),status);
    }

    @Override
    public void netError() {

    }

    @Override
    public void showError() {
        LoadingDialog.cancel();
    }

    @Override
    public void complete() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.finishActivity(this);
        mPageWidget.closeBook();
    }
}
