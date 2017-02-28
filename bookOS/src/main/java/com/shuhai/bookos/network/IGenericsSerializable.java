package com.shuhai.bookos.network;

/**
 * Json序列化接口
 * Created by 55345364 on 2017/2/27.
 */

public interface IGenericsSerializable {

    <T> T transform(String response,Class<T> classOft);

}
