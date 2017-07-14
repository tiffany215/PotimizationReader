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
    public void pageUp() {

    }

    @Override
    public void pageDown() {

    }

    @Override
    public Vector<String> nextPage() {
        return null;
    }

    @Override
    public Vector<String> prePage() {
        return null;
    }
}
