package shuhai.readercore.net.func;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * @author 55345364
 * @date 2017/7/6.
 *
 * 将请求返回参数转换成json格式。
 */

public class ApiFunc<T> implements Func1<ResponseBody,T>{

    protected Class<T> mClazz;

    public ApiFunc(Class<T> clazz){
        this.mClazz = clazz;
    }

    @Override
    public T call(ResponseBody responseBody) {
        Gson gson = new Gson();
        String json = null;
        try {
            json = responseBody.string();
            if(mClazz.equals(String.class)){
                return (T) json;
            }else{
                return gson.fromJson(json,mClazz);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            responseBody.close();
        }
        return (T) json;
    }
}
