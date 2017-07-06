package shuhai.readercore.api;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import shuhai.readercore.Constants;
import shuhai.readercore.bean.ChapterRead;
import shuhai.readercore.net.api.BookApi;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.utils.AppUtils;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class BookApis {

    private Context context;

    /**
     *
     * @param callback
     */
    public static void getChapterRead(ApiCallback<ChapterRead> callback){
        Map<String,String> params = initDefaultParams();
        params.put("","");
        params.put("","");
        params.put("","");
        params.put("","");
        BookApi bookApi = new BookApi.Builder(AppUtils.getAppContext()).build();
        bookApi.post(Constants.API_BASE_URL,params, callback);
    }


    /**
     * 初始化默认参数
     * @return
     */
    public static Map<String,String> initDefaultParams(){
        Map<String,String> params = new HashMap<>();
        params.put("","");
        params.put("","");
        params.put("","");
        params.put("","");
        return params;
    }






}
