package shuhai.readercore.ui.sharedp;

import android.content.Context;
import android.content.SharedPreferences;

import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.utils.AppUtils;
import shuhai.readercore.utils.Utils;

import static shuhai.readercore.Constants.FLIP_CONFIG.LEVEL_COVER_FLIP;

/**
 * @author 55345364
 * @date 2017/10/24.
 */

public class ReaderSP {

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private static final String FILE_KEY = "shuhai_read_info_setting_preferences";

    private ReaderSP(){
        sp = Utils.getAppContext().getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
        ed = sp.edit();

    }

    public static ReaderSP getInstance(){
        return ReaderSPHelp.INSTANCE;
    }



    private static class ReaderSPHelp{
        public static final ReaderSP INSTANCE = new ReaderSP();
    }


    /**
     * 设置翻页模式
     * @param model
     */
    public void setFlipModel(int model){
        ed.putInt("read_flip_mode",model);
        ed.commit();
    }

    /**
     * 获取翻页模式
     * @return
     */
    public int getFlipModel(){
        return sp.getInt("read_flip_mode",LEVEL_COVER_FLIP);
    }



    public int getReaderTheme(){
        return sp.getInt("read_theme", ThemeManager.READ_STYLE_THEME_15);
    }

    public void setReaderTheme(int theme){
        ed.putInt("read_theme",theme);
        ed.commit();
    }


    public boolean getReaderDayOrNight(){
        return sp.getBoolean("read_theme_day_or_night",true);
    }

    public void setReaderDayOrNight(boolean status){
        ed.putBoolean("read_theme_day_or_night",status);
        ed.commit();
    }


}
