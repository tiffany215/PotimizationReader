package shuhai.readercore.ui.sharedp;

import android.content.Context;
import android.content.SharedPreferences;

import shuhai.readercore.common.Constants;
import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.utils.Utils;

import static shuhai.readercore.common.Constants.FLIP_CONFIG.LEVEL_COVER_FLIP;

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


    /**
     * 获取阅读主题
     * @return
     */
    public int getReaderTheme(){
        return sp.getInt("read_theme", ThemeManager.READ_STYLE_THEME_15);
    }

    /**
     * 设置阅读主题
     * @param theme
     */
    public void setReaderTheme(int theme){
        ed.putInt("read_theme",theme);
        ed.commit();
    }


    /**
     * 获取夜间阅读模式
     * @return
     */
    public boolean getReaderDayOrNight(){
        return sp.getBoolean("read_theme_day_or_night",true);
    }

    /**
     * 设置夜间阅读模式
     * @param status
     */
    public void setReaderDayOrNight(boolean status){
        ed.putBoolean("read_theme_day_or_night",status);
        ed.commit();
    }


    /**
     * 获取行间距
     * @return
     */
    public float getLineSpace(){
        return sp.getFloat("read_line_space", Constants.LINE_SPACE.COMMON_LINE_AMONG);
    }


    /**
     * 设置行间距
     * @param space
     */
    public void setLineSpace(float space){
        ed.putFloat("read_line_space",space);
        ed.commit();
    }


    /**
     * 获取字体大小
     * @return
     */
    public int getTextSize(){
        return sp.getInt("read_text_size",34);
    }

    /**
     * 设置字体大小
     * @param textSize
     */
    public void setTextSize(int textSize){
        ed.putInt("read_text_size",textSize);
        ed.commit();

    }




}
