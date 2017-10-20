package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
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
import shuhai.readercore.view.readview.factory.Factory;
import shuhai.readercore.view.readview.factory.PageFactory;
import shuhai.readercore.view.readview.FlipStatus;


/**
 * @author 55345364
 * @date 2017/9/1.
 */

public abstract class GLHorizontalBaseReadView extends GLSurfaceView implements GLSurfaceView.Renderer, BaseReadViewImpl{

    private final static String TAG = "PageFlipView";

    int mPageNo;
    int mDuration;
    Handler mHandler;
    PageFlip mPageFlip;
    SinglePageRender mPageRender;
    ReentrantLock mDrawLock;




    private Factory factory;


    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;



    protected int mScreenWidth;
    protected int mScreenHeight;

    public boolean isPrepare;

    private int mBookId;
    private int mChapterId;

    Scroller mScroller;

    public GLHorizontalBaseReadView(Context context,int bookId, int chapterId) {
        super(context);
        this.mBookId = bookId;
        this.mChapterId = chapterId;
        // create handler to tackle message
        newHandler();

        mScroller = new Scroller(context);
        // load preferences
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        mDuration = 1000;
//        int pixelsOfMesh = pref.getInt(Constants.PREF_MESH_PIXELS, 10);
//        boolean isAuto = pref.getBoolean(Constants.PREF_PAGE_MODE, true);

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
    }


    public synchronized void init(int theme){
        if(!isPrepare){
            factory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
            openBook(mBookId,mChapterId,UserSP.getInstance().getLastReaderChapterOrder(mBookId),FlipStatus.ON_FLIP_CUR);
        }
    }

    public synchronized void openBook(int articleId,int chapterId,int chapterOrder,FlipStatus status){
        factory.openBook(mBookId,mChapterId, UserSP.getInstance().getLastReaderChapterOrder(mBookId),status);
//        if(ret == 0){
//            Toast.makeText(getContext(),"章节内容打开失败！",Toast.LENGTH_LONG).show();
//            return;
//        }

        factory.onDraw(mCurPageCanvas);
        isPrepare = true;
        requestRender();
    }


    public synchronized void closeBook(){

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:


                onFingerDown(event.getX(),event.getY());

//                dx = (int) event.getX();
//                dy = (int) event.getY();
//
//                mTouch.x = dx;
//                mTouch.y = dy;
//
//                actiondownX = dx;
//                actiondownY = dy;
//
//                if(actiondownX >= mScreenWidth / 3 && actiondownX <= mScreenWidth * 2 / 3
//                        && actiondownY >= mScreenHeight / 3 && actiondownY <= mScreenHeight * 2 / 3 ){
//                    center = true;
//                }else{
//                    center = false;
//                    //条件成立为右翻
//                    if(actiondownX < mScreenWidth / 2){
//                        BookStatus bookStatus = factory.prePage();
//                        if(bookStatus == BookStatus.NO_PRE_PAGE){
//                            Toast.makeText(getContext(),"没有上一页了",Toast.LENGTH_SHORT).show();
//                            return false;
//                        }else if(bookStatus == BookStatus.LOAD_SUCCESS){
//                            abortAnimation();
//                            factory.onDraw(mPrePageCanvas);
//                        }else{
//                            return false;
//                        }
//                    }else{
//                        BookStatus bookStatus = factory.nextPage();
//                        if(bookStatus == BookStatus.NO_NEXT_PAGE){
//                            Toast.makeText(getContext(),"没有下一页了",Toast.LENGTH_SHORT).show();
//                            return false;
//                        }else if(bookStatus == BookStatus.LOAD_SUCCESS){
//                            abortAnimation();
//                            factory.onDraw(mNextPageCanvas);
//                        }else{
//                            return false;
//                        }
//                    }
//                    postInvalidate();
//                }
                break;
            case MotionEvent.ACTION_MOVE:
//                if(center){
//                    break;
//                }
//                int mx = (int) event.getX();
//                int my = (int) event.getY();
//
//                mTouch.x = mx;
//                mTouch.y = my;
//
//                this.requestLayout();
                onFingerMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:


                onFingerUp(event.getX(), event.getY());
//                if(center){
//
//
//
//                    break;
//                }

                mScroller.startScroll((int)event.getX(),0,mScreenWidth - (int)event.getX(),0,700);
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

            // if there is the second page, create double page render when need
            int pageNo = mPageRender.getPageNo();
//            if (mPageFlip.getSecondPage() != null && width > height) {
//                if (!(mPageRender instanceof DoublePagesRender)) {
//                    mPageRender.release();
//                    mPageRender = new DoublePagesRender(getContext(),
//                            mPageFlip,
//                            mHandler,
//                            pageNo);
//                }
//            }
            // if there is only one page, create single page render when need
//            else if(!(mPageRender instanceof SinglePageRender)) {
            mPageRender.release();
            mPageRender = new SinglePageRender(getContext(),
                    mPageFlip,
                    mHandler,
                    pageNo);
//            }

            // let page render handle surface change
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
                            // notify page render to handle ended drawing
                            // message
                            if (mPageRender != null &&
                                    mPageRender.onEndedDrawing(msg.arg1)) {
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
            onFingerMove(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }


    }
}
