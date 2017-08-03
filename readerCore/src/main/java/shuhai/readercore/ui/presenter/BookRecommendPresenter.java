package shuhai.readercore.ui.presenter;
import shuhai.readercore.api.BookApis;
import shuhai.readercore.base.RxPresenter;
import shuhai.readercore.bean.BookInfoEntity;
import shuhai.readercore.manager.DataBaseManager;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;
import shuhai.readercore.ui.contract.BookRecommendContract;

/**
 * @author 55345364
 * @date 2017/8/3.
 */

public class BookRecommendPresenter extends RxPresenter<BookRecommendContract.View> implements BookRecommendContract.Presenter<BookRecommendContract.View> {


    private static final String TAG = "BookRecommendPresenter";

    @Override
    public void getRecommendBook(int sex,int pageSize) {

        BookApis.getInstance().obtainRecommendBook(sex,pageSize,new ApiCallback<BookInfoEntity>() {

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
            public void onNext(BookInfoEntity bookInfoEntity) {
                //将推荐书籍存入库中
                if(null != bookInfoEntity && null != bookInfoEntity.getMessage()){
                    for (int i = 0; i < bookInfoEntity.getMessage().size(); i++) {
                        DataBaseManager.getInstance().insertBookInfo(bookInfoEntity.getMessage().get(i));
                    }
                }
            }
        });



    }
}
