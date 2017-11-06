package shuhai.readercore.utils;

import android.net.Uri;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.Set;

import shuhai.readercore.Constants;
import shuhai.readercore.ui.sharedp.UserSP;

/**
 * @author 55345364
 * @date 2017/11/3.
 */

public final  class UrlUtils {

    /**
     * 拼接加验证码的URL
     * @param vStr
     * @return
     */
    public static String markSignUrl(String vStr){
        try {
            Uri uri = Uri.parse(vStr);
            Set<String> collection  = uri.getQueryParameterNames();
            if(!collection.contains("packagename") || TextUtils.isEmpty(uri.getQueryParameter("packagename"))){
//                vStr += "&packagename=" +  AppUtils.getPackageName();
                vStr += "&packagename=com.shuhai.bookos";
            }
            if(!collection.contains("uuid") || TextUtils.isEmpty(uri.getQueryParameter("uuid"))){
                vStr += "&uuid=" +  PhoneUtils.getUUID();
            }
            if(!collection.contains("timestamp")||TextUtils.isEmpty(uri.getQueryParameter("timestamp"))){
                vStr += "&timestamp=" +  TimeFromatUtile.getCurrentDateTimeSeconds();
            }else{
                vStr = vStr.replaceAll("&timestamp=" + uri.getQueryParameter("timestamp"), "&timestamp=" +  TimeFromatUtile.getCurrentDateTimeSeconds());
            }
            if(!collection.contains("sign") ||TextUtils.isEmpty(uri.getQueryParameter("sign"))){
                vStr += "&sign=" + MD5Utils.string2MD5(AppUtils.getPackageName() + TimeFromatUtile.getCurrentDateTimeSeconds()  + PhoneUtils.getUUID() + Constants.KEY_SIGN);
            }else{
                vStr = vStr.replaceAll("&sign=" + uri.getQueryParameter("sign"), "&sign=" +  MD5Utils.string2MD5(AppUtils.getPackageName() + TimeFromatUtile.getCurrentDateTimeSeconds() + PhoneUtils.getUUID() + Constants.KEY_SIGN));
            }
            if(!collection.contains("version") || TextUtils.isEmpty(uri.getQueryParameter("version"))){
                vStr += "&version=" + AppUtils.getAppVersion();
            }
            if(!collection.contains("uid") || TextUtils.isEmpty(uri.getQueryParameter("uid"))){
                vStr += "&uid=" + UserSP.getInstance().getUid();
            }
            if(!collection.contains("pass") || TextUtils.isEmpty(uri.getQueryParameter("pass"))){
                vStr += "&pass=" + UserSP.getInstance().getPass();
            }
            if(!collection.contains("username") || TextUtils.isEmpty(uri.getQueryParameter("username"))){
                vStr += "&username=" + URLEncoder.encode(URLEncoder.encode(UserSP.getInstance().getUserName(), "UTF-8"), "UTF-8");
            }
            if(!collection.contains("source") || TextUtils.isEmpty(uri.getQueryParameter("source"))){
                vStr += "&source=" + AppUtils.getChannel("UMENG_CHANNEL");
            }
        }catch (Exception e){
            return vStr;
        }
        return vStr;
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
                    + "&source=shuhai&packagename=" + AppUtils.getPackageName();
        } catch (Exception e) {
            url = vstr + "&version=" + AppUtils.getAppVersion()
                    + "&source=shuhai&packagename=" + AppUtils.getPackageName();
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
                    + "&packagename=" + AppUtils.getPackageName();
        } catch (Exception e) {
            url = vstr + "&uid=" + UserSP.getInstance().getUid() + "&username=" + ""+"&pass=" + ""
                    + "&version=" + AppUtils.getAppVersion()
                    + "&source="
                    + AppUtils.getChannel("UMENG_CHANNEL")
                    + "&packagename=" + AppUtils.getPackageName();
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
                + "" + "\"imei\":\"" + PhoneUtils.getIMEI() + "\","
                + "\"uuid\":\"" + PhoneUtils.getUUID() + "\","
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
}
