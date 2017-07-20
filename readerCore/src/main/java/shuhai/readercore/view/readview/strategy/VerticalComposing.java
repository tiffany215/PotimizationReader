package shuhai.readercore.view.readview.strategy;


import java.util.Vector;

/**
 * @author 55345364
 * @date 2017/7/13.
 * 上下滑动翻页 文字排版具体实现
 *
 */

public class VerticalComposing implements ComposingStrategy{


    @Override
    public String[] autoSplitParagraph(String str) {
        return new String[0];
    }

    @Override
    public Vector<String> pageUp(int page, String key) {
        return null;
    }

    @Override
    public Vector<String> pageCurr(int page, String key) {
        return null;
    }

    @Override
    public Vector<String> pageDown(int page, String key) {
        return null;
    }

    @Override
    public boolean characterTypesetting(String key) {
        return false;
    }
}
