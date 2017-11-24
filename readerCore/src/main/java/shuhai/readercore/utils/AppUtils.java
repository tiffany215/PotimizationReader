package shuhai.readercore.utils;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public final class AppUtils {


    /**
     * 获取版本名
     * @return
     */
    public static String getPackageName(){
        PackageInfo packageInfo = Utils.getPackageInfo();
        if(null != packageInfo){
            return packageInfo.packageName;
        }
        return "";
    }


    /**
     * 获取版本号
     * @return
     */
    public static int getAppVersionCode(){
        PackageInfo packageInfo = Utils.getPackageInfo();
        if(null != packageInfo){
            return packageInfo.versionCode;
        }
        return 1;
    }

    /**
     * 获取渠道名称
     *
     * @param
     * @param
     * @return
     */
    public static String getChannel(String name) {
        ApplicationInfo info;
        try {
            info = Utils.getAppContext().getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }





}
