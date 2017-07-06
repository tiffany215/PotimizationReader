package shuhai.readercore.net.func;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;
import shuhai.readercore.net.exception.ApiException;

/**
 * @author 55345364
 * @date 2017/7/6.
 * Throwable转Observable<T>
 */

public class ApiErrorFunc<T> implements Func1<Throwable, Observable<T>> {
    @Override
    public Observable<T> call(Throwable throwable) {
        return Observable.error(ApiException.handleException(throwable));
    }
}
