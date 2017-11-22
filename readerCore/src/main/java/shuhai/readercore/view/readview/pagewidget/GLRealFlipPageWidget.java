package shuhai.readercore.view.readview.pagewidget;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.eschao.android.widget.pageflip.OnPageFlipListener;
import com.eschao.android.widget.pageflip.Page;
import com.eschao.android.widget.pageflip.PageFlipState;
import com.kingja.loadsir.core.LoadService;

import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.displayview.GLHorizontalBaseReadView;
import shuhai.readercore.view.readview.status.BookStatus;

import static android.content.ContentValues.TAG;

/**
 * @author 55345364
 * @date 2017/9/1.
 */

public class GLRealFlipPageWidget extends GLHorizontalBaseReadView implements OnPageFlipListener {

    public GLRealFlipPageWidget(Context context,LoadService loadService) {
        super(context,loadService);
        mPageFlip.setListener(this);
    }


    /**
     * 处理结束绘图事件
     * 在这里，我们只处理动画绘制事件，如果我们需要的话
     * 继续请求渲染，请返回true。 记住这个功能
     * 将在主线程中调用
     *
     * @param what event type
     * @return ture if need render again
     */
    @Override
    public boolean onEndedDrawing(int what) {
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
//                    mPageFlip.getSecondPage().setSecondTextureWithFirst();
                        Log.e(TAG, "-----------------向前翻页完成-------------------->>"  );
                        factory.autoReduce();
                        factory.nextPage(mNextPageCanvas);
                        factory.curPage(mCurPageCanvas);
                        if(factory.prePage(mPrePageCanvas)){
                            mBookStatus = BookStatus.LOAD_SUCCESS;
                        }


                }
                // update page number and switch textures for forward flip
                else if (state == PageFlipState.END_WITH_FORWARD) {
                    Log.e(TAG, "-----------------向后翻页完成-------------------->>"  );
                    mPageFlip.getFirstPage().setFirstTextureWithSecond();
                    factory.autoIncrease();
                    factory.prePage(mPrePageCanvas);
                    factory.curPage(mCurPageCanvas);
                    if(factory.nextPage(mNextPageCanvas)){
                        mBookStatus = BookStatus.LOAD_SUCCESS;
                    }
                }

                mDrawCommand = DRAW_FULL_PAGE;
                return true;
            }
        }
        return false;
    }



    /**
     * Draw frame
     */
    @Override
    public void onDrawFrames() {
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
                    page.setSecondTexture(mNextPageBitmap);
                }
            }
            // 在向后翻页，第一页的第一个纹理检查是有效的
            else if (!page.isFirstTextureSet()) {
                page.setFirstTexture(mPrePageBitmap);
            }

            // draw frame for page flip
            // 画框翻页
            mPageFlip.drawFlipFrame();
        }
        // 画平静的页面而不翻转
        else if (mDrawCommand == DRAW_FULL_PAGE) {
            if (!page.isFirstTextureSet() || !page.isSecondTextureSet()) {
                page.setFirstTexture(mCurPageBitmap);
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



    @Override
    public boolean canFlipForward() {
        return factory.getPageSize() <= factory.getCountPage();
    }

    @Override
    public boolean canFlipBackward() {
        if (mBookStatus != BookStatus.NO_PRE_PAGE || factory.getPageSize() > 1) {
            mPageFlip.getFirstPage().setSecondTextureWithFirst();
            return true;
        }else {
            return false;
        }
    }


}
