package shuhai.readercore.common;

import android.app.Application;

import com.kingja.loadsir.core.LoadSir;

import shuhai.readercore.ui.dialog.LoadingCallback;
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
        LoadSir.beginBuilder().addCallback(new LoadingCallback()).setDefaultCallback(LoadingCallback.class).commit();

    }


    public static ReaderApplication getsInstance(){
        return sInstance;
    }




}
