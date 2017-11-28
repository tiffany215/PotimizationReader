package shuhai.readercore.common;

import android.app.Application;
import shuhai.readercore.utils.Utils;

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
        Utils.init(this);
    }


    public static ReaderApplication getsInstance(){
        return sInstance;
    }




}
