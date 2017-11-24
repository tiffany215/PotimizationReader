package shuhai.readercore.view.readview.displayview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.eschao.android.widget.pageflip.PageFlip;
import com.eschao.android.widget.pageflip.PageFlipException;
import com.kingja.loadsir.core.LoadService;

import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.dialog.BookReadSettingDialog;
import shuhai.readercore.ui.dialog.LoadingCallback;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.utils.ToastUtils;
import shuhai.readercore.view.readview.dataloader.HorizontalScrollChapterLoader;
import shuhai.readercore.view.readview.factory.Factory;
import shuhai.readercore.view.readview.factory.PageFactory;
import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.status.FlipStatus;


/**
 * @author 55345364
 * @date 2017/9/1.
 */

@SuppressLint("NewApi")
public abstract class GLHorizontalBaseReadView extends GLSurfaceView implements GLSurfaceView.Renderer, BaseReadViewImpl{

    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;
    protected PageFlip mPageFlip;
    private ReentrantLock mDrawLock;
    protected BookStatus mBookStatus;
    public Handler mHandler;
    protected final static int MSG_ENDED_DRAWING_FRAME = 1;
    protected final static int DRAW_MOVING_FRAME = 0;
    protected final static int DRAW_ANIMATING_FRAME = 1;
    protected final static int DRAW_FULL_PAGE = 2;

    // 点击页面区域
    private static final int PREVIOUS_AREA = 1;
    private static final int NEXT_AREA = 2;
    private static final int SETTING_AREA = 3;

    //获取滑动速度
    private VelocityTracker vt;
    //当前的滑动速度
    private float speed;
    //防止抖动
    private float speed_shake;

    protected int mDrawCommand;

    protected PageFactory factory;
    private LoadService mLoadService;

    private Context mContext;

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

        mPrePageBitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        mCurPageBitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);

        mPrePageCanvas =  new Canvas(mPrePageBitmap);
        mCurPageCanvas =  new Canvas(mCurPageBitmap);
        mNextPageCanvas =  new Canvas(mNextPageBitmap);
        mDrawCommand = DRAW_FULL_PAGE;

        factory = new PageFactory.Builder(context)
                .setLoadStrategy(HorizontalScrollChapterLoader.class)
//                .setComposingStrategy()
                .setOnReaderLoadingListener(new GLOnReaderLoadingListener())
                .builder();

        ViewConfiguration configuration = ViewConfiguration.get(context);
        speed_shake = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        this.mContext = context;

    }


    @Override
    public void init(int theme) {
        factory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
    }

    @Override
    public synchronized void openBook(int articleId, int chapterId, int chapterOrder, FlipStatus status) {
        mBookStatus = factory.openBook(articleId,chapterId,chapterOrder,status);
        switch (mBookStatus) {
            case LOAD_START:
                Toast.makeText(getContext(),"开始加载本书！",Toast.LENGTH_LONG).show();
                break;
            case LOAD_SUCCESS:
                if(null != mLoadService){
                    mLoadService.showSuccess();
                }
                factory.prePage(mPrePageCanvas);
                factory.curPage(mCurPageCanvas);
                factory.nextPage(mNextPageCanvas);
                requestRender();
                break;
        }
    }



    public abstract void onDrawFrames();

    public abstract boolean onEndedDrawing(int what);

    public synchronized void closeBook(){
        factory.closeBook();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
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
                onFingerDown(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                vt.addMovement(event);
                vt.computeCurrentVelocity(500);
                speed = vt.getXVelocity();
                onFingerMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(speed) < speed_shake)
                    speed = 0;
                if(getTouchLocal(event.getX(),event.getY()) == SETTING_AREA && Math.abs(speed) < 100 ){
                    new BookReadSettingDialog(mContext,factory).show();
                    return true;
                }
                onFingerUp(event.getX(), event.getY());
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
            factory.onDraw(mPrePageCanvas);
            factory.onDraw(mCurPageCanvas);
            factory.onDraw(mNextPageCanvas);
            requestRender();
        }

        @Override
        public void postOnDrawableInvalidatePage() {

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


    /**
     * 判断点击的区域
     *
     * @param x
     * @param y
     * @return
     */
    private int getTouchLocal(float x, float y) {
        if (x > ScreenUtils.getScreenWidth() / 4 && x < ScreenUtils.getScreenWidth() * 3 / 4
                && y > ScreenUtils.getScreenHeight() / 4 && y < ScreenUtils.getScreenHeight() * 3 / 4) {
            return SETTING_AREA;
        } else if (x > ScreenUtils.getScreenWidth() / 2) {
            return NEXT_AREA;
        }
        return PREVIOUS_AREA;
    }
}
