package com.shuhai.bookos.network;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 55345364 on 2017/2/22.
 */

public class ResponseManager {


    /**
     * post 请求
     *
     * @param params   访问参数
     * @param url      访问连接
     * @param callback 异步返回结果
     */
    public static void doPost(Map<String, String> params, String url, Callback callback) {
        OkHttpUtils.post().params(addFixedParams()).params(params).url(url).build().execute(callback);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param callback
     */
    public static void doPostFile(String url, Callback callback) {
        OkHttpUtils.postFile().file(null).url(url).build().execute(callback);
    }

    /**
     * 上传字符串
     *
     * @param url
     * @param callback
     */
//    public static void doPostJson(String url, Callback callback) {
//        OkHttpUtils.postString().mediaType(MediaType.parse("application/json; charset=utf-8")).url(url).content(new Gson().toJson(new UserBean("", ""))).build().execute(callback);
//    }

    /**
     * 添加固定参数
     * @return
     */
    private static Map<String, String> addFixedParams() {
        Map<String, String> params = new HashMap<>();
        params.put("packageame", "com.shuhai.bookos");
        params.put("sign", "e98a726ae2fd27c229515428de425f59");
        params.put("source", "shuhai");
        params.put("username", "wangxu@qq.com");
        params.put("imei", "356261050497138");
        params.put("version", "56");
        params.put("uuid", "00000000-35b8-f815-39da-21960033c587");
        params.put("ip", "192.168.1.185");
        params.put("siteid", "300");
        params.put("timestamp", "1483074883");
        params.put("uid", "202533");
        return params;
    }

}
