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
import com.kingja.loadsir.core.LoadService;

import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.dialog.callback.LoadingCallback;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.utils.StringUtils;
import shuhai.readercore.view.readview.dataloader.HorizontalScrollChapterLoader;
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

    protected Factory factory;
    private LoadService mLoadService;

    public GLHorizontalBaseReadView(Context context, LoadService loadService) {
        super(context);
        this.mLoadService = loadService;
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

        factory = new PageFactory.Builder(context)
                .setLoadStrategy(HorizontalScrollChapterLoader.class)
//                .setComposingStrategy()
                .setOnReaderLoadingListener(new GLOnReaderLoadingListener())
                .builder();

    }


    @Override
    public void init(int theme) {
        factory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
    }

    @Override
    public synchronized void openBook(int articleId, int chapterId, int chapterOrder, FlipStatus status) {
        mBookStatus = factory.openBook(articleId,chapterId,chapterOrder,status);
        switch (mBookStatus) {
            case LOAD_ERROR:
                Toast.makeText(getContext(),"开始加载本书！",Toast.LENGTH_LONG).show();
                break;
            case LOAD_SUCCESS:
                if(null != mLoadService){
                    mLoadService.showSuccess();
                }
                factory.curPage(mCanvas);
                requestRender();
                break;
        }
    }



    public abstract void onDrawFrames();

    public abstract boolean onEndedDrawing(int what);

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
            onDrawFrames();
        }finally {
            mDrawLock.unlock();
        }
    }

    /**
     * 阅读章节加载状态监听器
     */
    private class GLOnReaderLoadingListener implements OnReaderLoadingListener{

        @Override
        public void postInvalidatePage() {
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
            factory.postInvalidatePage(mCanvas,flipStatus);
        }

        @Override
        public void onPageStatus(BookStatus bookStatus) {

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
