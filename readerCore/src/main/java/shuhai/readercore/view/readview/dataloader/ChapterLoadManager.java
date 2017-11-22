package shuhai.readercore.view.readview.dataloader;

import android.text.TextUtils;

import shuhai.readercore.Constants;
import shuhai.readercore.api.BookApis;
import shuhai.readercore.bean.ChapterBean;
import shuhai.readercore.bean.ChapterEntity;
import shuhai.readercore.bean.MessageBean;
import shuhai.readercore.manager.ChapterLoader;
import shuhai.readercore.manager.DataBaseManager;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.utils.FastJsonUtils;
import shuhai.readercore.view.readview.displayview.OnChapterLoadStatusListener;
import shuhai.readercore.view.readview.status.BookStatus;
import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author 55345364
 * @date 2017/11/6.
 */

public class ChapterLoadManager {
    private static int mArticleId;
    private static int mChapterId;
    private static int pageCount;
    private static int pageSize;
    private static ChapterEntity mChapterEntity;
    private static ChapterLoaderStrategyImpl mChapterLoader;
    private static OnChapterLoadStatusListener chapterLoadStatusListener;
    private static ChapterDataSource dataSource;

    private ChapterLoadManager(){}

    private static class ChapterLoadManagerHolder{
        private static final ChapterLoadManager INSTANCE = new ChapterLoadManager();
    }

    public static ChapterLoadManager getInstance(){
        return ChapterLoadManagerHolder.INSTANCE;
    }


    public static final class Builder{
        private ChapterLoaderStrategyImpl mLoaderStrategy;
        private OnChapterLoadStatusListener mListener;

        public Builder(){

        }

        public ChapterLoadManager.Builder setLoaderStrategy(ChapterLoaderStrategyImpl strategy){
            this.mLoaderStrategy = strategy;
            return this;
        }

        public ChapterLoadManager.Builder setChapterLoaderListener(OnChapterLoadStatusListener listener){
            this.mListener = listener;
            return this;
        }

        public ChapterLoadManager.Builder setParams(int articleId,int chapterId,int chapterOrder){
            mArticleId = articleId;
            mChapterId = chapterId;
            mChapterEntity = DataBaseManager.getInstance().queryNextChapterInfo(Constants.CHAP_TYPE.CHAPTER,articleId,chapterOrder,FlipStatus.ON_FLIP_CUR);
            return this;
        }

        public ChapterLoadManager.Builder setPageSize(int ps){
            pageSize = ps;
            return this;
        }

        public ChapterLoadManager.Builder setPageCount(int pc){
            pageCount = pc;
            return this;
        }


        public ChapterLoadManager builder(){
            if(null == mListener){
                throw new IllegalStateException("章节加载 Listener 不能为 null");
            }

            chapterLoadStatusListener = mListener;
            if(null == mLoaderStrategy){
                throw new IllegalStateException("章节加载 mLoaderStrategy 不能为 null");
            }
            mChapterLoader = mLoaderStrategy;


            dataSource = new ChapterDataSource(mLoaderStrategy);
            if(null == dataSource){
                throw new IllegalStateException("章节加载 dataSource 不能为 null");
            }

            if(pageCount == 0){
                if(mLoaderStrategy.hasChapter(mArticleId,mChapterId)){
                   pageCount = mLoaderStrategy.getCountPage(mChapterId);
                }
            }

            if(pageSize == 0){
                pageSize = 1;
            }
            return getInstance();
        }
    }




    /**
     * 获取上一页内容
     * @return
     */
    public ChapterLoaderModel prePage() {
        if(null == mChapterEntity){
            return  null;
        }
        ChapterLoaderModel model = dataSource.getChapterInfo(mChapterEntity,pageSize - 1);
        if(null != model){
            return model;
        }
        model = getPreChapterLastPage();
        if(null != model){
            return model;
        }
        obtainChapter(mChapterEntity.getArticleId(),mChapterEntity.getChapterId(),mChapterEntity.getChapterOrder(),FlipStatus.ON_PRE_CHAPTER_LAST_PAGE);
        return null;

    }


    /**
     * 获取当前页内容
     * @return
     */

    public ChapterLoaderModel curPage() {
        if(pageSize > pageCount ){
            mChapterEntity = DataBaseManager.getInstance().queryNextChapterInfo(Constants.CHAP_TYPE.CHAPTER,mChapterEntity.getArticleId(),mChapterEntity.getChapterOrder(),FlipStatus.ON_FLIP_NEXT);
            if(null != mChapterEntity){
                if(mChapterLoader.hasChapter(mChapterEntity.getArticleId(),mChapterEntity.getChapterId())){
                    pageSize = 1;
                    pageCount = mChapterLoader.getCountPage(mChapterEntity.getChapterId());
                    UserSP.getInstance().setLastReaderChapterId(mArticleId,mChapterEntity.getChapterId());
                    UserSP.getInstance().setLastReaderChapterOrder(mArticleId,mChapterEntity.getChapterOrder());
                }else{
                    return null;
                }
            }else{
                return null;
            }

        }else if(pageSize < 1 ){
            mChapterEntity = DataBaseManager.getInstance().queryNextChapterInfo(Constants.CHAP_TYPE.CHAPTER,mChapterEntity.getArticleId(),mChapterEntity.getChapterOrder(),FlipStatus.ON_FLIP_PRE);
            if(null != mChapterEntity){
                if(null != mChapterEntity) {
                    pageSize = mChapterLoader.getCountPage(mChapterEntity.getChapterId());
                    pageCount = mChapterLoader.getCountPage(mChapterEntity.getChapterId());
                    UserSP.getInstance().setLastReaderChapterId(mArticleId,mChapterEntity.getChapterId());
                    UserSP.getInstance().setLastReaderChapterOrder(mArticleId,mChapterEntity.getChapterOrder());
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }
        ChapterLoaderModel model = dataSource.getChapterInfo(mChapterEntity,pageSize);
        if(null != model){
            return model;
        }else{
            obtainChapter(mChapterEntity.getArticleId(),mChapterEntity.getChapterId(),mChapterEntity.getChapterOrder(),FlipStatus.ON_FLIP_CUR);
        }
        return null;
    }

    /**
     * 获取下一页内容
     * @return
     */
    public ChapterLoaderModel nextPage() {
        if(null == mChapterEntity){
            return  null;
        }
        ChapterLoaderModel model = dataSource.getChapterInfo(mChapterEntity,pageSize + 1);
        if(null != model){
            return model;
        }
        model = getNextChapterFirstPage();
        if(null != model){
            return model;
        }
        obtainChapter(mChapterEntity.getArticleId(),mChapterEntity.getChapterId(),mChapterEntity.getChapterOrder(),FlipStatus.ON_NEXT_CHAPTER_FIRST_PAGE);
        return null;
    }


    /**
     *
     * @return
     */
    public ChapterLoaderModel getNextChapterFirstPage() {
        ChapterEntity entity = DataBaseManager.getInstance().queryNextChapterInfo(Constants.CHAP_TYPE.CHAPTER,mArticleId,mChapterEntity.getChapterOrder(), FlipStatus.ON_FLIP_NEXT);
        if (null == entity){
            return null;
        }

        ChapterLoaderModel model = dataSource.getChapterInfo(entity,1);
        if(null != model){
            return model;
        }
        return null;
    }


    /**
     *
     * @return
     */
    public ChapterLoaderModel getPreChapterLastPage() {
        ChapterEntity entity = DataBaseManager.getInstance().queryNextChapterInfo(Constants.CHAP_TYPE.CHAPTER,mArticleId,mChapterEntity.getChapterOrder(), FlipStatus.ON_FLIP_PRE);
        if(null == entity){
            return  null;
        }
        ChapterLoaderModel model = dataSource.getChapterInfo(entity,mChapterLoader.getCountPage(entity.getChapterId()));
        if(null != model){
            return model;
        }
        return null;
    }


    public boolean hasLocalData(){
       if(null != mChapterEntity){
            return true;
       }
        return false;
    }


    public void autoIncrease() {
        pageSize ++;
    }

    public void autoReduce() {
        pageSize --;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int ps){
        pageSize = ps;
    }


    public synchronized void obtainChapter(final int articleId, final int chapterId, final int chapterOrder, final FlipStatus status){

        BookApis.getInstance().obtainChapter(articleId, chapterId, chapterOrder, status, new ApiCallback<MessageBean>() {

            @Override
            public void onStart() {

                chapterLoadStatusListener.onStartLoading();
            }

            @Override
            public void onComplete() {
                chapterLoadStatusListener.onEndLoading();
                if(status == FlipStatus.ON_FLIP_CUR){
                    if(null != prePage()){
                        chapterLoadStatusListener.onPageChanged(FlipStatus.ON_FLIP_PRE);
                    }
                    if(null != nextPage()){
                        chapterLoadStatusListener.onPageChanged(FlipStatus.ON_FLIP_NEXT);
                    }
                }
            }

            @Override
            public void onError(ApiException e)
            {
                chapterLoadStatusListener.onEndLoading();
            }

            @Override
            public void onNext(MessageBean messageBean)  {
                String code = messageBean.getCode();

                switch (code) {
                    case Constants.RESPONSE_CODE.LOAD_SUCCESS:
                        storageChapter(messageBean, articleId, status);
                        break;

                    case Constants.RESPONSE_CODE.LOAD_FAILURE:
                        chapterLoadStatusListener.onPageStatus(BookStatus.LOAD_ERROR);
                        break;

                    case Constants.RESPONSE_CODE.NO_PRE_PAGE:
                        chapterLoadStatusListener.onPageStatus(BookStatus.NO_PRE_PAGE);
                        break;
                    default:
                        chapterLoadStatusListener.onPageStatus(BookStatus.LOAD_ERROR);
                        break;
                }


            }
        });
    }

    private void storageChapter(MessageBean messageBean, int articleId, FlipStatus status) {
        ChapterBean chapterBean = FastJsonUtils.textToJson(messageBean.getMessage(),ChapterBean.class);
        if(null != chapterBean && null != chapterBean.getList() && chapterBean.getList().size() > 0){
            ChapterEntity chapterEntity = chapterBean.getList().get(0);
            chapterEntity.setArticleId(articleId);
            String chapterStr = chapterEntity.getContent();
            if(!TextUtils.isEmpty(chapterStr)){
                ChapterLoader.put(articleId,chapterEntity.getChapterId(),chapterStr);
            }
            DataBaseManager.getInstance().insertChapterInfo(chapterEntity);

            //网络获取章节并成功的缓存到本地 则通知PageFactory重新在缓存中读取章节内容并绘制到
            // 内容到Bitmap上，然后读取后通知View刷新页面。
            //失败则通知View获取内容失败，Toast相关提示
            if(mChapterLoader.hasChapter(articleId,chapterEntity.getChapterId())){
                //如果是当前章节，则更新全局章节信息
                if(status == FlipStatus.ON_FLIP_CUR){
                    pageCount = mChapterLoader.getCountPage(chapterEntity.getChapterId());
                    UserSP.getInstance().setLastReaderChapterId(articleId,chapterEntity.getChapterId());
                    UserSP.getInstance().setLastReaderChapterOrder(articleId,chapterEntity.getChapterOrder());
                    mChapterEntity = DataBaseManager.getInstance().queryNextChapterInfo(Constants.CHAP_TYPE.CHAPTER,articleId,chapterEntity.getChapterOrder(),FlipStatus.ON_FLIP_CUR);
                }
                    chapterLoadStatusListener.onPageChanged(status);
            }else{
                chapterLoadStatusListener.onPageStatus(BookStatus.LOAD_ERROR);
            }
        }
    }


}
