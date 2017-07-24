package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.factory.Factory;
import shuhai.readercore.view.readview.factory.PageFactory;

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

    protected PointF mTouch = new PointF();
    protected Bitmap mPrePageBitmap,mCurPageBitmap,mNextPageBitmap;
    protected Canvas mPrePageCanvas,mCurPageCanvas,mNextPageCanvas;

    public float actiondownX,actiondownY;

    private Factory factory;

    public Scroller mScroller;
    public boolean isPrepare;

    private int mBookId;
    private int mChapterId;



    public HorizontalBaseReadView(Context context,int bookId,int chapterId) {
        super(context);

        this.mBookId = bookId;
        this.mChapterId = chapterId;

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

    private int dx;
    private int dy;
    private boolean center;



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
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
                          Toast.makeText(getContext(),"没有上一页了",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getContext(),"没有下一页了",Toast.LENGTH_LONG).show();
                            return false;
                        }else if(bookStatus == BookStatus.LOAD_SUCCESS){
                            abortAnimation();
                            factory.onDraw(mNextPageCanvas);
                        }else{
                            return false;
                        }
                    }
                        postInvalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(center){
                    break;
                }
                int mx = (int) event.getX();
                int my = (int) event.getY();

                mTouch.x = mx;
                mTouch.y = my;

                this.requestLayout();
                break;
            case MotionEvent.ACTION_CANCEL:

                if(center){



                    break;
                }


                break;
        }
        return super.onTouchEvent(event);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        drawPrePageArea(canvas);
        drawPrePageShadow(canvas);

        drawCurPageArea(canvas);
        drawCurPageShadow(canvas);

        drawNextPageArea(canvas);
        drawNextPageShadow(canvas);
    }




    protected abstract void drawPrePageArea(Canvas canvas);
    protected abstract void drawPrePageShadow(Canvas canvas);

    protected abstract void drawCurPageArea(Canvas canvas);
    protected abstract void drawCurPageShadow(Canvas canvas);

    protected abstract void drawNextPageArea(Canvas canvas);
    protected abstract void drawNextPageShadow(Canvas canvas);


}


