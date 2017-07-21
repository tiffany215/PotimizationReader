package shuhai.readercore.ui.presenter;

import android.text.TextUtils;

import javax.inject.Inject;

import shuhai.readercore.api.BookApis;
import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.bean.ChapterEntity;
import shuhai.readercore.manager.ChapterLoader;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;
import shuhai.readercore.ui.contract.BookReadContract;

/**
 * @author wangxu
 * @date 2017/7/5.
 */

public class BookReadPresenter extends RxPresenter<BookReadContract.View> implements BookReadContract.Presenter<BookReadContract.View>{

    @Inject
    public BookReadPresenter(){

    }


    @Override
    public void getChapterRead(final int articleId, final int chapterId) {

        BookApis.getInstance().obtainChapter(new ApiCallback<ChapterEntity>() {


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
                   String chapterStr = entity.getMessage().get(0).getContent();
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
