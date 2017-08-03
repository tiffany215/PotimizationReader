package shuhai.readercore.api;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import shuhai.readercore.net.api.CommonApi;
import shuhai.readercore.net.callback.ApiCallback;
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
        Map<String,Object> params = new HashMap<>();
        params.put("packageame", "");
        params.put("sign", "cf647dda9b03998695e8436954eeba2d");
        params.put("source", "shuhai");
        params.put("uuid", "00000000-35b8-f815-39da-21960033c587");
        params.put("siteid", 300);
        params.put("timestamp", 1500432469);
        params.put("chapterorder", 18);
        params.put("username", "wangxu");
        params.put("imei", 356261050497138L);
        params.put("version", 60);
        params.put("ip", String.valueOf("192.168.1.190"));
        params.put("uid", 173434);
        params.put("order", 0);
        params.put("articleid", 41427);
        params.put("chapterid", 2668827);
        commonApi.postMultipart("onechapter",params,callback);
    }


    public <T> void obtainRecommendBook(ApiCallback<T> callback){
        Map<String,Object> params = new HashMap<>();
        params.put("packageame", "");
        params.put("sign", "cf647dda9b03998695e8436954eeba2d");
        params.put("source", "shuhai");
        params.put("uuid", "00000000-35b8-f815-39da-21960033c587");
        params.put("siteid", 300);
        params.put("timestamp", 1500432469);
        params.put("chapterorder", 18);
        params.put("username", "wangxu");
        params.put("imei", 356261050497138L);
        params.put("version", 60);
        params.put("ip", String.valueOf("192.168.1.190"));
        params.put("uid", 173434);
        params.put("order", 0);
        params.put("articleid", 41427);
        params.put("chapterid", 2668827);
        commonApi.postMultipart("bookcasebooks",params,callback);
    }



}
