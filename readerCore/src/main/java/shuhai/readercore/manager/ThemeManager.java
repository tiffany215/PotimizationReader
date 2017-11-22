package shuhai.readercore.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;

import shuhai.readercore.R;
import shuhai.readercore.utils.ScreenUtils;
import shuhai.readercore.utils.Utils;

/**
 * @author 55345364
 * @date 2017/7/17.
 */

public class ThemeManager {
       public static final int READ_STYLE_THEME_00 = 0;
       public static final int READ_STYLE_THEME_01 = 1;
       public static final int READ_STYLE_THEME_02 = 2;
       public static final int READ_STYLE_THEME_03 = 3;
       public static final int READ_STYLE_THEME_04 = 4;
       public static final int READ_STYLE_THEME_05 = 5;
       public static final int READ_STYLE_THEME_06 = 6;
       public static final int READ_STYLE_THEME_07 = 7;
       public static final int READ_STYLE_THEME_08 = 8;
       public static final int READ_STYLE_THEME_09 = 9;
       public static final int READ_STYLE_THEME_10 = 10;
       public static final int READ_STYLE_THEME_11 = 11;
       public static final int READ_STYLE_THEME_12 = 12;
       public static final int READ_STYLE_THEME_13 = 13;
       public static final int READ_STYLE_THEME_14 = 14;
       public static final int READ_STYLE_THEME_15 = 15;
       public static final int READ_STYLE_THEME_16 = 16;
       public static final int READ_STYLE_THEME_17 = 17;
    public static Bitmap getThemeDrawable(int theme){
        Bitmap bitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        switch (theme) {
            case READ_STYLE_THEME_00:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(), R.color.read_style_theme_00));
                break;
            case READ_STYLE_THEME_01:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(), R.color.read_style_theme_01));
                break;
            case READ_STYLE_THEME_02:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_02));
                break;
            case READ_STYLE_THEME_03:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_03));
                break;
            case READ_STYLE_THEME_04:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_04));
                break;
            case READ_STYLE_THEME_05:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_05));
                break;
            case READ_STYLE_THEME_06:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_06));
                break;
            case READ_STYLE_THEME_07:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_07));
                break;
            case READ_STYLE_THEME_08:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_08));
                break;
            case READ_STYLE_THEME_09:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_09));
                break;
            case READ_STYLE_THEME_10:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_10));
                break;
            case READ_STYLE_THEME_11:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(),R.color.read_style_theme_11));
                break;
            case READ_STYLE_THEME_12:
                bitmap = BitmapFactory.decodeResource(Utils.getResources(),R.drawable.read_style_theme_12);
                break;
            case READ_STYLE_THEME_13:
                bitmap = BitmapFactory.decodeResource(Utils.getResources(),R.drawable.read_style_theme_13);
                break;
            case READ_STYLE_THEME_14:
                bitmap = BitmapFactory.decodeResource(Utils.getResources(),R.drawable.read_style_theme_14);
                break;
            case READ_STYLE_THEME_15:
                bitmap = BitmapFactory.decodeResource(Utils.getResources(),R.drawable.read_style_theme_15);
                break;
            case READ_STYLE_THEME_16:
                bitmap = BitmapFactory.decodeResource(Utils.getResources(),R.drawable.read_style_theme_16);
                break;
            case READ_STYLE_THEME_17:
                bitmap = BitmapFactory.decodeResource(Utils.getResources(),R.drawable.read_style_theme_17);
                break;
            default:
                bitmap.eraseColor(ContextCompat.getColor(Utils.getAppContext(), R.color.read_theme_white));
                break;
        }
        return bitmap;

    }

}
