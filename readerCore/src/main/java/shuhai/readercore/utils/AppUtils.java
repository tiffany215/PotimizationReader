package shuhai.readercore.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public class AppUtils {

    private static Context mContext;



    public static void init(Context context){
        mContext = context;
    }


    public static Context getAppContext(){
        return mContext;
    }

    public static Resources getResources(){
        return mContext.getResources();
    }

    public static PackageInfo getPackageInfo(){
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static int getAppVersion(){
        PackageInfo packageInfo = getPackageInfo();
        if(null != packageInfo){
            return packageInfo.versionCode;
        }
        return 1;
    }













}
