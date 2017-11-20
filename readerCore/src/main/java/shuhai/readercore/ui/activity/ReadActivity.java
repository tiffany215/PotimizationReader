package shuhai.readercore.ui.activity;

import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;

import javax.inject.Inject;

import butterknife.InjectView;
import shuhai.readercore.Constants;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.manager.ThemeManager;
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
    public void initData() {
        mBookId = getIntent().getIntExtra("read.book.id",0);
        mChapterId = UserSP.getInstance().getLastReaderChapterId(mBookId);
        mChapterOrder = UserSP.getInstance().getLastReaderChapterOrder(mBookId);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void configViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        loadService = LoadSir.getDefault().register(mContext, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
//              loadService.showCallback(LoadingCallback.class);
//              loadService.showSuccess();
            }
        });
        mFlipMark = 0;
        initPagerWidget();
        mPresenter.attachView(this);
        mPageWidget.openBook(mBookId,mChapterId,UserSP.getInstance().getLastReaderChapterOrder(mBookId),FlipStatus.ON_FLIP_CUR);
    }


    /**
     * 将阅读内容绘制好的View添加到当前容器
     */
    private void initPagerWidget(){
        switch (ReaderSP.getInstance().getFlipModel()) {
            case Constants.FLIP_CONFIG.LEVEL_NO_FLIP:
                mPageWidget = new NoEffectFlipPageWidget(this,loadService);
                break;
            case Constants.FLIP_CONFIG.LEVEL_COVER_FLIP:
                mPageWidget = new LevelCoverFlipPageWidget(this,loadService);
                break;
            case Constants.FLIP_CONFIG.LEVEL_SCROLLER_FLIP:
                mPageWidget = new LevelScrollFlipPageWidget(this,loadService);
                break;
            case Constants.FLIP_CONFIG.LEVEL_REAL_FLIP:
                mPageWidget = new GLRealFlipPageWidget(this,loadService);
                break;
        }
        mPageWidget.init(ThemeManager.CLASSICAL);
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
        if(null != loadService){
            loadService.showSuccess();
        }
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
