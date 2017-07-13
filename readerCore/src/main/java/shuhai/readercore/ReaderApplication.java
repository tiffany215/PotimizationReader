package shuhai.readercore;

import android.app.Application;

import shuhai.readercore.utils.AppUtils;

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
        AppUtils.init(this);



    }


    public static ReaderApplication getsInstance(){
        return sInstance;
    }




}
