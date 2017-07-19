package shuhai.readercore.net.subscriber;

import android.content.Context;

import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class ApiCallbackSubscriber<T> extends ApiSubscriber<T> {

    protected ApiCallback<T> apiCallback;

    public ApiCallbackSubscriber(Context context, ApiCallback<T> callback) {
        super(context);
        if(null == callback){
            throw new NullPointerException("this callback is null!");
        }
        this.apiCallback = callback;
    }

    @Override
    public void onStart() {
        super.onStart();
        apiCallback.onStart();
    }

    @Override
    public void onError(ApiException e) {
        apiCallback.onError(e);
    }

    @Override
    public void onCompleted() {
        apiCallback.onComplete();
    }

    @Override
    public void onNext(T t) {
        apiCallback.onNext(t);
    }
}
