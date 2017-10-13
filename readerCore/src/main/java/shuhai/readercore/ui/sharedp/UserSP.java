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
    public int getLastReaderChapterOrder(int articleId){
        return sp.getInt("last.read.order.mark" + articleId,0);
    }

    /**
     * 设置用户最后阅读章节排序号
     * @param chapterOrder
     */
    public void setLastReaderChapterOrder(int articleId,int chapterOrder){
        ed.putInt("last.read.order.mark" + articleId ,chapterOrder);
        ed.commit();
    }


    /**
     * 获取最后阅读章节ID
     * @param articleId 书籍Id
     * @return
     */
    public int getLastReaderChapterId(int articleId){
        return sp.getInt("last.read.chapter.mark" + articleId,-1);
    }

    /**
     * 设置最后阅读章节ID
     * @param articleId 书籍Id
     * @param chapterId 章节Id
     */
    public void setLastReaderChapterId(int articleId,int chapterId){
        ed.putInt("last.read.chapter.mark"+ articleId,chapterId);
        ed.commit();
    }


    /**
     * 设置最后阅读页码
     * @param articleId 书籍id
     * @param page 当前章节页码
     */
    public void setLastReaderPage(int articleId,int page){
        ed.putInt("last.read.page.mark" + articleId,page);
        ed.commit();
    }

    /**
     * 获取最后阅读页码
     * @param articleId 书籍id
     * @return
     */
    public int getLastReaderPage(int articleId){
        return sp.getInt("last.read.page.mark" + articleId,1);
    }

    /**
     * 设置推荐书籍状态
     * @param flag
     */
    public void setRecommendStatue(boolean flag){
        ed.putBoolean("book.store.recommend",flag);
        ed.commit();
    }


    /**
     * 获取推荐书籍状态
     * @return
     */
    public boolean getRecommendStatue(){
        return sp.getBoolean("book.store.recommend",false);
    }

}
