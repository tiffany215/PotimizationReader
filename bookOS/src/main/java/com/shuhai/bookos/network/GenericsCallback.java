package com.shuhai.bookos.network;
import com.zhy.http.okhttp.callback.Callback;
import java.lang.reflect.ParameterizedType;
import okhttp3.Response;
/**
 * 泛型参数回调接口,将解析服务端返回Json字符串，转为具体的bean对象。
 * Created by 55345364 on 2017/2/22.
 */

public abstract class GenericsCallback<T> extends Callback<T> {

    IGenericsSerializable genericsSerializable;

    public GenericsCallback(IGenericsSerializable serializable) {
        genericsSerializable = serializable;
    }


    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (entityClass == String.class) {
            return (T) string;
        }
        T bean = genericsSerializable.transform(string, entityClass);
        return bean;
    }


}
