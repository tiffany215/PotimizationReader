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
     * @param chapterOrder
     * @param status
     * @return 0：文件不存在或打开失败  1：打开成功
     */
    @Override
    public int openBook(int articleId,int chapterId,int chapterOrder,FlipStatus status){
        this.mBookId = articleId;
        this.mChapterId = chapterId;
        this.mChapterOrder = chapterOrder;
        if(null != chapterLoader){
            chapterLoader.clearPageCache();
            chapterLoader.characterTypesetting(cacheKeyCreate(articleId,chapterId));
            pageCount = chapterLoader.getCountPate();
            switch (status) {
                case ON_FLIP_PRE:
                    currentPage = pageCount;
                    break;
                case ON_FLIP_CUR:
                    currentPage = 1;
                    break;
                case ON_FLIP_NEXT:
                    currentPage = 1;
                    break;
            }
        }
        if(BookStatus.LOAD_SUCCESS == curPage()){
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
    public BookStatus prePage() {
        currentPage--;
        if(currentPage <= 0 ){
            currentPage = 0;
        }else if(currentPage == chapterLoader.getCountPate()){
            currentPage = chapterLoader.getCountPate() - 1;
        }
        //加载缓存中章节内容
        Vector<String> lines = chapterLoader.pageUp(currentPage,cacheKeyCreate(mBookId,mChapterId));
        if(null != lines && lines.size() > 0){
            mLines = lines;
            return BookStatus.LOAD_SUCCESS;
        }
        //如果缓存中内容不存在，则去网络加载内容
        if(null == lines || lines.size() <= 0){
            preChapter();
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
        if(null == lines || lines.size() <= 0){
            curChapter();
        }
        return BookStatus.NO_PRE_PAGE;
    }


    @Override
    public BookStatus nextPage() {
        currentPage++;
        if(currentPage == 1){
            currentPage = 2;
        }else if(currentPage > chapterLoader.getCountPate()){
            currentPage = chapterLoader.getCountPate() + 1;
        }
        Vector<String> lines = chapterLoader.pageDown(currentPage,cacheKeyCreate(mBookId,mChapterId));
        if(null != lines && lines.size() > 0){
            mLines = lines;
            return BookStatus.LOAD_SUCCESS;
        }
        if(null == lines || lines.size() <= 0){
            nextChapter();
        }
        return BookStatus.LOAD_SUCCESS;
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
        //查询下一章章节信息，如果没有章节信息，则从网络中获取章节信息并存入数据库，
        // 如果有则从缓存中获取章节信息。
        loadChapter(mChapterOrder,FlipStatus.ON_FLIP_NEXT);
    }

    /**
     *
     * @param chapterOrder
     * @param status
     */
    public void loadChapter(int chapterOrder,FlipStatus status){
        mChapterEntity = DataBaseManager.getInstance().queryNextChapterInfo(2,mBookId,chapterOrder,status);
        if(null != mChapterEntity){
            //从缓存中获取章节内容，如果章节内容为空则从网络中获取。
            String chapterContent =  ChapterLoader.getChapter(cacheKeyCreate(mBookId,mChapterEntity.getChpid()));
            if(!TextUtils.isEmpty(chapterContent)){
                onPageChanged(Integer.parseInt(String.valueOf(mChapterEntity.getChpid())),mChapterEntity.getChiporder(),status);
                return;
            }
        }
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
    private String cacheKeyCreate(Object... str){
        StringBuffer buffer = new StringBuffer();
        for(Object value : str){
            buffer.append(value.toString());
        }
        return buffer.toString();
    }

    public void setOnReadStateChangeListener(OnReadStateChangeListener listener){
        this.listener = listener;
    }

    public void onChapterChanged(int chapterId,int chapterOrder,FlipStatus status){
        if(null != listener){
            listener.onChapterChanged(chapterId,chapterOrder,status);
        }
    }

    public void onPageChanged(int chapterId,int chapterOrder,FlipStatus status){
        if(null != listener){
            listener.onPageChanged(chapterId,chapterOrder,status);
        }
    }
}
