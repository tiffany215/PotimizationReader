package shuhai.readercore.api;


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


    /**
     * 获取单章内容
     * @param callback
     * @param <T>
     */
    public <T> void obtainChapter(ApiCallback<T> callback){
        Map<String,String> params = new HashMap<>();
        params.put("packageame", "com.shuhai.bookos");
        params.put("order", "2");
        params.put("sign", "812e84d3004554fa28da53be24386e7f");
        params.put("source", "shuhai");
        params.put("uuid", "00000000-35b8-f815-39da-21960033c587");
        params.put("siteid", "300");
        params.put("timestamp", "1499913428");
        params.put("chapterorder", "1");
        params.put("username", "wangxu@qq.com");
        params.put("imei", "356261050497138");
        params.put("version", "60");
        params.put("articleid", "41391");
        params.put("ip", "192.168.1.190");
        params.put("uid", "0");
        params.put("chapterid", "2648720");
        commonApi.postMultipart("onechapter",params,callback);
    }




}
