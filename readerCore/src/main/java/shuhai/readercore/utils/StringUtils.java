package shuhai.readercore.utils;

import okhttp3.RequestBody;
import okio.Buffer;

/**
 * @author 55345364
 * @date 2017/7/18.
 */

public class StringUtils {


    /**
     * 将requestBody 转为 String
     * @param requestBody
     * @return
     */
    public static String bodyToString(final RequestBody requestBody){
        try {
            RequestBody  copy = requestBody;
            Buffer buffer = new Buffer();
            if(null != copy){
                copy.writeTo(buffer);
            }else{
                return "";
            }
            return buffer.readUtf8();

        } catch (Exception e) {
            return "did not work";
        }
    }
}
