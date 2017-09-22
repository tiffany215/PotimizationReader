package shuhai.readercore.ui.presenter;

import android.text.TextUtils;
import android.util.Log;

import javax.inject.Inject;

import shuhai.readercore.api.BookApis;
import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.bean.ChapterEntity;
import shuhai.readercore.manager.ChapterLoader;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;
import shuhai.readercore.ui.contract.BookReadContract;
import shuhai.readercore.ui.sharedp.UserSP;

import static android.content.ContentValues.TAG;

/**
 * @author wangxu
 * @date 2017/7/5.
 */

public class BookReadPresenter extends RxPresenter<BookReadContract.View> implements BookReadContract.Presenter<BookReadContract.View>{

    @Inject
    public BookReadPresenter(){

    }


    @Override
    public void getChapterRead(final int articleId, final int chapterId,int chapterOrder,int flipMark) {

        BookApis.getInstance().obtainChapter(articleId,chapterId,chapterOrder,flipMark,new ApiCallback<ChapterEntity>() {


            @Override
            public void onStart() {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onNext(ChapterEntity entity) {
               if(null != entity && null != entity.getMessage() && null != mView){
                   UserSP.getInstance().setLastReaderChapterOrder(Integer.parseInt(entity.getMessage().get(0).getChapterorder().trim()));
                   String chapterStr = entity.getMessage().get(0).getContent();
                   Log.e(TAG, "onNext: "+ chapterStr );
                   if(!TextUtils.isEmpty(chapterStr)){
                       ChapterLoader.put(articleId,chapterId,chapterStr);
                   }
                   mView.showChapterRead();
               }
            }
        });
    }

    @Override
    public void getBookMixAToc() {
        mView.showBookToc();
    }
}
