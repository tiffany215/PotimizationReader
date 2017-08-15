package shuhai.readercore.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.net.URLEncoder;
import java.util.UUID;

import shuhai.readercore.Constants;
import shuhai.readercore.ui.sharedp.UserSP;

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


    /**
     *
     * @param vstr
     * @return
     */
    public static String makeWebUrl(String vstr) {
        String url = "";
        try {
            url = vstr
                    + "&version=" + AppUtils.getAppVersion()
                    + "&source=shuhai&packagename=" + AppUtils.getPackageInfo().packageName;
        } catch (Exception e) {
            url = vstr + "&version=" + AppUtils.getAppVersion()
                    + "&source=shuhai&packagename=" + AppUtils.getPackageInfo().packageName;
        }
        return url;
    }


    /**
     * 仅限用户和目录页面使用
     * @param
     * @param vstr
     * @return
     */
    public static String makeUserUrl(String vstr) {
        String url = "";
        try {
            url = vstr
                    + "&uid="
                    + UserSP.getInstance().getUid()
                    + "&pass="
                    + UserSP.getInstance().getPass()
                    + "&username="
                    + URLEncoder.encode(URLEncoder.encode(UserSP.getInstance().getUserName(), "UTF-8"), "UTF-8")
                    + "&version=" + AppUtils.getAppVersion()
                    + "&source="
                    + AppUtils.getChannel("UMENG_CHANNEL")
                    + "&packagename=" + AppUtils.getPackageInfo().packageName;
        } catch (Exception e) {
            url = vstr + "&uid=" + UserSP.getInstance().getUid() + "&username=" + ""+"&pass=" + ""
                    + "&version=" + AppUtils.getAppVersion()
                    + "&source="
                    + AppUtils.getChannel("UMENG_CHANNEL")
                    + "&packagename=" + AppUtils.getPackageInfo().packageName;
        }
        return url;
    }


    /**
     * webview 初始化加载 ，将app信息发送服务端
     * @return
     */
    public static  String makeJsonText(){
        String systemTime = String.valueOf(System.currentTimeMillis());
        String str = "{" + "\"username\":\"" + UserSP.getInstance().getUserName() + "\","
                + "" + "\"nickname\":\"" + UserSP.getInstance().getNikeName() + "\","
                + "" + "\"imei\":\"" + AppUtils.getIMEI() + "\","
                + "\"uuid\":\"" + AppUtils.getMyUUID() + "\","
                + "\"source\":\""+AppUtils.getChannel("UMENG_CHANNEL")+ "\","
                + "\"version\":\"" + AppUtils.getAppVersion() + "\","
                + "\"uid\":\"" + UserSP.getInstance().getUid() + "\","
                + "\"pass\":\"" +UserSP.getInstance().getPass() + "\","
                + "\"packagename\":\"com.shuhai.bookos\","
//                + "\"packagename\":\""+ AppUtils.getPackageInfo().packageName + "\","
                + "\"timestamp\":\"" + systemTime + "\","
                + "\"siteid\":\"" + Constants.SITEID + "\","
                + "\"sign\":\""+ MD5Utils.string2MD5(Constants.KEY_SIGN + systemTime) + "\"" + "}";
        return str;


    }

    // 获取imei
    public static String getIMEI() {
        try {
            final TelephonyManager tm = (TelephonyManager) AppUtils.getAppContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }


    // 获取uuid
    public static String getMyUUID() {
        String uniqueId;
            final TelephonyManager tm = (TelephonyManager) mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    mContext.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            uniqueId = deviceUuid.toString();

        return uniqueId;
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
            info = mContext.getPackageManager().getApplicationInfo(
                    mContext.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }





}
