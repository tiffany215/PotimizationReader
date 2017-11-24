package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import android.widget.Toast;

import com.kingja.loadsir.core.LoadService;

import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.dialog.BookReadSettingDialog;
import shuhai.readercore.ui.dialog.LoadingCallback;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.utils.ToastUtils;
import shuhai.readercore.view.readview.dataloader.HorizontalScrollChapterLoader;
import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.factory.Factory;
import shuhai.readercore.view.readview.factory.PageFactory;
import shuhai.readercore.view.readview.status.FlipStatus;

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

    // 页面状态
    public static final int STATE_MOVE = 0;
    public static final int STATE_STOP = 1;
    public int state;

    // 点击页面区域
    private static final int PREVIOUS_AREA = 1;
    private static final int NEXT_AREA = 2;
    private static final int SETTING_AREA = 3;

    // 前一页，当前页，下一页的左边位置
    public int prePageLeft = 0, currPageLeft = 0;

    //获取滑动速度
    private VelocityTracker vt;
    //当前的滑动速度
    private float speed;
    //防止抖动
    private float speed_shake;

    public int scrollerSpeed = 0;

    // 正在滑动的页面右边位置，用于绘制阴影
    public float right;

    private float lastX;

    // 手指滑动的距离
    public  float moveLength;

    /**
     * 过滤多点触碰的控制变量
     */
    private int mEvents;


    // 滑动的时候存在两页可滑动，要判断是哪一页在滑动
    private boolean isPreMoving = true, isCurrMoving = true;

    private PageFactory factory;

    public Scroller mScroller;

    private FlipStatus mFlipStatus;

    private BookStatus mBookStatus;

    private LoadService mLoadService;


    public HorizontalBaseReadView(Context context,LoadService loadService) {
        super(context);
        this.scrollerSpeed = 500;
        this.mLoadService = loadService;
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
//                .setComposingStrategy(new HorizontalComposing())
                .setOnReaderLoadingListener(new MyOnReaderLoadingListener())
                .builder();

        ViewConfiguration configuration = ViewConfiguration.get(context);
        speed_shake = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        initParams();

    }


    /**
     * 初始化参数
     */
    private void initParams(){
        mFlipStatus = FlipStatus.ON_FLIP_CUR;
        mBookStatus = BookStatus.LOAD_START;
        prePageLeft = -mScreenWidth;
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
                if(null != mLoadService){
                    mLoadService.showSuccess();
                }
                break;

            case LOAD_SUCCESS:
                if(null != mLoadService){
                    mLoadService.showSuccess();
                }
                factory.prePage(mPrePageCanvas);
                factory.curPage(mCurPageCanvas);
                factory.nextPage(mNextPageCanvas);
                state = STATE_STOP;
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
        public void onStartLoading() {
            if(null != mLoadService){
                mLoadService.showCallback(LoadingCallback.class);
            }
        }

        @Override
        public void onEndLoading() {
            if(null != mLoadService){
                mLoadService.showSuccess();
            }
        }


        @Override
        public void onDrawPositionPage(FlipStatus flipStatus) {
            state = STATE_STOP;
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
            }
            if(null != mLoadService){
                mLoadService.showSuccess();
            }
        }
    }



    /**
     * 手指抬起时页面滚动执行此方法。
     * @param status
     * @param curX
     */
    private void updatePageArea(FlipStatus status,int curX){

        if(state != STATE_MOVE){
            return;
        }

        //向左滑动
        if(prePageLeft > -mScreenWidth && speed <= 0){
            moveRight(status,curX);
        }
        else if(currPageLeft < 0 && speed >= 0){
            moveLeft(status,curX);
        }

        //手指向左滑动
        else if(speed < 0 ) {
            moveLeft(status, curX);
            if (currPageLeft == -mScreenWidth) {
                factory.autoIncrease();
                factory.prePage(mPrePageCanvas);
                factory.curPage(mCurPageCanvas);
                if(factory.nextPage(mNextPageCanvas)){
                    mBookStatus = BookStatus.LOAD_SUCCESS;
                }
                factory.nextPage(mNextPageCanvas);
                mFlipStatus = FlipStatus.ON_FLIP_NEXT;
            }
        }

        //手指向右滑动
        else if(speed > 0 ){
            moveRight(status,curX);
            if(prePageLeft == 0){
                factory.autoReduce();
                factory.nextPage(mNextPageCanvas);
                factory.curPage(mCurPageCanvas);
                if( factory.prePage(mPrePageCanvas)){
                    mBookStatus = BookStatus.LOAD_SUCCESS;
                }
                mFlipStatus = FlipStatus.ON_FLIP_PRE;
            }
        }
        if(right == 0 || right == mScreenWidth){
            state = STATE_STOP;
            prePageLeft = -mScreenWidth;
            currPageLeft = 0;
            releaseMoving();
            postInvalidate();
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
                if(state == STATE_MOVE){
                    return false;
                }
                lastX = event.getX();
                try
                {
                    if (vt == null)
                    {
                        vt = VelocityTracker.obtain();
                    } else
                    {
                        vt.clear();
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                vt.addMovement(event);
                mEvents = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                vt.addMovement(event);
                vt.computeCurrentVelocity(scrollerSpeed);
                speed = vt.getXVelocity();
                moveLength = event.getX() - lastX;
                //右滑
                if((moveLength > 0 || !isCurrMoving) && isPreMoving && mEvents == 0){
                    mFlipStatus = FlipStatus.ON_FLIP_PRE;
                    isPreMoving = true;
                    isCurrMoving = false;
                    if(mBookStatus == BookStatus.NO_PRE_PAGE){
                        state = STATE_STOP;
                        releaseMoving();
                    }else{
                        prePageLeft  += moveLength;
                        if(prePageLeft > 0){
                            prePageLeft = 0;
                        }else if(prePageLeft < -mScreenWidth){
                            prePageLeft = -mScreenWidth;
                            releaseMoving();
                        }
                        right = prePageLeft + mScreenWidth;
                        state = STATE_MOVE;
                    }

                }
                //左滑
                else if((moveLength < 0 || !isPreMoving) && isCurrMoving && mEvents == 0){
                    mFlipStatus = FlipStatus.ON_FLIP_NEXT;
                    isPreMoving = false;
                    isCurrMoving = true;
                    if(mBookStatus == BookStatus.NO_NEXT_PAGE){
                        state = STATE_STOP;
                        releaseMoving();
                    }else{
                        currPageLeft += moveLength;
                        if(currPageLeft < -mScreenWidth){
                            currPageLeft = -mScreenWidth;
                        }else if(currPageLeft > 0){
                            currPageLeft = 0;
                            releaseMoving();
                        }
                        right = mScreenWidth + currPageLeft;
                        state = STATE_MOVE;
                    }
                }else {
                    mEvents = 0;
                }
                lastX = event.getX();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(speed) < speed_shake)
                    speed = 0;
                startScroller(speed);
                if(getTouchLocal(event.getX(),event.getY()) == SETTING_AREA && Math.abs(speed) < 100 && state != STATE_MOVE){
                    new BookReadSettingDialog(getContext(), factory).show();
                    return true;
                }
                postInvalidate();
                try
                {
                    vt.clear();
                    vt.recycle();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

        }
        return true;
    }


    /**
     * 判断点击的区域
     *
     * @param x
     * @param y
     * @return
     */
    private int getTouchLocal(float x, float y) {
        if (x > mScreenWidth / 4 && x < mScreenWidth * 3 / 4
                && y > mScreenHeight / 4 && y < mScreenHeight * 3 / 4) {
            return SETTING_AREA;
        } else if (x > mScreenWidth / 2) {
            return NEXT_AREA;
        }
        return PREVIOUS_AREA;
    }

    /** 手指抬起 页面滚动状态设置
     * @param speed
     */
    private void startScroller(float speed){
        if(speed == 0)
        {
            if(isCurrMoving){
                if(right < mScreenWidth / 2){
                    //当前页向左滚动
                    mScroller.startScroll((int)right,0,-(int)right,scrollerSpeed);
                }else if(right > mScreenWidth / 2){
                    //当前页向右滚动
                    mScroller.startScroll((int)right,0,mScreenWidth - (int)right,scrollerSpeed);
                }
            }else if(isPreMoving){
                if(right < mScreenWidth / 2){
                    //当前页向右滚动
                    mScroller.startScroll((int)right,0, - (int)right,0,scrollerSpeed);
                }else if(right > mScreenWidth / 2){
                    //当前页向左滚动
                    mScroller.startScroll((int)right,0,mScreenWidth - (int)right,0,scrollerSpeed);
                }
            }
        }else if(speed < 0){
            //向左滑动
            mScroller.startScroll((int)right,0,-(int)right,0,scrollerSpeed);
        }else if(speed > 0){
            //向右滑动
            mScroller.startScroll((int)right,0,mScreenWidth - (int)right,0,scrollerSpeed);
        }

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



    /**
     * 释放动作，不限制手滑动方向
     */

    private void releaseMoving()
    {
        isPreMoving = true;
        isCurrMoving = true;
    }

    /**
     * 手指左滑坐标计算
     * @param status
     * @param curX
     */
    private void moveLeft(FlipStatus status,int curX){
        switch (status) {
            case ON_FLIP_PRE:
                prePageLeft = curX;
                if (curX < - mScreenWidth)
                    prePageLeft = - mScreenWidth;
                right = mScreenWidth + prePageLeft;
                break;

            case ON_FLIP_NEXT:
                currPageLeft = -(mScreenWidth - curX);
                if (currPageLeft < -mScreenWidth)
                    currPageLeft = -mScreenWidth;
                right = mScreenWidth + currPageLeft;
                break;
        }
    }

    /**
     * 手指右滑坐标计算
     * @param status
     * @param curX
     */
    private void moveRight(FlipStatus status,int curX){
        switch (status) {
            case ON_FLIP_PRE:
                prePageLeft = -(mScreenWidth - curX);
                if (prePageLeft > 0)
                    prePageLeft = 0;
                right = mScreenWidth + prePageLeft;
                break;
            case ON_FLIP_NEXT:
                currPageLeft = curX;
                if (currPageLeft > 0)
                    currPageLeft = 0;
                right = mScreenWidth + currPageLeft;
                break;
        }
    }


    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()) {
            updatePageArea(mFlipStatus, mScroller.getCurrX());
            postInvalidate();
}
    }

    @Override
    public void closeBook() {
        factory.closeBook();
    }
}



