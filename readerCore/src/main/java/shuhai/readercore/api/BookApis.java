package shuhai.readercore.api;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import shuhai.readercore.Constants;
import shuhai.readercore.bean.ChapterRead;
import shuhai.readercore.net.api.BookApi;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.subscriber.ApiCallbackSubscriber;
import shuhai.readercore.utils.AppUtils;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class BookApis {


    public static <T> void getMovie(Context context, ApiCallback<T> callback){
        BookApi bookApi = new BookApi.Builder(context).build();
        Map<String,String> params = new HashMap<>();
        params.put("start","0");
        params.put("count","2");
        bookApi.get("",params,callback);
    }


}
