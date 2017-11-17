package shuhai.readercore.net.func;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import okhttp3.ResponseBody;
import rx.functions.Func1;

import static android.content.ContentValues.TAG;

/**
 * @author 55345364
 * @date 2017/7/6.
 *
 * 将请求返回参数转换成json格式。
 */

public class ApiFunc<T> implements Func1<ResponseBody,T>{

    protected Class<T> mClazz;

    public ApiFunc(Class<T> clazz){
        this.mClazz = clazz;
    }

    @Override
    public T call(ResponseBody responseBody) {
        String json = null;
        try {
            json = responseBody.string();
            if(mClazz.equals(String.class)){
                return (T) json;
            }else{


//              Object  object =  JSON.parseObject(json,mClazz);
//
//
//                Log.e(TAG, "call: " + object.toString() );

//                JSONObject object = JSON.parseObject(json);
//
//                String string = (String) object.get("code");
////
//               JSONArray jsonArray = object.getJSONArray("message");


                return JSON.parseObject(json,mClazz);

//                JSONToken jsonToken = new JSONToken();
//
//                BookEntity entity = (BookEntity) gson.fromJson(json,mClazz);
//
//                return gson.fromJson(json,mClazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            responseBody.close();
        }
        return (T) json;
    }
}
