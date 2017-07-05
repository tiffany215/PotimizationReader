package shuhai.readercore.manager;

import android.support.v4.util.LruCache;

import shuhai.readercore.Constants;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class MemoryLruCacheManager {
    private LruCache<String,String> mLruCache = null;
    private MemoryLruCacheManager(){
        mLruCache = new LruCache<String,String>(Constants.MAX_MEMORY_CACHE_SIZE){

        };
    }

    private static class MemoryLruCacheManagerHolder{
        private static final MemoryLruCacheManager sInstance = new MemoryLruCacheManager();
    }

    public static MemoryLruCacheManager getInstance(){
        return MemoryLruCacheManagerHolder.sInstance;
    }

    public void put(String key,String value){
        if(null == mLruCache){
            return;
        }
        mLruCache.put(key,value);
    }

    public String getString(String value){
        if(null == mLruCache){
            return null;
        }
        return mLruCache.get(value);
    }
}
