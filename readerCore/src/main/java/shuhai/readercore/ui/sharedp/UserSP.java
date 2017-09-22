package shuhai.readercore.ui.sharedp;

import android.content.Context;
import android.content.SharedPreferences;

import shuhai.readercore.bean.UserEntity;
import shuhai.readercore.utils.AppUtils;

/**
 * @author 55345364
 * @date 2017/8/14.
 */

public class UserSP {

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private static final String FILE_KEY = "shuhai_userinfo_setting_preferences";

    private UserSP(){
        sp = AppUtils.getAppContext().getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
        ed = sp.edit();
    }

    public static UserSP getInstance(){
        return UserSPHolder.INSTANCE;
    }


    private static class UserSPHolder{
        public static final UserSP INSTANCE =  new UserSP();
    }


    /**
     * 保存登录信息
     *
     */
    public void saveInfo(final UserEntity user) {
        ed.putString("user.uname", user.getMessage().getUname());
        ed.putString("user.name", user.getMessage().getName());
        ed.putString("user.pass", user.getMessage().getPass());
        ed.putString("user.uid", user.getMessage().getUid());
        ed.putString("user.avatar", user.getMessage().getAvatar());
        ed.putString("user.egold", user.getMessage().getEgold());
        ed.putString("user.issign", user.getMessage().getIssign());
        ed.putString("user.vip", user.getMessage().getVip());
        ed.commit();
    }



    /**
     * 获取用户id
     *
     * @return
     */
    public String getUid() {
        return sp.getString("user.uid", "0");
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public String getUserName() {
        return sp.getString("user.uname", ""
                + "");
    }


    /**
     * 获取用户昵称
     * @return
     */
    public String getNikeName() {
        return sp.getString("user.name", "");
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPass() {
        return sp.getString("user.pass", "");
    }


    /**
     * 获取用户最后阅读章节排序号
     * @return
     */
    public int getLastReaderChapterOrder(){
        return sp.getInt("last.read.order.mark",0);
    }

    /**
     * 设置用户最后阅读章节排序号
     * @param chapterOrder
     */
    public void setLastReaderChapterOrder(int chapterOrder){
        ed.putInt("last.read.order.mark",chapterOrder);
        ed.commit();
    }


}
