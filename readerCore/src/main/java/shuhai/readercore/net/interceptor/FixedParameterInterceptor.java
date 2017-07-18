package shuhai.readercore.net.interceptor;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import shuhai.readercore.utils.StringUtils;

import static android.content.ContentValues.TAG;

/**
 * @author 55345364
 * @date 2017/7/18.
 */

public class FixedParameterInterceptor implements Interceptor {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Gson mGson;


    public FixedParameterInterceptor(){
        mGson = new Gson();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String method = request.method();
        try {
            //公共参数
            HashMap<String, String> commonParamsMap = new HashMap<>();
            commonParamsMap.put("packageame", "com.shuhai.bookos");
            commonParamsMap.put("sign", "812e84d3004554fa28da53be24386e7f");
            commonParamsMap.put("source", "shuhai");
            commonParamsMap.put("uuid", "00000000-35b8-f815-39da-21960033c587");
            commonParamsMap.put("siteid", "300");
            commonParamsMap.put("timestamp", "1499913428");
            commonParamsMap.put("username", "wangxu@qq.com");
            commonParamsMap.put("imei", "356261050497138");
            commonParamsMap.put("version", "60");
            commonParamsMap.put("ip", "192.168.1.190");
            commonParamsMap.put("uid", "0");
            if ("GET".equals(method)) {
                HashMap<String, Object> rootMap = new HashMap<>();
                HttpUrl mHttpUrl = request.url();
                Set<String> paramNames = mHttpUrl.queryParameterNames();
//                for (String key : paramNames) {
//                    if (Constant.PARAM.equals(key)) {
//                        String oldParamJson = mHttpUrl.queryParameter(Constant.PARAM);
//                        //原始参数转hashmap
//                        if (oldParamJson != null) {
//                            HashMap<String, Object> p = mGson.fromJson(oldParamJson, HashMap.class);
//                            if (p != null) {
//                                for (Map.Entry<String, Object> entry : p.entrySet()) {
//                                      rootMap.put(entry.getKey(), entry.getValue());
//                                }
//                            }
//                        }
//                    } else {
//                        rootMap.put(key, mHttpUrl.queryParameter(key));
//                    }
//                }
                //加上公共参数 hashmap
                rootMap.put("publicParams", commonParamsMap);
                //新的json参数
                String newParamJson = mGson.toJson(rootMap);
                String url = mHttpUrl.toString();
                int index = url.indexOf("?");
                if (index > 0) {
                    url = url.substring(0, index);
                }
//                url = url + "?" + Constant.PARAM + "=" + newParamJson;

                //重新构建请求
                request = request.newBuilder().url(url).build();
            } else if ("POST".equals(method)) {
//                HashMap<String, Object> rootMap = new HashMap<>();
                RequestBody body = request.body();
                String newJsonParams;
                if (body instanceof FormBody) {
                    for (int i = 0; i < ((FormBody) body).size(); i++) {
                        commonParamsMap.put(((FormBody) body).encodedName(i), ((FormBody) body).encodedValue(i));
                    }
                }else if(body instanceof MultipartBody){
                    for (int i = 0; i <((MultipartBody)body).size() ; i++) {
                        commonParamsMap.put("" + i,((MultipartBody)body).part(i).toString());
                    }

                }else {
//                    Buffer buffer = new Buffer();
//                    body.writeTo(buffer);
//                    String oldJsonParams = buffer.readUtf8();
//                    commonParamsMap = mGson.fromJson(oldJsonParams, HashMap.class); // 原始参数
//                    commonParamsMap.put("publicParams", commonParamsMap); // 重新组装
//                    String newJsonParams = mGson.toJson(commonParamsMap); // {"page":0,"publicParams":{"imei":'xxxxx',"sdk":14,.....}}
//                    request = request.newBuilder().post(RequestBody.create(JSON, newJsonParams)).build();
                }
                newJsonParams = mGson.toJson(commonParamsMap);
                request = request.newBuilder().post(RequestBody.create(JSON,newJsonParams)).build();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return chain.proceed(request);
    }
}
