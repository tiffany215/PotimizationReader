package shuhai.readercore.view.readview.displayview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import android.widget.Toast;


import shuhai.readercore.common.Constants;
import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.dialog.BookReadSettingDialog;
import shuhai.readercore.ui.dialog.LoadingCallback;
import shuhai.readercore.ui.sharedp.ReaderSP;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.utils.ToastUtils;
import shuhai.readercore.view.readview.dataloader.HorizontalScrollChapterLoader;
import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.factory.Factory;
import shuhai.readercore.view.readview.factory.PageFactory;
import shuhai.readercore.view.readview.status.FlipStatus;
import shuhai.readercore.view.readview.status.ScrollStatus;

/**
 * @author 55345364
 * @date 2017/7/6.
 *
 * 水平滚动基类
 */

public abstract class HorizontalBaseReadView extends View implements BaseReadViewImpl {

    private static final String TAG = "HorizontalBaseReadView";
    protected int mScreenWidth;
    protected int mScreenHeight;

    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;

    // 前一页，当前页，下一页的左边位置
    public int currPageLeft = 0, currPageRight = 0;

    //获取滑动速度
    private VelocityTracker vt;
    //当前的滑动速度
    private int mSpeed;

    //判定为拖动的最小移动像素数
    private int mTouchSlop;

    //手指滑动的距离
    private  int moveLength;

    /**
     * 手机按下时的屏幕坐标
     */
    private int mXDown;

    /**
     * 手机当时所处的屏幕坐标
     */
    private int mXMove;

    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private int mXLastMove;

    /**
     * 过滤多点触碰的控制变量
     */
    private int mEvents;

    private PageFactory factory;

    public Scroller mScroller;

    private FlipStatus mFlipStatus;

    private BookStatus mBookStatus;

    private ScrollStatus mScrollStatus;

    private Dialog mLoadingDialog;


    public HorizontalBaseReadView(Context context,Dialog dialog) {
        super(context);
        this.mLoadingDialog = dialog;
        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();

        mPrePageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);

        mPrePageCanvas =  new Canvas(mPrePageBitmap);
        mCurPageCanvas =  new Canvas(mCurPageBitmap);
        mNextPageCanvas =  new Canvas(mNextPageBitmap);

        mScroller = new Scroller(getContext());

        factory = new PageFactory.Builder(context)
                .setLoadStrategy(HorizontalScrollChapterLoader.class)
                .setTextPartFontSize(ReaderSP.getInstance().getTextSize())
                .setLineSpace(ReaderSP.getInstance().getLineSpace())
                .setOnReaderLoadingListener(new MyOnReaderLoadingListener())
                .builder();

        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        initParams();

    }


    /**
     * 初始化参数
     */
    private void initParams(){
        mFlipStatus = FlipStatus.ON_FLIP_CUR;
        mBookStatus = BookStatus.LOAD_START;
        currPageLeft = 0;
    }


    /**
     * 主题设置OnChapterLoadStatusListener
     * @param theme
     */
    public synchronized void init(int theme){
        factory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
    }


    @Override
    public synchronized void openBook(int articleId,int chapterId,int chapterOrder,FlipStatus status){
        mBookStatus = factory.openBook(articleId,chapterId,chapterOrder,status);
        switch (mBookStatus) {
            case LOAD_START:
                Toast.makeText(getContext(),"开始加载章节内容！",Toast.LENGTH_LONG).show();
                break;
            case LOAD_NET_WORT_ERROR:
                ToastUtils.getSingleToast("网络异常，请稍后重试！",Toast.LENGTH_SHORT).show();
                if(null != mLoadingDialog){
                    mLoadingDialog.cancel();
                }
                break;

            case LOAD_SUCCESS:
                if(null != mLoadingDialog){
                    mLoadingDialog.cancel();
                }
                factory.prePage(mPrePageCanvas);
                factory.curPage(mCurPageCanvas);
                factory.nextPage(mNextPageCanvas);
                mScrollStatus = ScrollStatus.STATE_STOP;
                mFlipStatus = status;
                postInvalidate();
                break;
        }
    }


    /**
     * 阅读章节加载状态监听器
     */
    private class MyOnReaderLoadingListener implements OnReaderLoadingListener{

        @Override
        public void postInvalidatePage() {
            postInvalidate();
        }

        @Override
        public void postOnDrawableInvalidatePage() {
            factory.onDraw(mPrePageCanvas);
            factory.onDraw(mCurPageCanvas);
            factory.onDraw(mNextPageCanvas);
            postInvalidate();
        }

        @Override
        public void InvalidatePage() {
            invalidate();
        }

        @Override
        public void onStartLoading() {
            if(null != mLoadingDialog){
                mLoadingDialog.show();
            }
        }

        @Override
        public void onEndLoading() {
            if(null != mLoadingDialog){
                mLoadingDialog.cancel();
            }
        }


        @Override
        public void onDrawPositionPage(FlipStatus flipStatus) {
            mScrollStatus = ScrollStatus.STATE_STOP;
            switch (flipStatus) {
                case ON_FLIP_PRE:
                    factory.postInvalidatePage(mPrePageCanvas,flipStatus);
                    break;
                case ON_FLIP_CUR:
                    factory.postInvalidatePage(mCurPageCanvas,flipStatus);
                    break;
                case ON_FLIP_NEXT:
                    factory.postInvalidatePage(mNextPageCanvas,flipStatus);
                    break;
                case ON_PRE_CHAPTER_LAST_PAGE:
                    factory.postInvalidatePage(mPrePageCanvas,flipStatus);
                    break;
                case ON_NEXT_CHAPTER_FIRST_PAGE:
                    factory.postInvalidatePage(mNextPageCanvas,flipStatus);
                    break;
            }
        }

        @Override
        public void onPageStatus(BookStatus bookStatus) {
            mBookStatus = bookStatus;
            switch (mBookStatus) {
                case LOAD_ERROR:
                    ToastUtils.showToast("章节加载失败！");
                    break;
                case NO_PRE_PAGE:
                    ToastUtils.showToast("没有上一章了！");
                    break;
                case NEED_BUY_CHAPTER:
                    ToastUtils.showToast("此章节需要付费！");
                    break;
                case LOAD_NET_WORT_ERROR:
                    ToastUtils.showToast("网络连接失败，请稍后再试！");
                    mBookStatus = BookStatus.NO_NEXT_PAGE;
                    break;
            }
            if(null != mLoadingDialog){
                mLoadingDialog.cancel();
            }
        }
    }





    /**
     * 手指触摸屏幕事件处理
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(mScrollStatus == ScrollStatus.STATE_MOVE){
                    return false;
                }
                mXDown = (int) event.getRawX();
                mXLastMove = mXDown;
                try {
                    if(vt == null){
                        vt = VelocityTracker.obtain();
                    }else{
                        vt.clear();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                vt.addMovement(event);
                mEvents = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mEvents = -1;
            case MotionEvent.ACTION_MOVE:
                vt.addMovement(event);
                vt.computeCurrentVelocity(600);
                mSpeed = (int) vt.getXVelocity();
                mXMove = (int) event.getRawX();
                moveLength = mXMove - mXLastMove;
                if(moveLength > 0 && mEvents == 0){
                    mFlipStatus = FlipStatus.ON_FLIP_PRE;
                }else if(moveLength < 0 && mEvents == 0){
                    mFlipStatus = FlipStatus.ON_FLIP_NEXT;
                }
                if(Math.abs(moveLength) > mTouchSlop){
                    mScrollStatus = ScrollStatus.STATE_MOVE;
                }
                currPageLeft = moveLength;
                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
                //判断是否弹出阅读设置框，判断条件为 滑动距离小于最小拖动小像素 && 点击区域是否在页面中心 && 滑动速度小于最小拖动像素
                if(getTouchArea(event.getRawX(),event.getRawY()) == Constants.PAGE_TOUCH_AREA.SETTING_AREA
                        && Math.abs(moveLength) < mTouchSlop && mScrollStatus != ScrollStatus.STATE_MOVE){
                    new BookReadSettingDialog(getContext(), factory).show();
                    return true;
                }
                if(Math.abs(mSpeed) <= mTouchSlop){
                    mSpeed = 0;
                }
                startPageScroller(moveLength,event);
                try
                {
                    vt.clear();
                    vt.recycle();
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }


    /**
     * 开始页面滚动
     * mSpeed为正数 表明手指快速向右甩动
     * moveLength 为正数 表明手指向右滑动
     *
     */
    private void startPageScroller(int startX,MotionEvent event){
        int scrollDistance = 0;
        if(mSpeed > 0){
            //手指向右移动
            if(moveLength < 0){
                scrollDistance = Math.abs(moveLength);
            }else if(moveLength > 0){
                scrollDistance = mScreenWidth - Math.abs(moveLength);
            }
            //向右滚动
        }else if(mSpeed == 0){
            //此状态为在页面点击状态
            if(mScrollStatus == ScrollStatus.STATE_STOP || Math.abs(startX) < mTouchSlop){
                if(getTouchArea(event.getRawX(),event.getRawY()) == Constants.PAGE_TOUCH_AREA.NEXT_AREA){
                    startX = 0;
                    scrollDistance = -mScreenWidth;
                    mFlipStatus = FlipStatus.ON_FLIP_NEXT;
                }else if(getTouchArea(event.getRawX(),event.getRawY()) == Constants.PAGE_TOUCH_AREA.PREVIOUS_AREA){
                    startX = 0;
                    scrollDistance = mScreenWidth;
                    mFlipStatus = FlipStatus.ON_FLIP_PRE;
                }
            }else if(Math.abs(moveLength) > mScreenWidth / 2 && moveLength > 0){
                //向右滚动
                scrollDistance = mScreenWidth - Math.abs(moveLength);
            }else if(Math.abs(moveLength) < mScreenWidth / 2 && moveLength > 0){
                //向左回弹
                scrollDistance = -Math.abs(moveLength);
            }else if(Math.abs(moveLength) > mScreenWidth / 2 && moveLength < 0){
                //向左滚动
                scrollDistance = -(mScreenWidth - Math.abs(moveLength));
            }else if(Math.abs(moveLength) < mScreenWidth / 2 && moveLength < 0){
                //向右回弹
                scrollDistance = Math.abs(moveLength);
            }
        }else if(mSpeed < 0){
            //手指向右移动
            if(moveLength > 0){
                scrollDistance = -Math.abs(moveLength);
            }else if(moveLength < 0){
                scrollDistance = -(mScreenWidth - Math.abs(moveLength));
            }
            //向左滚动
        }
        /**
         * startX 为负值向左滚动
         */
        mScroller.startScroll(startX,0,scrollDistance,0,600);
        postInvalidate();
    }


    /**
     * 判断点击的区域
     *
     * @param x
     * @param y
     * @return
     */
    private int getTouchArea(float x, float y) {
        if (x > mScreenWidth / 4 && x < mScreenWidth * 3 / 4
                && y > mScreenHeight / 4 && y < mScreenHeight * 3 / 4) {
            return Constants.PAGE_TOUCH_AREA.SETTING_AREA;
        } else if (x > mScreenWidth / 2) {
            return Constants.PAGE_TOUCH_AREA.NEXT_AREA;
        }
        return Constants.PAGE_TOUCH_AREA.PREVIOUS_AREA;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawPageArea(canvas,mFlipStatus);
        drawPageShadow(canvas,mFlipStatus);
        fadePageArea(factory);
    }

    protected abstract void drawPageArea(Canvas canvas, FlipStatus status);
    protected abstract void drawPageShadow(Canvas canvas,FlipStatus status);
    protected abstract void fadePageArea(Factory factory);


    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()) {
            currPageLeft = mScroller.getCurrX();
            if(Math.abs(currPageLeft) == mScreenWidth || Math.abs(currPageLeft) == 0){
                drawablePage();
            }
            postInvalidate();

        }
    }


    /**
     * 翻页完成重新绘制页面
     */
    private void drawablePage(){
        if(mScroller.getCurrX() < 0){
            factory.autoIncrease();
            factory.prePage(mPrePageCanvas);
            factory.curPage(mCurPageCanvas);
            if(factory.nextPage(mNextPageCanvas)){
                mBookStatus = BookStatus.LOAD_SUCCESS;
            }
            factory.nextPage(mNextPageCanvas);
            mFlipStatus = FlipStatus.ON_FLIP_NEXT;
        }else if(mScroller.getCurrX() > 0){
            factory.autoReduce();
            factory.nextPage(mNextPageCanvas);
            factory.curPage(mCurPageCanvas);
            if( factory.prePage(mPrePageCanvas)){
                mBookStatus = BookStatus.LOAD_SUCCESS;
            }
            mFlipStatus = FlipStatus.ON_FLIP_PRE;
        }
        currPageLeft = 0;
        mScrollStatus = ScrollStatus.STATE_STOP;
        mScroller.abortAnimation();
    }


    @Override
    public void closeBook() {
        factory.closeBook();
    }
}



