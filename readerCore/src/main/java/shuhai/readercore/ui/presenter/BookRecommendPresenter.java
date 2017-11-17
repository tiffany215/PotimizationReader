package shuhai.readercore.ui.presenter;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import shuhai.readercore.api.BookApis;
import shuhai.readercore.base.RxPresenter;
//import shuhai.readercore.bean.BookInfoEntity;
////import shuhai.readercore.manager.DataBaseManager;
import shuhai.readercore.bean.BookBean;
import shuhai.readercore.bean.BookEntity;
import shuhai.readercore.bean.MessageBean;
import shuhai.readercore.manager.DataBaseManager;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;
import shuhai.readercore.ui.contract.BookRecommendContract;
import shuhai.readercore.ui.sharedp.UserSP;
import shuhai.readercore.utils.FastJsonUtils;

/**
 * @author 55345364
 * @date 2017/8/3.
 */

public class BookRecommendPresenter extends RxPresenter<BookRecommendContract.View> implements BookRecommendContract.Presenter<BookRecommendContract.View> {


    private static final String TAG = "BookRecommendPresenter";

    @Override
    public void getRecommendBook(int sex,int pageSize) {

        BookApis.getInstance().obtainRecommendBook(sex,pageSize,new ApiCallback<MessageBean>() {

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
            public void onNext(MessageBean messageBean) {
                BookBean bookBean  = FastJsonUtils.textToJson(messageBean.getMessage(),BookBean.class);
                if(null != bookBean && null != bookBean.getList() && bookBean.getList().size() > 0){
                    for (int i = 0; i < bookBean.getList().size(); i++) {
                        DataBaseManager.getInstance().insertBookInfo(bookBean.getList().get(i));
                    }
                    UserSP.getInstance().setRecommendStatue(true);
                }

                mView.postView();
            }
        });
    }
}
