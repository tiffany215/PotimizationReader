package shuhai.readercore.manager;

import android.text.TextUtils;

import shuhai.readercore.cache.DiskCache;
import shuhai.readercore.cache.MemoryCache;
import shuhai.readercore.utils.StringUtils;
import shuhai.readercore.utils.Utils;

/**
 * 章节内容缓存加载
 * Created by 55345364 on 2017/7/4.
 */

public class ChapterCacheManager {


    private  DiskCache diskLruCache;
    private  MemoryCache memoryLruCache;

    private ChapterCacheManager(){
        diskLruCache = new DiskCache(Utils.getAppContext());
        memoryLruCache = MemoryCache.getInstance();
    }

    public static ChapterCacheManager getInstance(){
        return CacheHolder.INSTANCE;
    }

    private static class CacheHolder{
        public static final ChapterCacheManager INSTANCE =  new ChapterCacheManager();
    }


    /**
     * 凭借章节缓存 key 值，获取章节内容。
     * @param key 章节缓存 key,由书籍id 和 章节id 拼接组成,(articleId + chapterId)
     * @return 返回章节缓存内容
     */
    public String getChapter(String key){
        String content =  null;
        content = memoryLruCache.get(key) != null ? memoryLruCache.get(key).toString():"";
        if(!TextUtils.isEmpty(content)){
            return content;
        }
        content = diskLruCache.get(key);
        if(!TextUtils.isEmpty(content)){
            memoryLruCache.put(key,content);
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
    public void put(int articleId,int chapterId, String value){
        put(StringUtils.cacheKeyCreate(articleId,chapterId),value);
    }


    /**
     * 将章节内容存入缓存中
     * @param key 章节缓存 key,由书籍id 和 章节id 拼接组成,(articleId + chapterId)
     * @param value 章节内容
     */
    public void put(String key,String value){
        if(TextUtils.isEmpty(memoryLruCache.get(key,String.class))){
            memoryLruCache.put(key,value);
        }
        if(TextUtils.isEmpty(diskLruCache.get(key))){
            diskLruCache.put(key,value);
        }
    }


    /**
     * 判断时候有缓存内容
     * @param key
     * @return
     */
    public boolean contains(String key){
        if(memoryLruCache.contains(key) && diskLruCache.contains(key)){
            return true;
        }
            return false;
    }
}
