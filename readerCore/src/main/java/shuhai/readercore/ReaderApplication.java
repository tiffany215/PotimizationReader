package shuhai.readercore;

import android.app.Application;

import com.kingja.loadsir.core.LoadSir;

import shuhai.readercore.ui.dialog.callback.LoadingCallback;
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
        LoadSir.beginBuilder().addCallback(new LoadingCallback()).setDefaultCallback(LoadingCallback.class).commit();

    }


    public static ReaderApplication getsInstance(){
        return sInstance;
    }




}
