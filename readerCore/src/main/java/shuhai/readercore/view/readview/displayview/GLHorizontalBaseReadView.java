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
public abstract class GLHorizontalBaseReadView extends GLSurfaceView implements GLSurfaceView.Renderer, BaseReadViewImpl{

    protected Bitmap mBitmap;
    protected Canvas mCanvas;
    protected PageFlip mPageFlip;
    private ReentrantLock mDrawLock;
    private BookStatus mBookStatus;
    protected int mPageSize;
    private int pageCount;

    public Handler mHandler;

    protected final static int MSG_ENDED_DRAWING_FRAME = 1;

    protected final static int DRAW_MOVING_FRAME = 0;
    protected final static int DRAW_ANIMATING_FRAME = 1;
    protected final static int DRAW_FULL_PAGE = 2;

    protected int mDrawCommand;
    protected int mBookId;
    private int mChapterId;

    protected Factory factory;

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
        mBookStatus = BookStatus.START_LOAD_SUCCESS;
        mPageSize = UserSP.getInstance().getLastReaderPage(mBookId);
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
                drawPage(mChapterId,mPageSize);
                break;
            case PRE_CHAPTER_LOAD_SUCCESS:
                drawPage(mChapterId,mPageSize);
                break;
            //下一章内容加载成功后
            case NEXT_CHAPTER_LOAD_SUCCESS:
                drawPage(mChapterId,mPageSize);
                break;
        }
        requestRender();
    }



    public abstract void onDrawFrame(int chapterId);

    public abstract boolean onEndedDrawing(int what);

    public abstract  void drawPage(int chapterId,int pageSize);

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
            onDrawFrame(mChapterId);
        }finally {
            mDrawLock.unlock();
        }
    }


    /**
     *创建消息处理程序来处理来自页面呈现的消息，
     *页面渲染将在GL线程中发送消息，但我们要处理这些消息
     *主线程中的消息，为什么我们需要在这里处理程序
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
}
