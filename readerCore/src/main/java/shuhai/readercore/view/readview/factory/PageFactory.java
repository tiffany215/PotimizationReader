package shuhai.readercore.view.readview.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import shuhai.readercore.Constants;
import shuhai.readercore.R;
import shuhai.readercore.dao.ChapterEntity;
import shuhai.readercore.manager.ChapterLoader;
import shuhai.readercore.manager.DataBaseManager;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.dataloader.ChapterLoaderImpl;
import shuhai.readercore.view.readview.dataloader.HorizontalScrollChapterLoader;
import shuhai.readercore.view.readview.displayview.OnReadStateChangeListener;
import shuhai.readercore.view.readview.FlipStatus;
import shuhai.readercore.view.readview.strategy.HorizontalComposing;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class PageFactory extends Factory {

    private Context mContext;

    OnReadStateChangeListener listener;

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
    private int mChapterOrder;
    private int currentPage = 1;
    private int pageCount = 1;

    private ChapterEntity mChapterEntity;
    private String TAG = "PageFactory";

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


    @Override
    public int openBook(int articleId,int chapterId,int chapterOrder,FlipStatus status){
        this.mBookId = articleId;
        this.mChapterId = chapterId;
        this.mChapterOrder = chapterOrder;
        if(null != chapterLoader){
            chapterLoader.clearPageCache();
            chapterLoader.characterTypesetting(cacheKeyCreate(articleId,chapterId));
            pageCount = chapterLoader.getCountPage();
        }
            mChapterEntity = DataBaseManager.getInstance().queryNextChapterInfo(2,mBookId,chapterOrder,FlipStatus.ON_FLIP_CUR);
            if(pageCount > 0){
                onSuccessLoadChapter();
                return 1;
        }else{
                return 0;
            }
    }

//    @Override
//    public int getCurPage() {
//        return currentPage;
//    }
//
//    @Override
//    public void setCurPage(int page) {
//        currentPage = page;
//    }
//
    @Override
    public int getCountPage() {
        return pageCount;
    }


    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mTitlePaint.setAlpha(alpha);
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
    public BookStatus getCurPageContent(int pageSize) {
        Vector<String> lines = chapterLoader.pageUp(pageSize,cacheKeyCreate(mBookId,mChapterId));
        if(null != lines && lines.size() > 0){
            mLines = lines;
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_PRE_PAGE;
    }


    @Override
    public BookStatus prePage() {
        currentPage--;
        if(currentPage <= 0 ){
            currentPage = 0;
        }else if(currentPage == chapterLoader.getCountPage()){
            currentPage = chapterLoader.getCountPage() - 1;
        }
        //加载缓存中章节内容
        Vector<String> lines = chapterLoader.pageUp(currentPage,cacheKeyCreate(mBookId,mChapterId));
        if(null != lines && lines.size() > 0){
            mLines = lines;
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_PRE_PAGE;
    }

    @Override
    public BookStatus curPage() {
        Vector<String> lines = chapterLoader.pageCur(currentPage,cacheKeyCreate(mBookId,mChapterId));
        if(null != lines && lines.size() > 0){
            mLines = lines;
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_PRE_PAGE;
    }


    @Override
    public BookStatus nextPage() {
        currentPage++;
        if(currentPage == 1){
            currentPage = 2;
        }else if(currentPage > chapterLoader.getCountPage()){
            currentPage = chapterLoader.getCountPage() + 1;
        }
        Vector<String> lines = chapterLoader.pageDown(currentPage,cacheKeyCreate(mBookId,mChapterId));
        if(null != lines && lines.size() > 0){
            mLines = lines;
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_NEXT_PAGE;
    }

    @Override
    public void preChapter() {
        loadChapter(mChapterOrder,FlipStatus.ON_FLIP_PRE);
    }

    @Override
    public void curChapter() {
        loadChapter(mChapterOrder,FlipStatus.ON_FLIP_CUR);
    }

    @Override
    public void nextChapter() {
        loadChapter(mChapterOrder,FlipStatus.ON_FLIP_NEXT);
    }

    /**
     * 加载章节，并刷新章节内容
     * @param chapterOrder
     * @param status
     */
    public void loadChapter(int chapterOrder,FlipStatus status){
        onStartLoadChapter();
        mChapterEntity = DataBaseManager.getInstance().queryNextChapterInfo(2,mBookId,chapterOrder,status);
        if(null != mChapterEntity){
            //从缓存中获取章节内容，如果章节内容为空则从网络中获取。
            String chapterContent =  ChapterLoader.getChapter(cacheKeyCreate(mBookId,mChapterEntity.getChpid()));
            if(!TextUtils.isEmpty(chapterContent)){
                onPageChanged(Integer.parseInt(String.valueOf(mChapterEntity.getChpid())),mChapterEntity.getChiporder(),status);
                return;
            }
        }
            //回调至ReadActivity中，从ReadActivity中回去网络中章节
            onChapterChanged(mChapterId, mChapterOrder,status);

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
            if(null != mChapterEntity){
                canvas.drawText(mChapterEntity.getChpnamme(),mMarginWidth,y,mTitlePaint);
            }
            y += mLineSpace;
            canvas.drawLine(mMarginWidth, y, mWidth - mMarginWidth, y, mTitlePaint);
            y += mFontSize;
            Log.e(TAG, "--------------------------------------------------->" );
            for (String line : mLines)
            {
                y += mLineSpace;
                canvas.drawText(line,mMarginWidth,y,mPaint);
                y += mFontSize;
                Log.e(TAG, "onDraw: " + line );
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

    /**
     * 设置阅读页背景
     * @param bitmap
     */
    @Override
    public void setBgBitmap(Bitmap bitmap) {
        this.mBookPageBg = bitmap;
    }


    /**
     * 缓存key生成方法
     * @return
     */
    private String cacheKeyCreate(Object... str){
        StringBuffer buffer = new StringBuffer();
        for(Object value : str){
            buffer.append(value.toString());
        }
        return buffer.toString();
    }

    /**
     * 设置翻页监听
     * @param listener
     */
    public void setOnReadStateChangeListener(OnReadStateChangeListener listener){
        this.listener = listener;
    }

    /**
     * 章节变化回调
     * @param chapterId
     * @param chapterOrder
     * @param status
     */
    public void onChapterChanged(int chapterId,int chapterOrder,FlipStatus status){
        if(null != listener){
            listener.onChapterChanged(chapterId,chapterOrder,status);
        }
    }

    /**
     * 页码变化回调
     * @param chapterId
     * @param chapterOrder
     * @param status
     */
    public void onPageChanged(int chapterId,int chapterOrder,FlipStatus status){
        if(null != listener){
            listener.onPageChanged(chapterId,chapterOrder,status);
        }
    }


    public void onStartLoadChapter(){
        if(null != listener){
            listener.onStartLoadChapter();
        }
    }


    public void onSuccessLoadChapter(){
        if(null != listener){
            listener.onSuccessLoadChapter();
        }
    }
}
