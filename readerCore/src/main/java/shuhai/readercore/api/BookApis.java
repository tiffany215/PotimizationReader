package shuhai.readercore.api;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
                .build();
    }


    public static BookApis getInstance(){
        return BookApisHolder.INSTANCE;
    }

    private static class BookApisHolder{
        private static final BookApis INSTANCE = new BookApis();
    }



    public static <T> void getMovie(Context context, ApiCallback<T> callback){
        CommonApi bookApi = new CommonApi.Builder(context).build();
        Map<String,String> params = new HashMap<>();
        params.put("start","0");
        params.put("count","2");
        bookApi.get("",params,callback);
    }


    /**
     * 获取单章内容
     * @param callback
     * @param <T>
     */
    public <T> void obtianChapter(ApiCallback<T> callback){
        Map<String,String> params = new HashMap<>();
        params.put("packageame", "com.shuhai.bookos");
        params.put("order", "3");
        params.put("sign", "0e7dd35dac1196a3096ad3796652d8dc");
        params.put("source", "shuhai");
        params.put("uuid", "00000000-35b8-f815-39da-21960033c587");
        params.put("siteid", "300");
        params.put("timestamp", "1499765520");
        params.put("chapterorder", "6");
        params.put("username", "wangxu@qq.com");
        params.put("imei", "356261050497138");
        params.put("version", "60");
        params.put("articleid", "41366");
        params.put("ip", "192.168.1.190");
        params.put("uid", "0");
        params.put("chapterid", "2642855");
        commonApi.postMultiport("onechapter",params,callback);
    }




}
