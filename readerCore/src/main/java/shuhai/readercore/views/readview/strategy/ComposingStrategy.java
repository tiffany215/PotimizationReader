package shuhai.readercore.views.readview.strategy;

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

    //获取上一页内容
    void pageUp();

    //获取下一页内容
    void pageDown();

    //向下翻页
    Vector<String> nextPage();

    //向上翻页
    Vector<String> prePage();

}
