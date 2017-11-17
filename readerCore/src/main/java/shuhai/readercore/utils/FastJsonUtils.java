package shuhai.readercore.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

import shuhai.readercore.bean.BookBean;

/**
 * @author 55345364
 * @date 2017/11/15.
 */

public class FastJsonUtils {



    public static Object toBean(String text) {
        return JSON.parse(text);
    }

    public static <T> T toBean(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    // 转换为数组
    public static <T> Object[] toArray(String text) {
        return toArray(text, null);
    }

    // 转换为数组
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }

    // 转换为List
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }


    /**
     * 将string转化为序列化的json字符串
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T textToJson(String str,Class<T> clazz){
        return toBean("{"+ "\"list\": "+ str + "}",clazz);
    }




    /**
     * 将string转化为序列化的json字符串
     * @param text
     * @return
     */
    public static Object textToJson(String text) {
        Object objectJson  = JSON.parse(text);
        return objectJson;
    }

    /**
     * json字符串转化为map
     * @param s
     * @return
     */
    public static Map stringToCollect(String s) {
        Map m = JSONObject.parseObject(s);
        return m;
    }

    /**
     * 将map转化为string
     * @param m
     * @return
     */
    public static String collectToString(Map m) {
        String s = JSONObject.toJSONString(m);
        return s;
    }

}
