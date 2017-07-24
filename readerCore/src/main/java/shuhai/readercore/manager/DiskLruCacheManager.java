package shuhai.readercore.manager;


import android.content.Context;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import shuhai.readercore.Constants;
import shuhai.readercore.utils.AppUtils;
import shuhai.readercore.utils.SecretUtil;

/**
 * Created by 55345364 on 2017/7/3.
 */

public class DiskLruCacheManager {

    private volatile static DiskLruCache mDiskLruCache;
    private static DiskLruCacheManager mInstance = null;
    private DiskLruCache.Editor mEditor = null;
    private DiskLruCache.Snapshot mSnapshot = null;


    private DiskLruCacheManager(Context context,String uniqueName){
        try {
            if(null != mDiskLruCache){
                mDiskLruCache.close();
                mDiskLruCache = null;
            }
            File cacheFile = getCacheFile(context,uniqueName);
            mDiskLruCache = DiskLruCache.open(cacheFile, AppUtils.getAppVersion(),1, Constants.MAX_DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DiskLruCacheManager getInstance (Context context,String uniqueName){
        if(null == mInstance){
            synchronized (DiskLruCacheManager.class){
                if(null == mInstance){
                    return new DiskLruCacheManager(context,uniqueName);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取缓存的路径 两个路径在卸载程序时都会删除，因此不会在卸载后还保留乱七八糟的缓存
     * 有SD卡时获取  /sdcard/Android/data/<application package>/cache
     * 无SD卡时获取  /data/data/<application package>/cache
     *
     * @param context    上下文
     * @param uniqueName 缓存目录下的细分目录，用于存放不同类型的缓存
     * @return 缓存目录 File
     */
    private File getCacheFile(Context context,String uniqueName){
        String cachePath = null;
        if((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) && null != context.getExternalCacheDir()){
            cachePath = context.getExternalCacheDir().getPath();
        }else{
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


    /**
     *  获取缓存 editor
     * @param key
     * @return
     * @throws IOException
     */
    private DiskLruCache.Editor edit(String key) throws IOException {
        if(null != mDiskLruCache){
            mEditor = mDiskLruCache.edit(key);
        }
        return mEditor;
    }


    /**
     *  根据 key 获取缓存缩略
     * @param key
     * @return
     */

    private DiskLruCache.Snapshot snapshot(String key){
        try {
            if(null != mDiskLruCache){
                mSnapshot = mDiskLruCache.get(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mSnapshot;
    }


    /*************************  字符串读写 ***************************/
    /**
     * 缓存 String
     * @param key
     * @param value
     */
    public void put(String key,String value){
        DiskLruCache.Editor editor = null;
        BufferedWriter writer = null;
        try {
            editor = edit(key);
            OutputStream os = editor.newOutputStream(0);
            writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(value);
        } catch (IOException e) {
            e.printStackTrace();
            if(null != editor){
                try {
                    editor.abort();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取字符串缓存
     * @param key
     * @return
     */
    public String getString(String key)  {
        InputStream inputStream = getCacheInputStream(key);
        if(null == inputStream){
            return null;
        }
        try {
            return inputStream2String(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /*************************   Json 数据读写 ***************************************/
    /**
     *  json数据存入缓存
     * @param key
     * @param value
     */
    public void put(String key, JSONObject value){
        put(key,value.toString());
    }


    /**
     *  获取json数据
     * @param key
     * @return
     */
    public JSONObject getJson(String key){
        String json = getString(key);
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**********************************数组对象数据读写*******************************************************/
    /**
     * 存入byte[] 数组
     * @param key
     * @param bytes
     */
    public void put(String key,byte[] bytes){
        OutputStream os = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = edit(key);
            if(null == editor){
                return;
            }
            os = editor.newOutputStream(0);
            os.write(bytes);
            os.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            if(null != editor){
                try {
                    editor.abort();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }finally{
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取缓存的byte[] 数组
     * @param key
     * @return
     */
    public byte[] getBytes(String key){
        byte[] bytes = null;
        InputStream in = getCacheInputStream(key);
        if(null == in){
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[256];
        int len = 0;
        try {
            while(-1 != (len = in.read(buf))){
                bos.write(buf,0,len);
            }
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return bytes;
    }

    /****************************  序列化数据读写 ***************************/
    /**
     * 缓存序列化数据
     * @param key
     * @param object
     */
    public void put(String key, Serializable object){
        ObjectOutputStream oos = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = edit(key);
            if(null == editor){
                return;
            }
            oos = new ObjectOutputStream(editor.newOutputStream(0));
            oos.writeObject(object);
            oos.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            if(null != editor){
                try {
                    editor.abort();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  获取序列化数据
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getSerializable(String key){
        T object = null;
        DiskLruCache.Snapshot mSnapshot = null;
        ObjectInputStream ois = null;
        InputStream is = getCacheInputStream(key);
        mSnapshot = snapshot(key);
        if(null == mSnapshot){
            return null;
        }
        try {
            ois = new ObjectInputStream(is);
            object = (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }



    /**************************************************************************************
     * ********************************* 辅助工具方法 分割线 *******************************
     * ***********************************************************************************/

    /**
     *  inputStream 转 String
     * @param in  输入流
     * @return  结果字符串
     * @throws IOException
     */

    private String inputStream2String(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        StringBuilder buffer = new StringBuilder();
        String line;
        while(null != (line = reader.readLine())){
            buffer.append(line);
        }
        return buffer.toString();
    }


    /**
     * 获取缓存数据的 InoutStream
     * @param key
     * @return
     */
    private InputStream getCacheInputStream(String key){
        key = SecretUtil.getMD5Result(key);
        InputStream in;
        DiskLruCache.Snapshot snapshot = snapshot(key);
        if(null == snapshot){
            return null;
        }
        in = snapshot.getInputStream(0);
        return in;
    }


    /**
     * 同步记录文件
     */
    public void flush(){
        if(null !=mDiskLruCache){
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }













}
