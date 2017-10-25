package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.Scroller;
import android.widget.Toast;

import com.eschao.android.widget.pageflip.PageFlip;
import com.eschao.android.widget.pageflip.PageFlipException;


import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.utils.StringUtils;
import shuhai.readercore.view.readview.factory.Factory;
import shuhai.readercore.view.readview.factory.PageFactory;
import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.status.FlipStatus;


/**
 * @author 55345364
 * @date 2017/9/1.
 */

public abstract class GLHorizontalBaseReadView extends GLSurfaceView implements GLSurfaceView.Renderer, BaseReadViewImpl{

    int mPageNo;
    int mDuration;
    Handler mHandler;
    PageFlip mPageFlip;
    SinglePageRender mPageRender;
    ReentrantLock mDrawLock;

    private static final String TAG = "HorizontalBaseReadView";

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



    private int mPreChapterId,mCurChapterId,mNextChapterId;


    private int pageSize;
    private int pageCount;

    OnReadStateChangeListener listener;

    private FlipStatus mFlipStatus;

    private BookStatus mBookStatus;


    private Factory factory;


    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;



    protected int mScreenWidth;
    protected int mScreenHeight;


    private int mBookId;
    private int mChapterId;

    Scroller mScroller;

    public GLHorizontalBaseReadView(Context context,int bookId, int chapterId,OnReadStateChangeListener listener) {
        super(context);
        this.mBookId = bookId;
        this.mChapterId = chapterId;
        newHandler();
        mScroller = new Scroller(context);
        mDuration = 1000;
        this.listener = listener;
        // create PageFlip
        mPageFlip = new PageFlip(context);
        mPageFlip.setSemiPerimeterRatio(0.8f)
                .setShadowWidthOfFoldEdges(5, 60, 0.3f)
                .setShadowWidthOfFoldBase(5, 80, 0.4f)
                .setPixelsOfMesh(10)
                .enableAutoPage(true);
        setEGLContextClientVersion(2);

        // init others
        mPageNo = 3 ;
        mDrawLock = new ReentrantLock();
        mPageRender = new SinglePageRender(context, mPageFlip,mHandler, mPageNo);
        // configure render
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);


        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();


        mPrePageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);

        mPrePageCanvas =  new Canvas(mPrePageBitmap);
        mCurPageCanvas =  new Canvas(mCurPageBitmap);
        mNextPageCanvas =  new Canvas(mNextPageBitmap);


        factory = new PageFactory(context);
        ((PageFactory)factory).setChapterLoader();
        ((PageFactory)factory).setComposingStrategy();
        ((PageFactory) factory).setOnReadStateChangeListener(listener);

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


    public synchronized void closeBook(){

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                onFingerDown(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onFingerMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                onFingerUp(event.getX(), event.getY());
                mScroller.startScroll((int)event.getX(),0,mScreenWidth - (int)event.getX(),0,500);
                postInvalidate();
                break;
        }
        return true;
    }
    /**
     * Handle finger down event
     *
     * @param x finger x coordinate
     * @param y finger y coordinate
     */
    public void onFingerDown(float x, float y) {
        // if the animation is going, we should ignore this event to avoid
        // mess drawing on screen
        if (!mPageFlip.isAnimating() &&
                mPageFlip.getFirstPage() != null) {
            mPageFlip.onFingerDown(x, y);
        }
    }

    /**
     * Handle finger moving event
     *
     * @param x finger x coordinate
     * @param y finger y coordinate
     */
    public void onFingerMove(float x, float y) {
        if (mPageFlip.isAnimating()) {
            // nothing to do during animating
        }
        else if (mPageFlip.canAnimate(x, y)) {
            // if the point is out of current page, try to start animating
            onFingerUp(x, y);
        }
        // move page by finger
        else if (mPageFlip.onFingerMove(x, y)) {
            try {
                mDrawLock.lock();
                if (mPageRender != null && mPageRender.onFingerMove(x, y)) {
                    requestRender();
                }
            }
            finally {
                mDrawLock.unlock();
            }
        }
    }

    /**
     * Handle finger up event and start animating if need
     *
     * @param x finger x coordinate
     * @param y finger y coordinate
     */
    public void onFingerUp(float x, float y) {
        if (!mPageFlip.isAnimating()) {
            mPageFlip.onFingerUp(x, y, mDuration);
            try {
                mDrawLock.lock();
                if (mPageRender != null && mPageRender.onFingerUp(x, y)) {
                    requestRender();
                }
            }
            finally {
                mDrawLock.unlock();
            }
        }
    }

    /**
     * Draw frame
     *
     * @param gl OpenGL handle
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        try {
            mDrawLock.lock();
            if (mPageRender != null) {
                mPageRender.onDrawFrame(mCurPageBitmap);
            }
        }
        finally {
            mDrawLock.unlock();
        }
    }

    /**
     * Handle surface is changed
     *
     * @param gl OpenGL handle
     * @param width new width of surface
     * @param height new height of surface
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        try {
            mPageFlip.onSurfaceChanged(width, height);
            int pageNo = mPageRender.getPageNo();
            mPageRender.release();
            mPageRender = new SinglePageRender(getContext(),
                    mPageFlip,
                    mHandler,
                    pageNo);
            mPageRender.onSurfaceChanged(width, height);
        }
        catch (PageFlipException e) {
            Log.e(TAG, "Failed to run PageFlipFlipRender:onSurfaceChanged");
        }
    }

    /**
     * Handle surface is created
     *
     * @param gl OpenGL handle
     * @param config EGLConfig object
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            mPageFlip.onSurfaceCreated();
        }
        catch (PageFlipException e) {
            Log.e(TAG, "Failed to run PageFlipFlipRender:onSurfaceCreated");
        }
    }

    /**
     * Create message handler to cope with messages from page render,
     * Page render will send message in GL thread, but we want to handle those
     * messages in main thread that why we need handler here
     */
    private void newHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SinglePageRender.MSG_ENDED_DRAWING_FRAME:
                        try {
                            mDrawLock.lock();
                            if (mPageRender != null && mPageRender.onEndedDrawing(msg.arg1)) {
                                requestRender();
                            }
                        }
                        finally {
                            mDrawLock.unlock();
                        }
                        break;

                    default:
                        break;
                }
            }
        };
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            Log.e(TAG, "computeScroll: ----------------------------->>" + mScroller.getCurrX() );
            onFingerMove(mScroller.getCurrX(),mScroller.getCurrY());
            updatePageArea(mScroller.getCurrX());
            postInvalidate();
        }
    }


    /**
     * 手指抬起时页面滚动执行此方法。
     * @param curX
     */
    private void updatePageArea(int curX){

        if(curX == mScreenWidth || curX == -mScreenWidth){

            pageSize++;
            if(pageSize == pageCount){
                postDrawView(mCurChapterId,pageSize,mCurPageCanvas);
                postDrawView(mCurChapterId,pageSize - 1,mPrePageCanvas);
                factory.nextChapter();
            } else if(pageSize > pageCount){
                postDrawView(mCurChapterId,pageSize - 1,mPrePageCanvas);
                pageSize = 1;
                pageCount = factory.getCountPage();
                postDrawView(mNextChapterId,pageSize,mCurPageCanvas);
                postDrawView(mNextChapterId,pageSize + 1,mNextPageCanvas);
                mPreChapterId = mCurChapterId;
                mCurChapterId = mNextChapterId;
            }else if(pageSize > 1 && pageSize < pageCount){
                 postInvalidateView(mCurChapterId,pageSize);
            }

        }


//        //手指向左滑动
//         if(speed < 0 && pageSize <= factory.getCountPage()) {
//            if (currPageLeft == -mScreenWidth) {
//                // 向后翻页动作完成，重新绘制显示内容。
//                pageSize++;
//                if(pageSize == pageCount){
//                    postDrawView(mCurChapterId,pageSize,mCurPageCanvas);
//                    postDrawView(mCurChapterId,pageSize - 1,mPrePageCanvas);
//                    factory.nextChapter();
//                } else if(mFlipStatus == FlipStatus.ON_FLIP_NEXT && pageSize > pageCount){
//                    postDrawView(mCurChapterId,pageSize - 1,mPrePageCanvas);
//                    pageSize = 1;
//                    pageCount = factory.getCountPage();
//                    postDrawView(mNextChapterId,pageSize,mCurPageCanvas);
//                    postDrawView(mNextChapterId,pageSize + 1,mNextPageCanvas);
//                    mPreChapterId = mCurChapterId;
//                    mCurChapterId = mNextChapterId;
//                }else if(pageSize > 1 && pageSize < pageCount){
//                    postInvalidateView(mCurChapterId,pageSize);
//                }
//            }
//        }
//
//        //手指向右滑动
//        else if(speed > 0 && pageSize >= 1){
//            if(prePageLeft == 0){
//                // 向前翻页动作完成，重新绘制显示内容。
//                pageSize--;
//                if(pageSize == 1){
//                    //翻页到当前章节的第一页，重新绘制当前页和下一页，预加载上一章的最后一页再绘制
//                    postDrawView(mCurChapterId,pageSize,mCurPageCanvas);
//                    postDrawView(mCurChapterId,pageSize + 1,mNextPageCanvas);
//                    factory.preChapter();
//                    mBookStatus = BookStatus.PRE_CHAPTER_LOAD_SUCCESS;
//                }else if(mFlipStatus == FlipStatus.ON_FLIP_PRE && pageSize < 1){
//                    //完成章节跳转，绘制
//                    postDrawView(mCurChapterId,1,mNextPageCanvas);
//                    pageSize = factory.getCountPage();
//                    pageCount = factory.getCountPage();
//                    postDrawView(mPreChapterId,pageSize,mCurPageCanvas);
//                    postDrawView(mPreChapterId,pageSize - 1,mPrePageCanvas);
//                    mNextChapterId =  mCurChapterId;
//                    mCurChapterId = mPreChapterId;
//                }else if(pageSize > 1 && pageSize < pageCount){
//                    postInvalidateView(mCurChapterId,pageSize);
//                }
//            }
//        }
//        if(right == 0 || right == mScreenWidth){
//            state = STATE_STOP;
//            prePageLeft = -mScreenWidth;
//            currPageLeft = 0;
//        }
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
            factory.getPageContent(chapterId,pageSizes[i], StringUtils.cacheKeyCreate(mBookId,chapterId));
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
