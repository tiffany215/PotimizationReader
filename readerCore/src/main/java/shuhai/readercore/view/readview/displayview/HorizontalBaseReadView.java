package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.utils.ScreenUtils;
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

//    protected PointF mTouch = new PointF();
    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;
    // 页面状态
    private static final int STATE_MOVE = 0;
    private static final int STATE_STOP = 1;
    private int state;

    // 前一页，当前页，下一页的左边位置
    private int prePageLeft = 0, currPageLeft = 0, nextPageLeft = 0;

    //获取滑动速度
    private VelocityTracker vt;
    //当前的滑动速度
    private float speed;
    //防止抖动
    private float speed_shake = 20;
    // 正在滑动的页面右边位置，用于绘制阴影
    private float right;

    private float lastX;

    private Factory factory;

    public Scroller mScroller;
    public boolean isPrepare;

    private int mBookId;

    OnReadStateChangeListener listener;

    private FlipStatus mFlipStatus = FlipStatus.ON_FLIP_CUR;



    public HorizontalBaseReadView(Context context,int bookId,int chapterId,OnReadStateChangeListener listener) {
        super(context);

        this.mBookId = bookId;
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
    }


    public synchronized void init(int theme){
        if(!isPrepare){
            factory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
            openBook(mBookId,UserSP.getInstance().getLastReaderChapterId(mBookId),UserSP.getInstance().getLastReaderChapterOrder(mBookId),FlipStatus.ON_FLIP_CUR);
        }
    }


    public synchronized void openBook(int articleId,int chapterId,int chapterOrder,FlipStatus status){
        int ret = factory.openBook(articleId,chapterId,chapterOrder,status);
        if(ret == 0){
            Toast.makeText(getContext(),"章节内容打开失败！",Toast.LENGTH_LONG).show();
            return;
        }
        switch (status) {
            case ON_FLIP_PRE:
                factory.onDraw(mPrePageCanvas);
                break;

            case ON_FLIP_CUR:
                factory.onDraw(mCurPageCanvas);
                break;

            case ON_FLIP_NEXT:
                factory.onDraw(mNextPageCanvas);
                break;
        }
        state = STATE_STOP;
        isPrepare = true;
        mFlipStatus = status;
        postInvalidate();
    }



    private int dx;
    private int dy;
    private boolean center;
    // 手指滑动的距离
    public  float moveLength;



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                lastX = event.getX();
//
//                if(null == vt){
//                    vt = VelocityTracker.obtain();
//                }else{
//                    vt.clear();
//                }
//                vt.addMovement(event);


                dx = (int) event.getX();
                dy = (int) event.getY();





//                if(actiondownX >= mScreenWidth / 3 && actiondownX <= mScreenWidth * 2 / 3
//                        && actiondownY >= mScreenHeight / 3 && actiondownY <= mScreenHeight * 2 / 3 ){
//                    center = true;
//                }else{
//                    center = false;
//                    //条件成立为右翻
//                    if(actiondownX < mScreenWidth / 2){
//                        BookStatus bookStatus = factory.prePage();
//                        if(bookStatus == BookStatus.NO_PRE_PAGE){
//                          Toast.makeText(getContext(),"没有上一页了",Toast.LENGTH_SHORT).show();
//                            return false;
//                        }else if(bookStatus == BookStatus.LOAD_SUCCESS){
//                            abortAnimation();
//                            factory.onDraw(mPrePageCanvas);
//                            mFlipStatus = FlipStatus.ON_FLIP_PRE;
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
//                            mFlipStatus = FlipStatus.ON_FLIP_NEXT;
//                        }else{
//                            return false;
//                        }
//                    }
//                        postInvalidate();
//                }
                break;
            case MotionEvent.ACTION_MOVE:
//                vt.addMovement(event);
//                vt.computeCurrentVelocity(500);
//                speed = vt.getXVelocity();
                moveLength = event.getX() - lastX;
                //向左滑动
                if(moveLength < 0 && Math.abs(moveLength) > speed_shake){
                    mFlipStatus = FlipStatus.ON_FLIP_PRE;
                }else if(moveLength > 0 && Math.abs(moveLength) > speed_shake){
                    mFlipStatus = FlipStatus.ON_FLIP_NEXT;
                }
                if(state == STATE_STOP){
                    updatePageArea(mFlipStatus);
                }
                    state = STATE_MOVE;
                Log.e(TAG, "onTouchEvent: " + moveLength);
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                state = STATE_STOP;
                mScroller.startScroll((int)moveLength,0,(int)(moveLength - mScreenWidth),0,2000);

//                if(center){
//                    break;
//                }
//                if(Math.abs(speed) < speed_shake)
//                    speed = 0;
//                if(moveLength < 0 ){
//                    updatePageArea(FlipStatus.ON_FLIP_PRE);
//                }else {
//                    updatePageArea(FlipStatus.ON_FLIP_PRE);
//                }
//
//
//                vt.clear();
//                vt.recycle();
//
//                mScroller.startScroll(0,0,0,0,500);
//                postInvalidate();
                break;
        }
            return true;
//        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPageArea(canvas,mFlipStatus);
        drawPageShadow(canvas,mFlipStatus);
    }

    protected abstract void drawPageArea(Canvas canvas, FlipStatus status);
    protected abstract void drawPageShadow(Canvas canvas,FlipStatus status);


    /**
     *
     * @param status
     */
    private void updatePageArea(FlipStatus status){
        BookStatus bookStatus;
        switch (status) {
            case  ON_FLIP_PRE:
                bookStatus = factory.prePage();
                if(bookStatus == BookStatus.NO_PRE_PAGE){
                    Toast.makeText(getContext(),"没有上一页了",Toast.LENGTH_SHORT).show();
                }else if(bookStatus == BookStatus.LOAD_SUCCESS){
                    abortAnimation();
                    factory.onDraw(mPrePageCanvas);
                    mFlipStatus = FlipStatus.ON_FLIP_PRE;
                }
                break;
            case ON_FLIP_NEXT:
                bookStatus = factory.nextPage();
                if(bookStatus == BookStatus.NO_NEXT_PAGE){
                    Toast.makeText(getContext(),"没有下一页了",Toast.LENGTH_SHORT).show();
                }else if(bookStatus == BookStatus.LOAD_SUCCESS){
                    abortAnimation();
                    factory.onDraw(mNextPageCanvas);
                    mFlipStatus = FlipStatus.ON_FLIP_NEXT;
                }
                break;
        }
    }


    private void moveLeft(FlipStatus status){

        switch (status) {

            case ON_FLIP_PRE:

                break;

            case ON_FLIP_NEXT:


                break;

        }


    }

    private void moveRight(FlipStatus status){
        switch (status) {

            case ON_FLIP_PRE:

                break;

            case ON_FLIP_NEXT:


                break;
        }
    }

    private void addPrePage(){
        Bitmap temp = mNextPageBitmap;
        mNextPageBitmap = mCurPageBitmap;
        mCurPageBitmap = mPrePageBitmap;
        mPrePageBitmap = temp;
    }


    private void addNextPage(){
        Bitmap temp = mCurPageBitmap;
        mCurPageBitmap = mNextPageBitmap;
        mNextPageBitmap = mPrePageBitmap;
        mPrePageBitmap = temp;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            moveLength = mScroller.getCurrX();



            postInvalidate();
        }else{
//            state = STATE_STOP;
            moveLength = 0;
        }

    }
}



