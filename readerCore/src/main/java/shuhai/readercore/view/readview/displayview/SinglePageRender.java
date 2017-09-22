package shuhai.readercore.view.readview.displayview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

import com.eschao.android.widget.pageflip.OnPageFlipListener;
import com.eschao.android.widget.pageflip.Page;
import com.eschao.android.widget.pageflip.PageFlip;
import com.eschao.android.widget.pageflip.PageFlipState;


/**
 *
 */
public class SinglePageRender implements OnPageFlipListener {

    public final static int MSG_ENDED_DRAWING_FRAME = 1;

    final static int DRAW_MOVING_FRAME = 0;
    final static int DRAW_ANIMATING_FRAME = 1;
    final static int DRAW_FULL_PAGE = 2;

    final static int MAX_PAGES = 30;

    int mPageNo;
    int mDrawCommand;
    Bitmap mBitmap;
    Canvas mCanvas;
    Bitmap mBackgroundBitmap;
    Context mContext;
    Handler mHandler;
    PageFlip mPageFlip;

    /**
     * Constructor
     */
    public SinglePageRender(Context context, PageFlip pageFlip,
                            Handler handler, int pageNo) {
        mContext = context;
        mPageFlip = pageFlip;
        mPageNo = pageNo;
        mDrawCommand = DRAW_FULL_PAGE;
        mCanvas = new Canvas();
        mPageFlip.setListener(this);
        mHandler = handler;
    }

    /**
     * Draw frame
     */
    public void onDrawFrame(Bitmap bitmap) {
        // 1. delete unused textures
        mPageFlip.deleteUnusedTextures();
        Page page = mPageFlip.getFirstPage();

        // 2. 手指移动和动画触发的处理绘图命令
        if (mDrawCommand == DRAW_MOVING_FRAME ||
                mDrawCommand == DRAW_ANIMATING_FRAME) {
            // 正向翻转
            if (mPageFlip.getFlipState() == PageFlipState.FORWARD_FLIP) {
                //检查第一页的第二个纹理是否有效，如果没有，创建新的。
                if (!page.isSecondTextureSet()) {
                    drawPage(bitmap);
                    page.setSecondTexture(mBitmap);
                }
            }
            // 在向后翻页，第一页的第一个纹理检查是有效的
            else if (!page.isFirstTextureSet()) {
                drawPage(bitmap);
                page.setFirstTexture(mBitmap);
            }

            // 绘制翻页效果
            mPageFlip.drawFlipFrame();
        }
        // 画平静的页面而不翻转
        else if (mDrawCommand == DRAW_FULL_PAGE) {
            if (!page.isFirstTextureSet()) {
                drawPage(bitmap);
                page.setFirstTexture(mBitmap);
            }

            mPageFlip.drawPageFrame();
        }

        // 3.发送消息给主线程通知绘图结束
        //如果需要，我们可以继续计算下一个动画帧。
        //记住：绘图操作始终在GL线程中，而不是
        //主线程
        Message msg = Message.obtain();
        msg.what = MSG_ENDED_DRAWING_FRAME;
        msg.arg1 = mDrawCommand;
        mHandler.sendMessage(msg);
    }

    /**
     * 处理GL表面已更改
     *
     * @param width surface width
     * @param height surface height
     */
    public void onSurfaceChanged(int width, int height) {
        // 如果需要，可以回收位图资源
        if (mBackgroundBitmap != null) {
            mBackgroundBitmap.recycle();
        }

        if (mBitmap != null) {
            mBitmap.recycle();
        }

        // 为页面创建位图和画布
        //mBackgroundBitmap = background;
        Page page = mPageFlip.getFirstPage();
        mBitmap = Bitmap.createBitmap((int)page.width(), (int)page.height(),
                Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
    }

    /**
     * 处理结束绘图事件
     * 在这里，我们只处理动画制作事件，如果我们需要的话
     * 继续请求渲染，请返回true。 记住这个功能
     * 将在主线程中调用
     * @param what event type
     * @return ture if need render again
     */
    public boolean onEndedDrawing(int what) {
        if (what == DRAW_ANIMATING_FRAME) {
            boolean isAnimating = mPageFlip.animating();
            // 继续动画
            if (isAnimating) {
                mDrawCommand = DRAW_ANIMATING_FRAME;
                return true;
            }
            //动画完成
            else {
                final PageFlipState state = mPageFlip.getFlipState();
                // 更新页码进行向后翻页
                if (state == PageFlipState.END_WITH_BACKWARD) {
                    //由于mPageNo总是不要在页码上执行任何操作
                    //表示FIRST_TEXTURE否;
                }
                //更新页码并切换纹理以进行向前翻页
                else if (state == PageFlipState.END_WITH_FORWARD) {
                    mPageFlip.getFirstPage().setFirstTextureWithSecond();
                    mPageNo++;
                }

                mDrawCommand = DRAW_FULL_PAGE;
                return true;
            }
        }
        return false;
    }

    /**
     * 绘制显示内容
     *
     * @param
     */
    private void drawPage(Bitmap bitmap) {
        final int width = mCanvas.getWidth();
        final int height = mCanvas.getHeight();
        Paint p = new Paint();
        p.setFilterBitmap(true);
        Rect rect = new Rect(0, 0, width, height);
        mCanvas.drawBitmap(bitmap, null, rect, p);
//        bitmap.recycle();
//        bitmap = null;

    }

    /**
     * 如果页面可以向前翻页
     *
     * @return
     */
    public boolean canFlipForward() {
        return (mPageNo < MAX_PAGES);
    }

    /**
     * 如果页面可以向后翻页
     *
     * @return
     */
    public boolean canFlipBackward() {
        if (mPageNo > 1) {
            mPageFlip.getFirstPage().setSecondTextureWithFirst();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 获取页码
     *
     * @return page number
     */
    public int getPageNo() {
        return mPageNo;
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }

        mPageFlip.setListener(null);
        mCanvas = null;
        mBackgroundBitmap = null;
    }


    /**
     * 手指移动事件
     *
     * @param x x手指移动坐标
     * @param y y手指移动坐标
     * @如果事件被处理，返回true
     */
    public boolean onFingerMove(float x, float y) {
        mDrawCommand = DRAW_MOVING_FRAME;
        return true;
    }

     /**
      * 手指握手事件
      *
      * @param x x手指坐标
      * @param y y加入的坐标
      * @如果事件被处理，返回true
      */
    public boolean onFingerUp(float x, float y) {
        if (mPageFlip.animating()) {
            mDrawCommand = DRAW_ANIMATING_FRAME;
            return true;
        }

        return false;
    }

    /**
     * 用给定的SP单位计算字体大小
     */
    protected int calcFontSize(int size) {
        return (int)(size * mContext.getResources().getDisplayMetrics()
                .scaledDensity);
    }
}
