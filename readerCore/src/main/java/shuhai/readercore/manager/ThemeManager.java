package shuhai.readercore.manager;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;

import shuhai.readercore.R;
import shuhai.readercore.utils.AppUtils;
import shuhai.readercore.utils.ScreenUtils;

/**
 * @author 55345364
 * @date 2017/7/17.
 */

public class ThemeManager {


    public static final int NORMAL = 0;
    public static final int YELLOW = 1;
    public static final int GREEN = 2;
    public static final int LEATHER = 3;
    public static final int GRAY = 4;
    public static final int NIGHT = 5;


    public static Bitmap getThemeDrawable(int theme){
        Bitmap bitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        switch (theme) {
            case NORMAL:
                bitmap.eraseColor(ContextCompat.getColor(AppUtils.getAppContext(), R.color.read_theme_white));
                break;
            case YELLOW:
                bitmap.eraseColor(ContextCompat.getColor(AppUtils.getAppContext(),R.color.read_theme_yellow));
                break;
            case GREEN:
                bitmap.eraseColor(ContextCompat.getColor(AppUtils.getAppContext(),R.color.read_theme_green));
                break;
            case LEATHER:
                bitmap.eraseColor(ContextCompat.getColor(AppUtils.getAppContext(),R.color.read_theme_white));
                break;
            case GRAY:
                bitmap.eraseColor(ContextCompat.getColor(AppUtils.getAppContext(),R.color.read_theme_gray));
                break;
            case NIGHT:
                bitmap.eraseColor(ContextCompat.getColor(AppUtils.getAppContext(),R.color.read_theme_night));
                break;
            default:
                bitmap.eraseColor(ContextCompat.getColor(AppUtils.getAppContext(), R.color.read_theme_white));
                break;

        }
        return bitmap;

    }

}
