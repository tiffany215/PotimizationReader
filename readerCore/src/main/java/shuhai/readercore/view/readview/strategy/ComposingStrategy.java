package shuhai.readercore.view.readview.strategy;

import java.util.Vector;

/**
 * @author 55345364
 * @date 2017/7/13.
 *
 * 文字排版策略接口，实现横向排版和纵向排版
 *
 */

public interface ComposingStrategy {

    String[] autoSplitParagraph(String str);

//    //获取上一页内容
//    Vector<String> pageUp(int page, String key);


    //获取当前页内容
    Vector<String> obtainPageContent(int chapterId,int page, String key);


//    //获取下一页内容
//    Vector<String> pageDown(int page, String key);


    //文字排版
    boolean characterTypesetting(int chapterId,String key);


    int getCountPage(int chapterId);


    void clearPageCache();


    boolean hasChapter(int articleId,int chapterId);

    void setTextSize(int textSize);



}
