package shuhai.readercore.manager;

import android.text.TextUtils;

import shuhai.readercore.Constants;
import shuhai.readercore.utils.AppUtils;
import shuhai.readercore.utils.Utils;

/**
 * 章节内容缓存加载
 * Created by 55345364 on 2017/7/4.
 */

public class ChapterLoader {

    /**
     * 凭借章节缓存 key 值，获取章节内容。
     * @param key 章节缓存 key,由书籍id 和 章节id 拼接组成,(articleId + chapterId)
     * @return 返回章节缓存内容
     */
    public static String getChapter(String key){
        String content =  null;
        content = MemoryLruCacheManager.getInstance().getString(key);
        if(!TextUtils.isEmpty(content)){
            return content;
        }
        content = DiskLruCacheManager.getInstance(Utils.getAppContext(), Constants.SHUHAIREAD_KEY).getString(key);
        if(!TextUtils.isEmpty(content)){
            return content;
        }
        return "";
    }


    /**
     * 将章节内容存入缓存中
     * @param articleId 书籍ID
     * @param chapterId 章节ID
     * @param value 章节内容
     */
    public static void put(int articleId,int chapterId, String value){
        put(String.valueOf(articleId)+String.valueOf(chapterId),value);
    }


    /**
     * 将章节内容存入缓存中
     * @param key 章节缓存 key,由书籍id 和 章节id 拼接组成,(articleId + chapterId)
     * @param value 章节内容
     */
    public static void put(String key,String value){
        if(TextUtils.isEmpty(MemoryLruCacheManager.getInstance().getString(key))){
            MemoryLruCacheManager.getInstance().put(key,value);
        }
        if(TextUtils.isEmpty(DiskLruCacheManager.getInstance(Utils.getAppContext(), Constants.SHUHAIREAD_KEY).getString(key))){
            DiskLruCacheManager.getInstance(Utils.getAppContext(), Constants.SHUHAIREAD_KEY).put(key,value);
        }
    }

}
