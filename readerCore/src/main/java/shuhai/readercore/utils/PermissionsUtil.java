package shuhai.readercore.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

/**
 * @author 55345364
 * @date 2017/8/14.
 */

public class PermissionsUtil {
    private final Context mContext;

    public PermissionsUtil(Context context){
        this.mContext = context;
    }


    /**
     * 判断权限集合
     * @param permissions
     * @return
     */
    public boolean lacksPermissions(String... permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getTargetSDKVersionCode() >= Build.VERSION_CODES.M) {
                for (String permission : permissions) {
                    if(lacksPermissionM(permission)){
                        return true;
                    }
                }
            } else {
                for (String permission : permissions) {
                    if(lacksPermissionN(permission)){
                        return true;
                    }
                }
            }
        }
        return false;
    }




    /**
     * 判断时候缺少权限
     * @param permission
     * @return
     */
    private boolean lacksPermissionM(String permission){
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
    }

    private boolean lacksPermissionN(String permission){
        return PermissionChecker.checkSelfPermission(mContext, permission) == PermissionChecker.PERMISSION_DENIED;
    }


    private int getTargetSDKVersionCode(){
        try {
            final PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            return  info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return 0;
    }

}
