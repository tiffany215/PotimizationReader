package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.eschao.android.widget.pageflip.Page;
import com.eschao.android.widget.pageflip.PageFlip;
import com.eschao.android.widget.pageflip.PageFlipException;
import com.eschao.android.widget.pageflip.PageFlipState;

import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.factory.Factory;


/**
 * @author 55345364
 * @date 2017/9/1.
 */

public abstract class GLHorizontalBaseReadView extends GLSurfaceView implements GLSurfaceView.Renderer, BaseReadViewImpl{


    public final static int MSG_ENDED_DRAWING_FRAME = 1;
    private final static String TAG = "PageRender";

    final static int DRAW_MOVING_FRAME = 0;
    final static int DRAW_ANIMATING_FRAME = 1;
    final static int DRAW_FULL_PAGE = 2;

    final static int MAX_PAGES = 30;

    int mPageNo;
    int mDrawCommand;

    private Factory factory;


    public PageFlip mPageFlip;
    PageRender mPageRender;
    public ReentrantLock mDrawLock;
    Handler mHandler;

    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;



    protected int mScreenWidth;
    protected int mScreenHeight;

    public boolean isPrepare;

    private int mBookId;
    private int mChapterId;




    public GLHorizontalBaseReadView(Context context, int bookId, int chapterId) {
        super(context,null);

        newHandler();
        this.mBookId = bookId;
        this.mChapterId = chapterId;

        mPageFlip = new PageFlip(context);
        mPageFlip.setSemiPerimeterRatio(0.8f)
                .setShadowWidthOfFoldEdges(5, 60, 0.3f)
                .setShadowWidthOfFoldBase(5, 80, 0.4f)
                .setPixelsOfMesh(1000)
                .enableAutoPage(true);
        setEGLContextClientVersion(2);

        mPageNo = 1;
        mDrawLock = new ReentrantLock();
        mPageRender = new SinglePageRender(context, mPageFlip, mHandler, mPageNo);
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


    }

    public synchronized void init(int theme){
        if(!isPrepare){
            factory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
            int ret =  factory.openBook(mBookId,mChapterId,1);
            if(ret == 0){
                Toast.makeText(getContext(),"章节内容打开失败！",Toast.LENGTH_LONG).show();
                return;
            }

            factory.onDraw(mCurPageCanvas);
            isPrepare = true;

            postInvalidate();
        }
    }


    public float actiondownX,actiondownY;
    protected PointF mTouch = new PointF();
    private int dx;
    private int dy;
    private boolean center;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                dx = (int) event.getX();
                dy = (int) event.getY();

                mTouch.x = dx;
                mTouch.y = dy;

                actiondownX = dx;
                actiondownY = dy;

                if(actiondownX >= mScreenWidth / 3 && actiondownX <= mScreenWidth * 2 / 3
                        && actiondownY >= mScreenHeight / 3 && actiondownY <= mScreenHeight * 2 / 3 ){
                    center = true;
                }else{
                    center = false;
                    //条件成立为右翻
                    if(actiondownX < mScreenWidth / 2){
                        BookStatus bookStatus = factory.prePage();
                        if(bookStatus == BookStatus.NO_PRE_PAGE){
                            Toast.makeText(getContext(),"没有上一页了",Toast.LENGTH_SHORT).show();
                            return false;
                        }else if(bookStatus == BookStatus.LOAD_SUCCESS){
                            abortAnimation();
                            factory.onDraw(mPrePageCanvas);
                        }else{
                            return false;
                        }
                    }else{
                        BookStatus bookStatus = factory.nextPage();
                        if(bookStatus == BookStatus.NO_NEXT_PAGE){
                            Toast.makeText(getContext(),"没有下一页了",Toast.LENGTH_SHORT).show();
                            return false;
                        }else if(bookStatus == BookStatus.LOAD_SUCCESS){
                            abortAnimation();
                            factory.onDraw(mNextPageCanvas);
                        }else{
                            return false;
                        }
                    }
                    requestRender();
                }




                onFingerDown(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onFingerMove(event.getX(),event.getY());
                requestRender();
                break;
            case MotionEvent.ACTION_CANCEL:
                onFingerUp(event.getX(),event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }


    public  void onFingerMove(float x, float y){
        if (mPageFlip.isAnimating()) {
            // 动画无关
        }
        else if (mPageFlip.canAnimate(x, y)) {
            // 如果点超出当前页面，尝试开始动画
            onFingerUp(x, y);
        }
        // move page by finger
        else if (mPageFlip.onFingerMove(x, y)) {
            try {
                mDrawLock.lock();
                if (this.isFingerMove(x, y)) {
                    requestRender();
                }
            }
            finally {
                mDrawLock.unlock();
            }
        }
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        try {
            mPageFlip.onSurfaceCreated();
        }
        catch (PageFlipException e) {
            Log.e(TAG, "Failed to run PageFlipFlipRender:onSurfaceCreated");
        }
    }


    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        try {
            mPageFlip.onSurfaceChanged(width, height);
            // if there is the second page, create double page render when need
            int pageNo = mPageRender.getPageNo();
           if(!(mPageRender instanceof SinglePageRender)) {
                mPageRender.release();
                mPageRender = new SinglePageRender(getContext(),
                        mPageFlip,
                        mHandler,
                        pageNo);
            }

            // let page render handle surface change
            mPageRender.onSurfaceChanged(width, height);
        }
        catch (PageFlipException e) {
            Log.e(TAG, "Failed to run PageFlipFlipRender:onSurfaceChanged");
        }
    }

    /**
     * onDrawFrame是绘制每一帧的方法
     * @param gl10
     */
    @Override
    public void onDrawFrame(GL10 gl10) {
        try {
            mDrawLock.lock();
            if (mPageRender != null) {
                mPageFlip.deleteUnusedTextures();
                Page page = mPageFlip.getFirstPage();

                // 2. 从手指移动和动画触发的处理绘图命令
                if (mDrawCommand == DRAW_MOVING_FRAME ||
                        mDrawCommand == DRAW_ANIMATING_FRAME) {
                    // 正向翻转
                    if (mPageFlip.getFlipState() == PageFlipState.FORWARD_FLIP) {
                        //检查第一页的第二个纹理是否有效，如果没有，创建新的。
                        if (!page.isSecondTextureSet()) {
                            drawPage(mPrePageCanvas,mPrePageBitmap);
                            page.setSecondTexture(mPrePageBitmap);
                        }
                    }
                    // 在向后翻页，第一页的第一个纹理检查是有效的
                    else if (!page.isFirstTextureSet()) {
                        drawPage(mNextPageCanvas,mNextPageBitmap);
                        page.setFirstTexture(mNextPageBitmap);
                    }

                    // 画框翻页
                    mPageFlip.drawFlipFrame();
                }
                // 画平静的页面而不翻转
                else if (mDrawCommand == DRAW_FULL_PAGE) {
                    if (!page.isFirstTextureSet()) {


                        drawPage(mCurPageCanvas,mCurPageBitmap);
                        page.setFirstTexture(mCurPageBitmap);


                    }
                    mPageFlip.drawPageFrame();
                }

                /**
                 * 3.发送消息到主线程通知绘图结束如果需要，
                 * 我们可以继续计算下一个动画帧。
                 * 记住：绘图操作始终在GL线程中，而不是主线程
                 */
                Message msg = Message.obtain();
                msg.what = MSG_ENDED_DRAWING_FRAME;
                msg.arg1 = mDrawCommand;
                mHandler.sendMessage(msg);
            }
        }
        finally {
            mDrawLock.unlock();
        }
    }


    public  void onFingerUp(float x, float y){
        if (!mPageFlip.isAnimating()) {
            mPageFlip.onFingerUp(x, y, 1000);
            try {
                mDrawLock.lock();
                if (this.isFingerUp(x, y)) {
                    requestRender();
                }
            }
            finally {
                mDrawLock.unlock();
            }
        }
    };

    public  void onFingerDown(float x, float y){
        if (!mPageFlip.isAnimating() && mPageFlip.getFirstPage() != null) {
            mPageFlip.onFingerDown(x, y);
        }
    };


    public boolean isFingerUp(float x, float y) {
        if (mPageFlip.animating()) {
            mDrawCommand = DRAW_ANIMATING_FRAME;
            return true;
        }

        return false;
    }

    public boolean isFingerMove(float x, float y) {
        mDrawCommand = DRAW_MOVING_FRAME;
        return true;
    }


    /**
     * 创建消息处理程序来处理来自页面呈现的消息，
     * 页面渲染将在GL线程中发送消息，但是我们要处理这些消息
     * 主线程中的消息，为什么我们需要处理程序
     */
    private void newHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PageRender.MSG_ENDED_DRAWING_FRAME:
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


    public abstract void drawPage(Canvas canvas,Bitmap bitmap);

    protected abstract void drawPrePageArea(Canvas canvas);

    protected abstract void drawCurPageArea(Canvas canvas);

    protected abstract void drawNextPageArea(Canvas canvas);






}
