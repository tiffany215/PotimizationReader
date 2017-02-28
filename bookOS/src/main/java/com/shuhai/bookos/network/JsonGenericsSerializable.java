package com.shuhai.bookos.network;
import com.google.gson.Gson;
/**
 * 将Json序列化
 * Created by 55345364 on 2017/2/27.
 */

public class JsonGenericsSerializable implements IGenericsSerializable {
    Gson mGson = new Gson();
    @Override
    public <T> T transform(String response, Class<T> classOft) {
        return mGson.fromJson(response,classOft);
    }
}
