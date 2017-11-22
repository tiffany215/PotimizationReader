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
    public Vector<String> obtainPageContent(int chapterId, int page, String key) {
        return null;
    }

    @Override
    public boolean characterTypesetting(int chapterId, String key) {
        return false;
    }

    @Override
    public int getCountPage(int chapterId) {
        return 0;
    }

    @Override
    public void clearPageCache() {

    }

    @Override
    public boolean hasChapter(int articleId, int chapterId) {
        return false;
    }

    @Override
    public void setTextSize(int textSize) {

    }
}
