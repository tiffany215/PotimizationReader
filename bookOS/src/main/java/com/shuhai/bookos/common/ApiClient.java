package com.shuhai.bookos.common;

import android.util.Log;

import com.shuhai.bookos.network.ResponseManager;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 55345364 on 2017/2/22.
 */

public class ApiClient {

    private final static String mBaseUrl = "http://v.juhe.cn/toutiao/index";


    /**
     * 获取新闻信息
     * @param callback
     */
    public static void getNewsInfo(Callback callback){
        Map<String,String> params = new HashMap<>();
        params.put("type","shehui");
        params.put("key","0f248a00edfbf3e7ee667ad1b0e06953");
        ResponseManager.doPost(params, mBaseUrl,callback);
    }

}
