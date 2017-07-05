package shuhai.readercore;

import android.app.Application;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public class ReaderApplication extends Application {

    private static ReaderApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }


    public static ReaderApplication getsInstance(){
        return sInstance;
    }




}
