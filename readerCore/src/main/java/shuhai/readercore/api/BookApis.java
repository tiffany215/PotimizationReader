package shuhai.readercore.api;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import shuhai.readercore.net.api.CommonApi;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.mode.ApiCode;
import shuhai.readercore.utils.AppUtils;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class BookApis {

    private CommonApi commonApi;

    private BookApis(){
        commonApi = new CommonApi.Builder(AppUtils.getAppContext())
                .connectionTimeout(10, TimeUnit.SECONDS)
                .writeTimeOut(10,TimeUnit.SECONDS)
                .readTimeOut(10,TimeUnit.SECONDS)
                .addLogInterceptor(true, HttpLoggingInterceptor.Level.BODY)
                .build();
    }


    public static BookApis getInstance(){
        return BookApisHolder.INSTANCE;
    }

    private static class BookApisHolder{
        private static final BookApis INSTANCE = new BookApis();
    }


    /**
     * 获取单章内容
     * @param callback
     * @param <T>
     */
    public <T> void obtainChapter(ApiCallback<T> callback){
        Map<String,String> params = new HashMap<>();
//        params.put("packageame", "com.shuhai.bookos");
//        params.put("sign", "812e84d3004554fa28da53be24386e7f");
//        params.put("source", "shuhai");
//        params.put("uuid", "00000000-35b8-f815-39da-21960033c587");
//        params.put("siteid", "300");
//        params.put("timestamp", "1499913428");
//        params.put("chapterorder", "1");
//        params.put("username", "wangxu@qq.com");
//        params.put("imei", "356261050497138");
//        params.put("version", "60");
//        params.put("ip", "192.168.1.190");
//        params.put("uid", "0");
        params.put("order", "2");
        params.put("articleid", "41391");
        params.put("chapterid", "2648720");
        params.put("chapterorder", "1");
        commonApi.postMultipart("onechapter",params,callback);
    }

}
