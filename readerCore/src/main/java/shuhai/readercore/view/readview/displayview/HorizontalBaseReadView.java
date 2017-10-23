package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import android.widget.Toast;

import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.utils.StringUtils;
import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.factory.Factory;
import shuhai.readercore.view.readview.factory.PageFactory;
import shuhai.readercore.view.readview.FlipStatus;

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


    // 前一页，当前页，下一页的左边位置
    public int prePageLeft = 0, currPageLeft = 0;

    //获取滑动速度
    private VelocityTracker vt;
    //当前的滑动速度
    private float speed;
    //防止抖动
    private float speed_shake;
    // 正在滑动的页面右边位置，用于绘制阴影
    public float right;

    private float lastX;


    // 滑动的时候存在两页可滑动，要判断是哪一页在滑动
    private boolean isPreMoving = true, isCurrMoving = true;

    private Factory factory;

    public Scroller mScroller;

//    public boolean isPrepare;

    private int mBookId;
    private int mPreChapterId,mCurChapterId,mNextChapterId;


    private int pageSize;
    private int pageCount;

    OnReadStateChangeListener listener;

    private FlipStatus mFlipStatus;

    private BookStatus mBookStatus;



    public HorizontalBaseReadView(Context context,int bookId,int chapterId,OnReadStateChangeListener listener) {
        super(context);

        this.mBookId = bookId;
        this.mCurChapterId = chapterId;
        this.listener = listener;

        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();

        mPrePageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);

        mPrePageCanvas =  new Canvas(mPrePageBitmap);
        mCurPageCanvas =  new Canvas(mCurPageBitmap);
        mNextPageCanvas =  new Canvas(mNextPageBitmap);

        mScroller = new Scroller(getContext());

        factory = new PageFactory(context);
        ((PageFactory)factory).setChapterLoader();
        ((PageFactory)factory).setComposingStrategy();
        ((PageFactory) factory).setOnReadStateChangeListener(listener);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        speed_shake = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        initParams();

    }


    private void initParams(){
        mFlipStatus = FlipStatus.ON_FLIP_CUR;
        mBookStatus = BookStatus.START_LOAD_SUCCESS;
        prePageLeft = -mScreenWidth;
        currPageLeft = 0;
        pageSize = UserSP.getInstance().getLastReaderPage(mBookId);


    }


    public synchronized void init(int theme){
            factory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
    }


    public synchronized void openBook(int articleId,int chapterId,int chapterOrder,FlipStatus status){
        mBookStatus = factory.openBook(articleId,chapterId,chapterOrder,status);
        if(mBookStatus == BookStatus.CUR_CHAPTER_LOAD_FAILURE){
            Toast.makeText(getContext(),"章节内容打开失败！",Toast.LENGTH_LONG).show();
            return;
        }
        switch (mBookStatus) {
            //第一次打开书籍
            case CUR_CHAPTER_LOAD_SUCCESS:
                mCurChapterId = chapterId;
                //如果当前页等于章节第一页,绘制第一页内容和上一章最后一页内容
                //如果当前页等于章节最后一页，绘制最后一页内容和下一章第一页内容
                pageCount = factory.getCountPage();
                if(pageSize == 1){
                    postDrawView(mCurChapterId,pageSize,mCurPageCanvas);
                    postDrawView(mCurChapterId,pageSize + 1,mNextPageCanvas);
                }else if(pageSize == factory.getCountPage()){
                    postDrawView(mCurChapterId,pageSize - 1,mPrePageCanvas);
                    postDrawView(mCurChapterId,pageSize,mCurPageCanvas);
                }else if(pageSize > 1 && pageSize < factory.getCountPage()){
                    postInvalidateView(mCurChapterId,pageSize);
                }
                break;
            case PRE_CHAPTER_LOAD_SUCCESS:
                mPreChapterId = chapterId;
                postDrawView(mPreChapterId,factory.getCountPage(),mPrePageCanvas);
                break;
            //下一章内容加载成功后
            case NEXT_CHAPTER_LOAD_SUCCESS:
                mNextChapterId = chapterId;
                postDrawView(mNextChapterId,1,mNextPageCanvas);
                break;
        }

        state = STATE_STOP;
        mFlipStatus = status;
        postInvalidate();
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
            moveLeft(status,curX);
        }
        else if(currPageLeft < 0 && speed >= 0){
            moveRight(status,curX);
        }

        //手指向左滑动
        else if(speed < 0 && pageSize <= factory.getCountPage()) {
            moveLeft(status, curX);
            if (currPageLeft == -mScreenWidth) {
                // 向后翻页动作完成，重新绘制显示内容。
                pageSize++;
                if(pageSize == pageCount){
                    Log.e(TAG, mCurChapterId + "-------------------postInvalidateView------1--------------------->" );
                    postDrawView(mCurChapterId,pageSize,mCurPageCanvas);
                    postDrawView(mCurChapterId,pageSize - 1,mPrePageCanvas);
                    factory.nextChapter();

                    Log.e(TAG, "-------当前章---------------->>" + mCurChapterId  +"-------------当前页--------->>"+ pageSize);
                } else if(mFlipStatus == FlipStatus.ON_FLIP_NEXT && pageSize > pageCount){
                    Log.e(TAG, mCurChapterId + "-------------------postInvalidateView--------2------------------->" );
                    if(mBookStatus == BookStatus.NEXT_CHAPTER_LOAD_SUCCESS){
                        postDrawView(mCurChapterId,pageSize - 1,mPrePageCanvas);
                        pageSize = 1;
                        pageCount = factory.getCountPage();
                        postDrawView(mNextChapterId,pageSize,mCurPageCanvas);
                        postDrawView(mNextChapterId,pageSize + 1,mNextPageCanvas);
                        mPreChapterId = mCurChapterId;
                        mCurChapterId = mNextChapterId;
                        Log.e(TAG, "-------当前章---------------->>" + mCurChapterId  +"-------------当前页--------->>"+ pageSize);
                    }else if(mBookStatus == BookStatus.NEXT_CHAPTER_LOAD_FAILURE){

                    }
                }else if(pageSize > 1 && pageSize < pageCount){
                    Log.e(TAG, mCurChapterId + "-------------------postInvalidateView---------3------------------>" );
                    Log.e(TAG, "-------当前章---------------->>" + mCurChapterId  +"-------------当前页--------->>"+ pageSize);
                    postInvalidateView(mCurChapterId,pageSize);
                }

            }
        }

        //手指向右滑动
        else if(speed > 0 && pageSize >= 1){
            moveRight(status,curX);
            if(prePageLeft == 0){
                // 向前翻页动作完成，重新绘制显示内容。
                pageSize--;
                if(pageSize == 1){
                    //翻页到当前章节的第一页，重新绘制当前页和下一页，预加载上一章的最后一页再绘制
                    postDrawView(mCurChapterId,pageSize,mCurPageCanvas);
                    postDrawView(mCurChapterId,pageSize + 1,mNextPageCanvas);
                    factory.preChapter();
                    mBookStatus = BookStatus.PRE_CHAPTER_LOAD_SUCCESS;
                    Log.e(TAG, "-------当前章---------------->>" + mCurChapterId  +"-------------当前页--------->>"+ pageSize);
                }else if(mFlipStatus == FlipStatus.ON_FLIP_PRE && pageSize < 1){

                    //完成章节跳转，绘制
                    postDrawView(mCurChapterId,1,mNextPageCanvas);
                    pageSize = factory.getCountPage();
                    pageCount = factory.getCountPage();
                    postDrawView(mPreChapterId,pageSize,mCurPageCanvas);
                    postDrawView(mPreChapterId,pageSize - 1,mPrePageCanvas);

                    mNextChapterId =  mCurChapterId;
                    mCurChapterId = mPreChapterId;

                    Log.e(TAG, "-------当前章---------------->>" + mCurChapterId  +"-------------当前页--------->>"+ pageSize);
                }else if(pageSize > 1 && pageSize < pageCount){
                    postInvalidateView(mCurChapterId,pageSize);
                    Log.e(TAG, "-------当前章---------------->>" + mCurChapterId  +"-------------当前页--------->>"+ pageSize);

                }
            }
        }

        if(right == 0 || right == mScreenWidth){
            state = STATE_STOP;
            prePageLeft = -mScreenWidth;
            currPageLeft = 0;
            releaseMoving();
        }
    }


    public void closeBook(){
        UserSP.getInstance().setLastReaderPage(mBookId,pageSize);
    }


    // 手指滑动的距离
    public  float moveLength;

    /**
     * 过滤多点触碰的控制变量
     */
    private int mEvents;



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
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
                vt.computeCurrentVelocity(500);
                speed = vt.getXVelocity();
                moveLength = event.getX() - lastX;
                //右滑
                if((moveLength > 0 || !isCurrMoving) && isPreMoving && mEvents == 0){
                    mFlipStatus = FlipStatus.ON_FLIP_PRE;
                    isPreMoving = true;
                    isCurrMoving = false;
                    if(pageSize < 1){
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
                    if(pageSize > factory.getCountPage()){
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
                if(speed < 0){
                    mScroller.startScroll(currPageLeft,0,-mScreenWidth - currPageLeft,0,500);
                }else{
                    mScroller.startScroll(mScreenWidth  + prePageLeft ,0, Math.abs(mScreenWidth - (mScreenWidth  + prePageLeft)),0,500);
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
     *
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
                currPageLeft = curX;
                if (curX < -mScreenWidth)
                    currPageLeft = -mScreenWidth;
                    right = mScreenWidth + currPageLeft;
                break;
        }
    }

    /**
     *
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


    /**
     * 刷新显示页面
     * @param chapterId
     * @param pageSize
     */
    private void postInvalidateView(final int chapterId,int pageSize){
        Canvas[] canvases = {mPrePageCanvas,mCurPageCanvas,mNextPageCanvas};
        int[] pageSizes = {pageSize - 1,pageSize,pageSize + 1};
        for (int i = 0; i < canvases.length; i++) {
            factory.getPageContent(chapterId,pageSizes[i],StringUtils.cacheKeyCreate(mBookId,chapterId));
            factory.setPageSize(pageSizes[i]);
            factory.onDraw(canvases[i]);
        }
    }


    /**
     * 绘制指定页面
     * @param pageSize
     * @param canvas
     */
    private void postDrawView(int chapterId,int pageSize,Canvas canvas){
        factory.getPageContent(chapterId,pageSize,StringUtils.cacheKeyCreate(mBookId,chapterId));
        factory.setPageSize(pageSize);
        factory.onDraw(canvas);
    }





}



