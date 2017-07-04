package shuhai.readercore.Utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class MyAppliaction  extends Application{

    private static MyAppliaction myAppliaction;


    @Override
    public void onCreate() {
        super.onCreate();
        this.myAppliaction = this;
    }


    /**
     * 获取全局 Context
     *
     * @return 全局 Context
     */
    private static Context getContext(){
        return myAppliaction.getApplicationContext();
    }


    /**
     * 获取应用版本号
     *
     * @return 应用版本号
     */
    public static int getAppVersion(){
        Context context = getContext();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
