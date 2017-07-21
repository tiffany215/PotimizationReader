package shuhai.readercore.manager;

import android.text.TextUtils;

import shuhai.readercore.Constants;
import shuhai.readercore.utils.AppUtils;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class ChapterLoader {
    public static String getChapter(String key){
        String content =  null;
        content = MemoryLruCacheManager.getInstance().getString(key);
        if(!TextUtils.isEmpty(content)){
            return content;
        }
        content = DiskLruCacheManager.getInstance(AppUtils.getAppContext(), Constants.SHUHAIREAD_KEY).getString(key);
        if(!TextUtils.isEmpty(content)){
            return content;
        }
        return null;
    }


    public static void put(int articleId,int chapterId,String value){
        put(String.valueOf(articleId)+String.valueOf(chapterId),value);
    }



    public static void put(String key,String value){
        if(TextUtils.isEmpty(MemoryLruCacheManager.getInstance().getString(key))){
            MemoryLruCacheManager.getInstance().put(key,value);
        }
        if(TextUtils.isEmpty(DiskLruCacheManager.getInstance(AppUtils.getAppContext(), Constants.SHUHAIREAD_KEY).getString(key))){
            DiskLruCacheManager.getInstance(AppUtils.getAppContext(), Constants.SHUHAIREAD_KEY).put(key,value);
        }
    }

}
