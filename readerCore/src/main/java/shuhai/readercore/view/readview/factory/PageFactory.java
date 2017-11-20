package shuhai.readercore.view.readview.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.ProgressBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import shuhai.readercore.Constants;
import shuhai.readercore.bean.ChapterEntity;
import shuhai.readercore.manager.DataBaseManager;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.view.readview.dataloader.ChapterLoadManager;
import shuhai.readercore.view.readview.dataloader.ChapterLoaderModel;
import shuhai.readercore.view.readview.displayview.OnChapterLoadStatusListener;
import shuhai.readercore.view.readview.displayview.OnReaderLoadingListener;
import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.dataloader.ChapterLoaderStrategyImpl;
import shuhai.readercore.view.readview.dataloader.HorizontalScrollChapterLoader;
import shuhai.readercore.view.readview.status.FlipStatus;
import shuhai.readercore.view.readview.strategy.ComposingStrategy;
import shuhai.readercore.view.readview.strategy.HorizontalComposing;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class PageFactory extends Factory {


    private static Context mContext;

    /**
     * 屏幕的高度和宽度
     */
    private static int mHeight,mWidth;

    /**
     * 间距
     */
    private static int mMarginWidth,mMarginHeight;

    /**
     * 文字大小
     */
    private static int mFontSize,mNumFontSize;

    /**
     * 行间距
     */
    private static int mLineSpace;

    /**
     * 自定义字体路径
     */
    private static String mFontPath;


    private static ChapterLoaderStrategyImpl mChapterLoaderStrategy;
    private ChapterLoadManager chapterLoadManager;
    private static OnReaderLoadingListener mOnReaderLoadingListener;
    private ChapterLoaderModel mChapterModel;

    private static Paint mPaint;
    private static Paint mTitlePaint;

    private Bitmap mBookPageBg;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private static int timeLen = 0;
    private static int percentLen = 0;
    private static int appNameLen;
    private int battery = 40;
    private static Rect rectF;
    private ProgressBar batteryView;
    private Bitmap batteryBitmap;
    private int mArticleId;
    private int mChapterId;
    private int mChapterOrder;

    private PageFactory(){
        mPaint.setTextSize(mFontSize);
        mTitlePaint.setTextSize(mNumFontSize);
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),mFontPath);
        mPaint.setTypeface(typeface);
        mTitlePaint.setTypeface(typeface);

        appNameLen = (int) mTitlePaint.measureText("书海阅读");
        timeLen = (int) mTitlePaint.measureText("00:00");
        percentLen = (int) mTitlePaint.measureText("00.00%");
    }

    public static final class Builder{

        public Builder(Context context){

            mContext = context;
            mWidth = ScreenUtils.getScreenWidth();
            mHeight = ScreenUtils.getScreenHeight();

            rectF = new Rect(0,0,mWidth,mHeight);

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);

//            mPaint.setColor(ContextCompat.getColor(context, R.color.primary_text));
//            mPaint.setShadowLayer(1f, 0.5f, 0.5f, Color.WHITE);//设置阴影层，这是关键

            mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            mTitlePaint.setStyle(Paint.Style.FILL);//设置填充样式
            mTitlePaint.setStrokeWidth(1);//设置画笔宽度
        }


        /**
         * 设置自定义字体
         */
        public PageFactory.Builder setFontType(String fontPath){
            mFontPath = fontPath;
            return this;
        }

        /**
         * 设置文字上下边距
         * @return
         */
        public PageFactory.Builder setMarginHeight(int marginHeight){
            mMarginHeight = marginHeight;
            return this;
        }

        /**
         * 设置文字左右边距
         * @return
         */
        public PageFactory.Builder setMarginWidth(int marginWidth){
            mMarginWidth = marginWidth;
            return this;
        }


        /**
         * 设置正文字体大小
         * @return
         */
        public PageFactory.Builder setTextPartFontSize(int textPartFontSize){
            mFontSize = textPartFontSize;
            return this;
        }

        /**
         * 设置标题字体大小
         * @param titleFontSize
         * @return
         */
        public PageFactory.Builder setTitleFontSize(int titleFontSize){
            mNumFontSize = titleFontSize;
            return this;
        }

        /**
         * 设置正文行间距
         * @return
         */
        public PageFactory.Builder setLineSpace(int lineSpace){
            mLineSpace = lineSpace;
            return this;
        }


        /**
         * 设置数据加载类
         */
        public <T extends ChapterLoaderStrategyImpl> PageFactory.Builder setLoadStrategy(Class<T> clazz){
            mChapterLoaderStrategy = createChapterLoader(clazz);
            return this;
        }

        /**
         * 设置数据加载类的策略
         */
        public PageFactory.Builder setComposingStrategy(ComposingStrategy strategy){
            mChapterLoaderStrategy.setComposingStrategy(strategy);
            return this;
        }

        /**
         * 设置数据加载类的策略
         */
        public PageFactory.Builder setOnReaderLoadingListener(OnReaderLoadingListener listener){
            mOnReaderLoadingListener = listener;
            return this;
        }


        public PageFactory builder(){
            if(null == mChapterLoaderStrategy){
                createChapterLoader(HorizontalScrollChapterLoader.class);
            }

            if(null == mOnReaderLoadingListener){
                throw  new IllegalStateException("章节加载状态 mOnReaderLoadingListener 不能为 null");
            }

            if(mFontSize == 0){
                mFontSize =  34;
            }

            if(mNumFontSize == 0){
                mNumFontSize = 28;
            }

            if(mMarginHeight == 0){
                mMarginHeight = Constants.MARGIN_HEIGHT;
            }

            if(mMarginWidth == 0){
                mMarginWidth = Constants.MARGIN_WIDTH;
            }

            if(mLineSpace == 0){
                mLineSpace = mFontSize / 5 * 4;
            }

            if(TextUtils.isEmpty(mFontPath)){
                mFontPath = "fonts/HYQiHei-50S.otf";
            }

            mChapterLoaderStrategy.setComposingStrategy(new HorizontalComposing(mWidth,mHeight,mMarginWidth,mMarginHeight,mFontSize,mNumFontSize,mLineSpace,mPaint));
            return new PageFactory();
        }

    }




    @Override
    public BookStatus openBook(int articleId,int chapterId,int chapterOrder,FlipStatus status){
        this.mArticleId = articleId;
        this.mChapterId = chapterId;
        this.mChapterOrder = chapterOrder;
        chapterLoadManager = new ChapterLoadManager.Builder()
                .setLoaderStrategy(mChapterLoaderStrategy)
                .setChapterLoaderListener(new MyChapterLoadListener())
                .setParams(mArticleId,mChapterId,mChapterOrder)
                .setPageSize(UserSP.getInstance().getLastReaderPage(mArticleId))
                .setPageCount(mChapterLoaderStrategy.getCountPage(mChapterId))
                .builder();
        if(chapterLoadManager.hasLocalData() && mChapterLoaderStrategy.hasChapter(mArticleId,mChapterId)){
                return BookStatus.LOAD_SUCCESS;
        }
        chapterLoadManager.obtainChapter(mArticleId,mChapterId,mChapterOrder,FlipStatus.ON_FLIP_CUR);
            return BookStatus.LOAD_START;
    }

    @Override
    public int getCountPage() {
        return chapterLoadManager.getPageCount();
    }

    @Override
    public int getPageSize() {
        return chapterLoadManager.getPageSize();
    }


    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mTitlePaint.setAlpha(alpha);
    }


    @Override
    public boolean prePage(Canvas canvas) {
        if(null == chapterLoadManager)
            return false;
        mChapterModel = chapterLoadManager.prePage();
        if(null != mChapterModel && null != mChapterModel.getPageContent() && mChapterModel.getPageContent().size() > 0){
            onDraw(canvas);
            return true;
        }
        return false;
    }


    @Override
    public boolean curPage(Canvas canvas) {
        if(null == chapterLoadManager)
            return false;
        mChapterModel = chapterLoadManager.curPage();
        if(null != mChapterModel && null != mChapterModel.getPageContent() && mChapterModel.getPageContent().size() > 0){
            onDraw(canvas);
            return true;
        }
        return false;
    }




    @Override
    public boolean nextPage(Canvas canvas) {
         if(null == chapterLoadManager)
            return false;
        mChapterModel = chapterLoadManager.nextPage();
        if(null != mChapterModel && null != mChapterModel.getPageContent() && mChapterModel.getPageContent().size() > 0){
            onDraw(canvas);
            return true;
        }
        return false;
    }


    @Override
    public void autoIncrease(){
        if(null != chapterLoadManager)
            chapterLoadManager.autoIncrease();
    }

    @Override
    public void autoReduce() {
        if(null != chapterLoadManager)
            chapterLoadManager.autoReduce();
    }

    /**
     * 获取网络章节监听
     */
    private class MyChapterLoadListener implements OnChapterLoadStatusListener{

        @Override
        public void onPageChanged(FlipStatus status) {
            if(null != mOnReaderLoadingListener){
                mOnReaderLoadingListener.onDrawPositionPage(status);
            }
        }

        @Override
        public void onPageStatus(BookStatus bookStatus) {
            if(null != mOnReaderLoadingListener){
                mOnReaderLoadingListener.onPageStatus(bookStatus);
            }
        }

        @Override
        public void onStartLoading() {
            if(null != mOnReaderLoadingListener){
                mOnReaderLoadingListener.onStartLoading();
            }
        }

        @Override
        public void onEndLoading() {
            if(null != mOnReaderLoadingListener){
                mOnReaderLoadingListener.onEndLoading();
            }
        }
    }


    /**
     * 获取网络章节成功后的回调方法，来从新绘制直接章节内容到Bitmap上
     * @param canvas
     * @param status
     */
    @Override
    public void postInvalidatePage(Canvas canvas,FlipStatus status) {

        switch (status) {
            case ON_FLIP_PRE:
                mChapterModel = chapterLoadManager.prePage();
                if(null != mChapterModel && mChapterModel.getPageContent().size() > 0){
                    onDraw(canvas);
                }
                break;

            case ON_FLIP_CUR:
                mChapterModel = chapterLoadManager.curPage();
                if(null != mChapterModel && mChapterModel.getPageContent().size() > 0){
                    onDraw(canvas);
                }
                break;

            case ON_FLIP_NEXT:
                //获取下一章的第一页
                mChapterModel = chapterLoadManager.nextPage();
                if(null != mChapterModel && mChapterModel.getPageContent().size() > 0){
                    onDraw(canvas);
                }
                break;

            case ON_PRE_CHAPTER_LAST_PAGE:
                mChapterModel = chapterLoadManager.getPreChapterLastPage();
                if(null != mChapterModel && mChapterModel.getPageContent().size() > 0){
                    onDraw(canvas);
                }
                break;
            case ON_NEXT_CHAPTER_FIRST_PAGE:
                mChapterModel = chapterLoadManager.getNextChapterFirstPage();
                if(null != mChapterModel && mChapterModel.getPageContent().size() > 0){
                    onDraw(canvas);
                }
                break;
        }

        if(null != mOnReaderLoadingListener){
            mOnReaderLoadingListener.postInvalidatePage();
        }
    }



    /**
     * 绘制章节内容到具体 widget 的 bitmap上。
     * @param canvas  由具体的 widget (如仿真翻页组件) 传过来的 canvas。
     */
    @Override
    public synchronized void onDraw(Canvas canvas){
        if(null != mChapterModel && mChapterModel.getPageContent().size() > 0){
            int y = mMarginHeight + mNumFontSize;
            if(null == mBookPageBg){
                canvas.drawColor(Color.WHITE);
            }else{
                canvas.drawBitmap(mBookPageBg,null,rectF,null);
            }

            String titleName = mChapterModel.getChapterName();
            canvas.drawText(TextUtils.isEmpty(titleName) ? "" : titleName,mMarginWidth,y,mTitlePaint);
            canvas.drawText("书海阅读",mWidth - mMarginWidth - appNameLen,y,mTitlePaint);
            y += mLineSpace;
            canvas.drawLine(mMarginWidth, y, mWidth - mMarginWidth, y, mTitlePaint);
            y += mFontSize;
            Vector<String> lines =  mChapterModel.getPageContent();
            if(null !=  lines && lines.size() > 0){
                for (String line : lines)
                {
                    y += mLineSpace;
                    canvas.drawText(line,mMarginWidth,y,mPaint);
                    y += mFontSize;
                }
            }
            if(null != batteryBitmap){
                canvas.drawBitmap(batteryBitmap,mMarginWidth + 2, mHeight - mMarginHeight - ScreenUtils.dpToPxInt(12), mTitlePaint);
            }
//            float percent = (float) currentPage / pageCount;
            canvas.drawText( mChapterModel.getPageSize() + " / " + mChapterModel.getPageCount(),mMarginWidth, mHeight  - mMarginHeight, mTitlePaint);
//            canvas.drawText(decimalFormat.format(percent * 100) + "%",mMarginWidth, mHeight  - mMarginHeight, mTitlePaint);
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


    @Override
    public void closeBook() {
        UserSP.getInstance().setLastReaderPage(mArticleId,getPageSize());
    }
}
