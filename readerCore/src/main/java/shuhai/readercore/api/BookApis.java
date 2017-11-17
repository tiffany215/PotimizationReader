package shuhai.readercore.api;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import shuhai.readercore.net.api.CommonApi;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.utils.AppUtils;
import shuhai.readercore.utils.TimeFromatUtile;
import shuhai.readercore.utils.Utils;
import shuhai.readercore.view.readview.status.FlipStatus;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class BookApis {

    private CommonApi commonApi;

    private BookApis(){
        commonApi = new CommonApi.Builder(Utils.getAppContext())
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



    private RequestBody formatParams(Object params){
        return RequestBody.create(null,String.valueOf(params));
    }


    /**
     * 获取单章内容
     * @param callback
     * @param <T>
     */
    public <T> void obtainChapter(final int articleId, final int chapterId, int chapterOrder, FlipStatus status, ApiCallback<T> callback){
        Map<String, RequestBody> params = new HashMap<>();
        String systemTime = String.valueOf(TimeFromatUtile.getCurrentDateTimeSeconds());
        params.put("packagename", formatParams("com.shuhai.bookos"));
        params.put("sign",  formatParams("cf647dda9b03998695e8436954eeba2d"));
        params.put("source",  formatParams("shuhai"));
        params.put("uuid",  formatParams("00000000-35b8-f815-39da-21960033c587"));
        params.put("siteid",  formatParams("300"));
        params.put("timestamp", formatParams(systemTime));
        params.put("chapterorder", formatParams(chapterOrder));
        params.put("username", formatParams("wangxu"));
        params.put("imei", formatParams("356261050497138L"));
        params.put("version", formatParams(60));
        params.put("ip", formatParams("192.168.1.190"));
        params.put("uid", formatParams("173434"));
        switch (status) {
            case ON_FLIP_PRE:
                params.put("order", formatParams(1));
                break;
            case ON_FLIP_CUR:
                params.put("order", formatParams(0));
                break;
            case ON_FLIP_NEXT:
                params.put("order", formatParams(2));
                break;
            case ON_NEXT_CHAPTER_FIRST_PAGE:
                params.put("order", formatParams(2));
                break;

            case ON_PRE_CHAPTER_LAST_PAGE:
                params.put("order", formatParams(1));
                break;

        }
        params.put("articleid", formatParams(articleId));
        params.put("chapterid", formatParams(chapterId));
        commonApi.postMultipart("onechapter",params,callback);
    }


    public <T> void obtainRecommendBook(int sex,int pagesize,ApiCallback<T> callback){
        String systemTime = String.valueOf(TimeFromatUtile.getCurrentDateTimeSeconds());
        Map<String,RequestBody> params = new HashMap<>();
        params.put("packagename", formatParams("com.shuhai.bookos"));
        params.put("sign", formatParams("2416e3a386248cc0b6d01754972591e7"));
        params.put("source", formatParams("shuhai"));
        params.put("uuid", formatParams("00000000-35b8-f815-39da-21960033c587"));
        params.put("siteid", formatParams(300));
        params.put("timestamp", formatParams(systemTime));
//        params.put("chapterorder", formatParams(18));
        params.put("username", formatParams("wangxu"));
        params.put("imei", formatParams(356261050497138L));
        params.put("version", formatParams(64));
        params.put("ip", formatParams("192.168.2.47"));
        params.put("uid", formatParams(173434));
        params.put("sex", formatParams(sex));
        params.put("pagesize", formatParams(pagesize));
        commonApi.postMultipart("bookcasebooks",params,callback);
    }







}
