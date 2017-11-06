package shuhai.readercore.view.readview.displayview;

import android.annotation.SuppressLint;
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

import com.eschao.android.widget.pageflip.OnPageFlipListener;
import com.eschao.android.widget.pageflip.Page;
import com.eschao.android.widget.pageflip.PageFlip;
import com.eschao.android.widget.pageflip.PageFlipException;
import com.eschao.android.widget.pageflip.PageFlipState;

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

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


/**
 * @author 55345364
 * @date 2017/9/1.
 */

@SuppressLint("NewApi")
public abstract class GLHorizontalBaseReadView extends GLSurfaceView implements GLSurfaceView.Renderer, BaseReadViewImpl,OnPageFlipListener{

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private PageFlip mPageFlip;
    private ReentrantLock mDrawLock;
    final static int MAX_PAGES = 30;
    private FlipStatus mFlipStatus;
    private BookStatus mBookStatus;
    private int pageSize;
    private int pageCount;

    private Handler mHandler;

    public final static int MSG_ENDED_DRAWING_FRAME = 1;

    final static int DRAW_MOVING_FRAME = 0;
    final static int DRAW_ANIMATING_FRAME = 1;
    final static int DRAW_FULL_PAGE = 2;

    int mDrawCommand;
    private int mBookId;
    private int mChapterId;

    private Factory factory;

    public GLHorizontalBaseReadView(Context context,int bookId, int chapterId,OnReadStateChangeListener listener) {
        super(context);
        this.mBookId = bookId;
        this.mChapterId = chapterId;


        newHandler();

        mPageFlip = new PageFlip(context);
        mPageFlip.setSemiPerimeterRatio(0.8f)
                .setShadowWidthOfFoldEdges(5, 60, 0.3f)
                .setShadowWidthOfFoldBase(5, 80, 0.4f)
                .setPixelsOfMesh(10)
                .enableAutoPage(true);
        setEGLContextClientVersion(2);
        mPageFlip.setListener(this);




        mDrawLock = new ReentrantLock();
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mBitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mDrawCommand = DRAW_FULL_PAGE;

        factory = new PageFactory(context);
        ((PageFactory)factory).setChapterLoader();
        ((PageFactory)factory).setComposingStrategy();
        ((PageFactory) factory).setOnReadStateChangeListener(listener);

        initParams();
    }

    private void initParams(){
        mFlipStatus = FlipStatus.ON_FLIP_CUR;
        mBookStatus = BookStatus.START_LOAD_SUCCESS;
        pageSize = UserSP.getInstance().getLastReaderPage(mBookId);
    }

    @Override
    public void init(int theme) {
        factory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
    }

    @Override
    public synchronized void openBook(int articleId, int chapterId, int chapterOrder, FlipStatus status) {
        mBookStatus = factory.openBook(articleId,chapterId,chapterOrder,status);
        if(mBookStatus == BookStatus.CUR_CHAPTER_LOAD_FAILURE){
            Toast.makeText(getContext(),"章节内容打开失败！",Toast.LENGTH_LONG).show();
            return;
        }

        switch (mBookStatus) {
            //第一次打开书籍
            case CUR_CHAPTER_LOAD_SUCCESS:
                //如果当前页等于章节第一页,绘制第一页内容和上一章最后一页内容
                //如果当前页等于章节最后一页，绘制最后一页内容和下一章第一页内容
                pageCount = factory.getCountPage();
                drawPage(mChapterId,pageSize);
                break;
            case PRE_CHAPTER_LOAD_SUCCESS:
                drawPage(mChapterId,pageSize);
                break;
            //下一章内容加载成功后
            case NEXT_CHAPTER_LOAD_SUCCESS:
                drawPage(mChapterId,pageSize);
                break;
        }
        mFlipStatus = status;
        requestRender();
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
                break;
        }
        return true;
    }




    /**
     * Draw frame
     */
    public void onDrawFrame() {
        // 1. delete unused textures
        mPageFlip.deleteUnusedTextures();
        Page page = mPageFlip.getFirstPage();

        // 2. 手指移动和动画触发的处理绘图命令
        if (mDrawCommand == DRAW_MOVING_FRAME ||  mDrawCommand == DRAW_ANIMATING_FRAME) {
            // 正向翻转
            if (mPageFlip.getFlipState() == PageFlipState.FORWARD_FLIP) {
                // check if second texture of first page is valid, if not,
                // create new one
                //检查第一页的第二个纹理是否有效，如果没有，
                //创建新的
                if (!page.isSecondTextureSet()) {
                    drawPage(mChapterId,pageSize ++);
                    page.setSecondTexture(mBitmap);
                }
            }
            // 在向后翻页，第一页的第一个纹理检查是有效的
            else if (!page.isFirstTextureSet()) {
                drawPage(mChapterId,pageSize --);
                page.setFirstTexture(mBitmap);
            }

            // draw frame for page flip
            // 画框翻页
            mPageFlip.drawFlipFrame();
        }
        // 画平静的页面而不翻转
        else if (mDrawCommand == DRAW_FULL_PAGE) {
            if (!page.isFirstTextureSet()) {
                drawPage(mChapterId,pageSize);
                page.setFirstTexture(mBitmap);
            }
            mPageFlip.drawPageFrame();
        }

        // 3.发送消息给主线程通知绘图结束
        //如果需要，我们可以继续计算下一个动画帧。
        //记住：绘图操作始终在GL线程中，而不是
        // 主线程
        Message msg = Message.obtain();
        msg.what = MSG_ENDED_DRAWING_FRAME;
        msg.arg1 = mDrawCommand;
        mHandler.sendMessage(msg);
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
                    case MSG_ENDED_DRAWING_FRAME:
                        try {
                            mDrawLock.lock();
                            // notify page render to handle ended drawing
                            // message
                            if (onEndedDrawing(msg.arg1)) {
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




    /**
     * Handle ended drawing event
     * In here, we only tackle the animation drawing event, If we need to
     * continue requesting render, please return true. Remember this function
     * will be called in main thread
     *
     * @param what event type
     * @return ture if need render again
     */
    public boolean onEndedDrawing(int what) {

//        Log.e("dddd", "drawPage: " + "------wwww-------->");

        if (what == DRAW_ANIMATING_FRAME) {
            boolean isAnimating = mPageFlip.animating();
            // continue animating
            if (isAnimating) {
                mDrawCommand = DRAW_ANIMATING_FRAME;
                return true;
            }
            // animation is finished
            else {
                final PageFlipState state = mPageFlip.getFlipState();
                // update page number for backward flip
                if (state == PageFlipState.END_WITH_BACKWARD) {
                    // don't do anything on page number since mPageNo is always
                    // represents the FIRST_TEXTURE no;
                }
                // update page number and switch textures for forward flip
                else if (state == PageFlipState.END_WITH_FORWARD) {
                    mPageFlip.getFirstPage().setFirstTextureWithSecond();
//                    mPageNo++;
                }

                mDrawCommand = DRAW_FULL_PAGE;
                return true;
            }
        }
        return false;
    }




    private void drawPage(int chapterId,int pageSize) {
        factory.getPageContent(chapterId,pageSize, StringUtils.cacheKeyCreate(mBookId,chapterId));
        factory.setPageSize(pageSize);
        factory.onDraw(mCanvas);
    }




    private void onFingerUp(float x,float y){
        if(!mPageFlip.isAnimating()){
            mPageFlip.onFingerUp(x,y,1000);
            try {
                mDrawLock.lock();
                if(mPageFlip.animating()){
                    mDrawCommand = DRAW_ANIMATING_FRAME;
                    requestRender();
                }
            }finally {
                mDrawLock.unlock();
            }
        }
    }

    private void onFingerMove(float x,float y){
        if(mPageFlip.isAnimating()){
        }else if(mPageFlip.canAnimate(x,y)){
            onFingerUp(x,y);
        }else if(mPageFlip.onFingerMove(x,y)){
            try {
                mDrawLock.lock();
                mDrawCommand = DRAW_MOVING_FRAME;
                requestRender();
            }finally {
                mDrawLock.unlock();
            }
        }
    }


    private void onFingerDown(float x,float y){
        if(!mPageFlip.isAnimating() && mPageFlip.getFirstPage() != null){
            mPageFlip.onFingerDown(x,y);
        }
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        try {
            mPageFlip.onSurfaceCreated();
        } catch (PageFlipException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        try {
            mPageFlip.onSurfaceChanged(width, height);
        } catch (PageFlipException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        try {
            mDrawLock.lock();
            onDrawFrame();
        }finally {
            mDrawLock.unlock();
        }
    }


    @Override
    public boolean canFlipForward() {
        return (pageSize < MAX_PAGES);
    }

    @Override
    public boolean canFlipBackward() {
        if (pageSize > 1) {
            mPageFlip.getFirstPage().setSecondTextureWithFirst();
            return true;
        }else {
            return false;
        }
    }
}
