package shuhai.readercore.view.readview.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import shuhai.readercore.Constants;
import shuhai.readercore.R;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.dataloader.ChapterLoaderImpl;
import shuhai.readercore.view.readview.dataloader.HorizontalScrollChapterLoader;
import shuhai.readercore.view.readview.strategy.HorizontalComposing;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class PageFactory extends Factory {

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
    private int mMarginWidth,mMarginHeight;

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
    private int mBookId;
    private int mChapterId;

    public PageFactory(Context context, int bookId ,int chapterId){
        this(context,ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(), Constants.MARGIN_WIDTH,Constants.MARGIN_HEIGHT,38,bookId,chapterId);
    }

    public PageFactory(Context context, int width, int height, int marginWidth,int marginHeight,int fontSize, int bookId,int chapterId){
        this.mBookId = bookId;
        this.mChapterId = chapterId;
        mContext = context;
        mWidth = width;
        mHeight = height;
        mFontSize = fontSize;
        mMarginWidth = marginWidth;
        mMarginHeight = marginHeight;
        mLineSpace = mFontSize / 5 * 2;
        mVisibleHeight = mHeight - marginHeight * 2 + mNumFontSize * 2 - mLineSpace * 2;
        mVisibleWidth = mWidth - marginWidth * 2;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        rectF = new Rect(0,0,mWidth,mHeight);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mFontSize);
        mPaint.setColor(ContextCompat.getColor(context, R.color.primary_text));

        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setTextSize(mNumFontSize);
        mTitlePaint.setColor(ContextCompat.getColor(context, R.color.colorAccent));

        timeLen = (int) mTitlePaint.measureText("00:00");
        percentLen = (int) mTitlePaint.measureText("00.00%");

        /**
         * 设置自定义字体
         */

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/HYQiHei-50S.otf");
        mPaint.setTypeface(typeface);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mTitlePaint.setTypeface(typeface);


//        AssetManager mgr= mContext.getAssets();//得到AssetManager
//        Typeface tf =Typeface.createFromAsset(mgr, "fonts/HYQiHei-50S.otf");
//        mPaint.setTypeface(tf);
//        mPaint.setAntiAlias(true);
//        mPaint.setColor(0XFF303030);
//        mPaint.setStyle(Style.STROKE);


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
    public BookStatus nextPage() {
        if(null != chapterLoader.pageDown(1,"") && chapterLoader.pageDown(1,"").size() > 0){
            mLines = chapterLoader.pageDown(1,"");
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_NEXT_PAGE;
    }

    @Override
    public BookStatus prePage() {
        if(null != chapterLoader.pageUp(1,"") && chapterLoader.pageUp(1,"").size() > 0){
            mLines = chapterLoader.pageUp(1,"");
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_PRE_PAGE;
    }

    @Override
    public BookStatus curPage() {
        if(null != chapterLoader.pageCur(1,mBookId+ ""+ mChapterId) && chapterLoader.pageCur(1,mBookId+ ""+ mChapterId).size() > 0){
            mLines = chapterLoader.pageCur(1,"");
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_PRE_PAGE;
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
        if(null != chapterLoader){
            this.chapterLoader.setComposingStrategy(new HorizontalComposing(mWidth,mHeight,mMarginWidth,mMarginHeight,mFontSize,mPaint,mTitlePaint));
        }
    }




    /**
     * 绘制章节内容到具体 widget 的 bitmap上。
     * @param canvas  由具体的 widget (如仿真翻页组件) 传过来的 canvas。
     */
    @Override
    public synchronized void onDraw(Canvas canvas){
        if(mLines.size() > 0){
            int y = mMarginHeight + (mLineSpace << 1);
            if(null == mBookPageBg){
                canvas.drawColor(Color.WHITE);
            }else{
                canvas.drawBitmap(mBookPageBg,null,rectF,null);
            }

            canvas.drawText("啥都会发生看机会",mMarginWidth,y,mTitlePaint);
            y += mLineSpace + mNumFontSize;

            for (String line : mLines)
            {
                y += mLineSpace;
                canvas.drawText(line,mMarginWidth,y,mPaint);
                y += mFontSize;
            }

            if(null != batteryBitmap){
                canvas.drawBitmap(batteryBitmap,mMarginWidth + 2, mHeight - mMarginHeight - ScreenUtils.dpToPxInt(12), mTitlePaint);
            }

            float percent = (float) 80.56;
            canvas.drawText(decimalFormat.format(percent) + "%",(mWidth - percent) / 2,mHeight  - mMarginHeight, mTitlePaint);
            String mTime = simpleDateFormat.format(new Date());
            canvas.drawText(mTime, mWidth - mMarginWidth - timeLen, mHeight - mMarginHeight, mTitlePaint);
        }
    }

    @Override
    public void setBgBitmap(Bitmap bitmap) {
        this.mBookPageBg = bitmap;
    }


}
