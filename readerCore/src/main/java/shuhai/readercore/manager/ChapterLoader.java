package shuhai.readercore.manager;

import android.text.TextUtils;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class ChapterLoader {





    public String getChapter(String key){
        String content =  null;
        content = MemoryLruCacheManager.getInstance().getString(key);
        if(!TextUtils.isEmpty(content)){
            return content;
        }
        content = DiskLruCacheManager.getInstatnce().getString(key);
        if(!TextUtils.isEmpty(content)){
            return content;
        }
        return null;
    }

    public void put(String key,String value){
        MemoryLruCacheManager.getInstance().put(key,value);
        DiskLruCacheManager.getInstatnce().put(key,value);
    }





}
