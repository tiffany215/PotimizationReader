package shuhai.readercore.view.readview.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import shuhai.readercore.R;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.dataloader.ChapterLoaderImpl;
import shuhai.readercore.view.readview.dataloader.HorizontalScrollChapterLoader;
import shuhai.readercore.view.readview.strategy.HorizantalComposing;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class PageFactroy extends Factory {

    private Context mContext;

    /**
     * 屏幕的高度和宽度
     */
    private int mHeight,mWidth;

    /**
     * 可绘制区域的高度和宽度
     */
    private int mVisibleHeight,mVisibleWidth;

    /**
     * 间距
     */
    private int marginWidth,marginHeight;

    /**
     * 文字大小
     */
    private int mFontSize,mNumFontSize;

    /**
     * 每页行数
     */
    private int mPageLineCount;

    /**
     * 行间距
     */
    private int mLineSpace;

    private ChapterLoaderImpl chapterLoader;

    private Vector<String> mLines = new Vector<>();

    private Paint mPaint;

    private Paint mTitlePaint;

    private Bitmap mBookPageBg;


    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private int timeLen = 0;
    private int percentLen = 0;
    private String time;
    private int battery = 40;
    private Rect rectF;
    private ProgressBar batteryView;
    private Bitmap batteryBitmap;


    public PageFactroy(Context context,String bookId){
        this(context,ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(),34,bookId);

    }



    public PageFactroy(Context context,int width,int height,int fontsize,String bookId){
        mContext = context;
        mWidth = width;
        mHeight = height;
        mFontSize = fontsize;
        mLineSpace = mFontSize / 5 * 2;
        mVisibleHeight = mHeight - marginHeight * 2 + mNumFontSize * 2 - mLineSpace * 2;
        mVisibleWidth = mWidth - marginWidth * 2;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        rectF = new Rect(0,0,mWidth,mHeight);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mFontSize);
        mPaint.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        mPaint.setColor(Color.BLACK);

        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setTextSize(mNumFontSize);
        mPaint.setColor(ContextCompat.getColor(context, R.color.colorAccent));

        timeLen = (int) mTitlePaint.measureText("00:00");
        percentLen = (int) mTitlePaint.measureText("00.00%");

        /**
         * 设置自定义字体
         */
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"");
        mPaint.setTypeface(typeface);
        mTitlePaint.setTypeface(typeface);


    }



    @Override
    public <T extends ChapterLoaderImpl> T createChapterLoader(Class<T> clz) {
        ChapterLoaderImpl chapterLoader = null;
        try {
            chapterLoader = (ChapterLoaderImpl) Class.forName(clz.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) chapterLoader;
    }

    @Override
    BookStatus nextPage() {
        if(null != chapterLoader.pageDown() && chapterLoader.pageDown().size() > 0){
            mLines = chapterLoader.pageDown();
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_NEXT_PAGE;
    }

    @Override
    BookStatus prePage() {
        if(null != chapterLoader.pageUp() && chapterLoader.pageUp().size() > 0){
            mLines = chapterLoader.pageUp();
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_PRE_PAGE;
    }

    @Override
    BookStatus curPage() {
        return null;
    }


    /**
     * 设置数据加载类
     */
    public void setChapterLoader(){
        this.chapterLoader = createChapterLoader(HorizontalScrollChapterLoader.class);
    }

    /**
     * 设置数据加载类的策略
     */
    public void setComposingStrategy(){
        this.chapterLoader.setComposingStrategy(new HorizantalComposing(mWidth,mHeight,mFontSize));
    }




    /**
     * 绘制章节内容到具体 widget 的 bitmap上。
     * @param canvas  由具体的 widget (如仿真翻页组件) 传过来的 canvas。
     */
    @Override
    public synchronized void onDraw(Canvas canvas){
        if(mLines.size() > 0){
            int y = marginHeight + (mLineSpace << 1);
            if(null == mBookPageBg){
                canvas.drawColor(Color.WHITE);
            }else{
                canvas.drawBitmap(mBookPageBg,null,rectF,null);
            }

            canvas.drawText("",marginWidth,y,mTitlePaint);
            y += mLineSpace + mNumFontSize;

            for (String line : mLines)
            {
                y += mLineSpace;
                canvas.drawText(line,marginWidth,y,mPaint);
                y += mFontSize;
            }

            if(null != batteryBitmap){
                canvas.drawBitmap(batteryBitmap,marginWidth + 2, mHeight - marginHeight - ScreenUtils.dpToPxInt(12), mTitlePaint);
            }

            float percent = (float) 80.56;
            canvas.drawText(decimalFormat.format(percent) + "%",(mWidth - percent) / 2,mHeight  - marginHeight, mTitlePaint);
            String mTime = simpleDateFormat.format(new Date());
            canvas.drawText(mTime, mWidth - marginWidth - timeLen, mHeight - marginHeight, mTitlePaint);
        }
    }





























}
