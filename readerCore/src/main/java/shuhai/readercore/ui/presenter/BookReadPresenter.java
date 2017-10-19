package shuhai.readercore.ui.presenter;

import android.text.TextUtils;
import android.util.Log;

import javax.inject.Inject;

import shuhai.readercore.api.BookApis;
import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.bean.ChapterEntity;
import shuhai.readercore.manager.ChapterLoader;
import shuhai.readercore.manager.DataBaseManager;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;
import shuhai.readercore.ui.contract.BookReadContract;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.view.readview.FlipStatus;

import static android.content.ContentValues.TAG;

/**
 * @author wangxu
 * @date 2017/7/5.
 */

public class BookReadPresenter extends RxPresenter<BookReadContract.View> implements BookReadContract.Presenter<BookReadContract.View>{



    public static final String TAG = "BookReadPresenter";
    @Inject
    public BookReadPresenter(){

    }


    @Override
    public void getChapterRead(final int articleId, final int chapterId, int chapterOrder, final FlipStatus status) {

        BookApis.getInstance().obtainChapter(articleId,chapterId,chapterOrder,status,new ApiCallback<ChapterEntity>() {


            @Override
            public void onStart() {

                Log.e(TAG, "onStart: ----------------->");

            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ----------------->");
            }

            @Override
            public void onError(ApiException e) {
                Log.e(TAG, "onError: ----------------->");
                mView.showError();
            }

            @Override
            public void onNext(ChapterEntity entity) {
               if(null != entity && null != entity.getMessage() && null != mView){
                   UserSP.getInstance().setLastReaderChapterId(articleId,Integer.parseInt(entity.getMessage().get(0).getChapterid().trim()));
                   UserSP.getInstance().setLastReaderChapterOrder(articleId,Integer.parseInt(entity.getMessage().get(0).getChapterorder().trim()));
                   String chapterStr = entity.getMessage().get(0).getContent();
                   Log.e(TAG, "onNext: "+ chapterStr );
                   if(!TextUtils.isEmpty(chapterStr)){
                       ChapterLoader.put(articleId,Integer.parseInt(entity.getMessage().get(0).getChapterid().trim()),chapterStr);
                   }
                   if(null != entity && null != entity.getMessage()){
                       for (int i = 0; i < entity.getMessage().size(); i++) {
                           shuhai.readercore.dao.ChapterEntity chapterInfo = new shuhai.readercore.dao.ChapterEntity();
                           chapterInfo.setArticleid(articleId);
                           chapterInfo.setChpid(Long.parseLong(entity.getMessage().get(i).getChapterid().trim()));
                           chapterInfo.setChpnamme(entity.getMessage().get(i).getChaptername());
                           chapterInfo.setChptype(2);
                           chapterInfo.setChiporder(Integer.parseInt(entity.getMessage().get(i).getChapterorder().trim()));
                           DataBaseManager.getInstance().insertChapterInfo(chapterInfo);
                       }
                   }
                   mView.showChapterRead(Integer.parseInt(entity.getMessage().get(0).getChapterid().trim()),status);
               }
            }
        });
    }

    @Override
    public void getBookMixAToc() {
        mView.showBookToc();
    }
}
