package shuhai.readercore.utils;

/**
 * @author 55345364
 * @date 2017/7/5.
 */

public class ScreenUtils {


    /**
     * 获取手机屏幕高度
     *
     * @return
     */
    public static int getScreenWidth(){
        return AppUtils.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取手机屏幕高度
     *
     * @return
     */

    public static int getScreenHeight(){
        return AppUtils.getAppContext().getResources().getDisplayMetrics().heightPixels;
    }

}
