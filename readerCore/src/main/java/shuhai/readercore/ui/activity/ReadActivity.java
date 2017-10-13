package shuhai.readercore.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;

import javax.inject.Inject;

import butterknife.InjectView;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.manager.ChapterLoader;
import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.contract.BookReadContract;
import shuhai.readercore.ui.dialog.callback.LoadingCallback;
import shuhai.readercore.ui.presenter.BookReadPresenter;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.view.readview.displayview.BaseReadViewImpl;
import shuhai.readercore.view.readview.displayview.OnReadStateChangeListener;
import shuhai.readercore.view.readview.pagewidget.GLRealFlipPageWidget;
import shuhai.readercore.view.readview.pagewidget.LevelCoverFlipPageWidget;
import shuhai.readercore.view.readview.pagewidget.NoEffectFlipPageWidget;
import shuhai.readercore.view.readview.FlipStatus;

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
//                loadService.showSuccess();
            }
        });
        mFlipMark = 0;
        initPagerWidget();
        mPresenter.attachView(this);
        mPresenter.getBookMixAToc();
    }


    /**
     * 将阅读内容绘制好的View添加到当前容器
     */
    private void initPagerWidget(){
        mPageWidget = new LevelCoverFlipPageWidget(this,mBookId,mChapterId,new ReadListener());
//        mPageWidget = new NoEffectFlipPageWidget(this,mBookId,mChapterId,new ReadListener());
//        mPageWidget = new GLRealFlipPageWidget(this,mBookId,mChapterId);
        mPageWidget.init(ThemeManager.PARCHMENT);
        lsReadWidget.removeAllViews();
        lsReadWidget.addView((View) mPageWidget);
    }


    /**
     * 获取当前章节内容
     */
    public void readCurrentChapter(){
        String str = ChapterLoader.getChapter(mBookId+""+mChapterId);
        if(!TextUtils.isEmpty(str)){
            showChapterRead(mChapterId,FlipStatus.ON_FLIP_CUR);
        }else{
            mPresenter.getChapterRead(mBookId,mChapterId,mChapterOrder,FlipStatus.ON_FLIP_CUR);
        }
    }


    @Override
    public void showBookToc() {
        readCurrentChapter();
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



    /**
     * 阅读状态监听
     */
    private class ReadListener implements OnReadStateChangeListener{

        @Override
        public void onChapterChanged(int chapterId,int chapterOrder,FlipStatus status) {
            mPresenter.getChapterRead(mBookId,chapterId,chapterOrder,status);
        }

        @Override
        public void onPageChanged(int chapterId,int chapterOrder, FlipStatus status) {
            mPageWidget.openBook(mBookId,chapterId, chapterOrder,status);
        }

        @Override
        public void onStartLoadChapter() {
            if(null != loadService){
                loadService.showCallback(LoadingCallback.class);
            }
        }

        @Override
        public void onSuccessLoadChapter() {
            if(null != loadService){
                loadService.showSuccess();
            }
        }
    }

}
