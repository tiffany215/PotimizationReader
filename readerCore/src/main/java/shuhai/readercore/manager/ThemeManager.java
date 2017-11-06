package shuhai.readercore.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;

import shuhai.readercore.R;
import shuhai.readercore.utils.AppUtils;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.utils.Utils;

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
    public static final int PARCHMENT = 6;
    public static final int CLASSICAL = 7;
    public static final int MODERN = 8;


    public static Bitmap getThemeDrawable(int theme){
        Bitmap bitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        switch (theme) {
            case NORMAL:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(), R.color.read_theme_white));
                break;
            case YELLOW:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_theme_yellow));
                break;
            case GREEN:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_theme_green));
                break;
            case LEATHER:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_theme_white));
                break;
            case GRAY:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_theme_gray));
                break;
            case NIGHT:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_theme_night));
                break;
            case PARCHMENT:
                bitmap = BitmapFactory.decodeResource(Utils.getResources(),R.drawable.theme_reader_bg_02);
                break;

            case CLASSICAL:
                bitmap = BitmapFactory.decodeResource(Utils.getResources(),R.drawable.theme_leather_bg);
                break;

            case MODERN:
                bitmap = BitmapFactory.decodeResource(Utils.getResources(),R.drawable.theme_reader_bg_01);
                break;
            default:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(), R.color.read_theme_white));
                break;

        }
        return bitmap;

    }

}
