package shuhai.readercore.net.callback;

import shuhai.readercore.net.exception.ApiException;

/**
 * @author 55345364
 * @date 2017/7/6.
 * API 操作的回调
 */

public abstract class ApiCallback<T> {

    public abstract void onStart();

    public abstract void onComplete();

    public abstract void onError(ApiException e);

    public abstract void onNext(T t);


}
