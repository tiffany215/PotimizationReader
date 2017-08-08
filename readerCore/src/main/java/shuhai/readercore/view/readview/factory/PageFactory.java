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
     * 间距
     */
    private int mMarginWidth,mMarginHeight;

    /**
     * 文字大小
     */
    private int mFontSize,mNumFontSize;

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
    private int currentPage = 1;
    private int pageCount = 1;

    public PageFactory(Context context){
        this(context,ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(), Constants.MARGIN_WIDTH,Constants.MARGIN_HEIGHT,34);
    }

    public PageFactory(Context context, int width, int height, int marginWidth,int marginHeight,int fontSize){
        mContext = context;
        mWidth = width;
        mHeight = height;
        mFontSize = fontSize;
        mMarginWidth = marginWidth;
        mMarginHeight = marginHeight;
        mLineSpace = mFontSize / 5 * 4;
        mNumFontSize = 28;

        rectF = new Rect(0,0,mWidth,mHeight);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mFontSize);
        mPaint.setColor(ContextCompat.getColor(context, R.color.primary_text));

        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setTextSize(mNumFontSize);
        mTitlePaint.setStyle(Paint.Style.FILL);//设置填充样式
        mTitlePaint.setStrokeWidth(1);//设置画笔宽度
        mTitlePaint.setColor(ContextCompat.getColor(context, R.color.secondary_text));

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


    }


    /**
     * 打开书籍文件
     * @param articleId
     * @param chapterId
     * @param curPage
     * @return 0：文件不存在或打开失败  1：打开成功
     */
    @Override
    public int openBook(int articleId,int chapterId,int curPage){
        this.mBookId = articleId;
        this.mChapterId = chapterId;
        this.currentPage = curPage;
        if(BookStatus.LOAD_SUCCESS == curPage()){
            if(null != chapterLoader){
                pageCount = chapterLoader.getCountPate();
            }
                return 1;
        }else{
                return 0;
        }
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
        currentPage++;
        if(currentPage == 1){
            currentPage = 2;
        }else if(currentPage > chapterLoader.getCountPate()){
            currentPage = chapterLoader.getCountPate() + 1;
        }
        Vector<String> lines = chapterLoader.pageDown(currentPage,cacheKeyCreate());
        if(null != lines && lines.size() > 0){
            mLines = lines;
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_NEXT_PAGE;
    }

    @Override
    public BookStatus prePage() {
        currentPage--;
        if(currentPage <= 0 ){
            currentPage = 0;
        }else if(currentPage == chapterLoader.getCountPate()){
            currentPage = chapterLoader.getCountPate() - 1;
        }


        Vector<String> lines = chapterLoader.pageUp(currentPage,cacheKeyCreate());
        if(null != lines && lines.size() > 0){
            mLines = lines;
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_PRE_PAGE;
    }

    @Override
    public BookStatus curPage() {
        Vector<String> lines = chapterLoader.pageCur(currentPage,cacheKeyCreate());
        if(null != lines && lines.size() > 0){
            mLines = lines;
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
            this.chapterLoader.setComposingStrategy(new HorizontalComposing(mWidth,mHeight,mMarginWidth,mMarginHeight,mFontSize,mNumFontSize,mLineSpace,mPaint));
        }
    }


    /**
     * 绘制章节内容到具体 widget 的 bitmap上。
     * @param canvas  由具体的 widget (如仿真翻页组件) 传过来的 canvas。
     */
    @Override
    public synchronized void onDraw(Canvas canvas){
        if(mLines.size() > 0){
            int y = mMarginHeight + mNumFontSize;
            if(null == mBookPageBg){
                canvas.drawColor(Color.WHITE);
            }else{
                canvas.drawBitmap(mBookPageBg,null,rectF,null);
            }
            canvas.drawText("第十八章：徐美惠的眼线无孔不入。",mMarginWidth,y,mTitlePaint);
            y += mLineSpace;
            canvas.drawLine(mMarginWidth, y, mWidth - mMarginWidth, y, mTitlePaint);
            y += mFontSize;
            for (String line : mLines)
            {
                y += mLineSpace;
                canvas.drawText(line,mMarginWidth,y,mPaint);
                y += mFontSize;
            }

            if(null != batteryBitmap){
                canvas.drawBitmap(batteryBitmap,mMarginWidth + 2, mHeight - mMarginHeight - ScreenUtils.dpToPxInt(12), mTitlePaint);
            }

            float percent = (float) currentPage / pageCount;
            canvas.drawText(decimalFormat.format(percent * 100) + "%",mMarginWidth, mHeight  - mMarginHeight, mTitlePaint);
            String mTime = simpleDateFormat.format(new Date());
            canvas.drawText(mTime, mWidth - mMarginWidth - timeLen, mHeight - mMarginHeight, mTitlePaint);
        }
    }

    @Override
    public void setBgBitmap(Bitmap bitmap) {
        this.mBookPageBg = bitmap;
    }


    /**
     * 缓存key生成方法
     * @return
     */
    private String cacheKeyCreate(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(mBookId).append(mChapterId);
        return buffer.toString().trim();
    }



}
