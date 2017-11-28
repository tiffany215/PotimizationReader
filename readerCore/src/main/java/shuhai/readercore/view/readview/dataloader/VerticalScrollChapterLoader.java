package shuhai.readercore.view.readview.dataloader;


import java.util.Vector;

import shuhai.readercore.view.readview.strategy.ComposingStrategy;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class VerticalScrollChapterLoader implements ChapterLoaderStrategyImpl {


    @Override
    public int getCountPage(int chapterId) {
        return 0;
    }


    @Override
    public void setComposingStrategy(ComposingStrategy composingStrategy) {

    }

    @Override
    public Vector<String> obtainPageContent(int chapterId, int page, String key) {
        return null;
    }

    @Override
    public boolean hasChapter(int articleId, int chapterId) {
        return false;
    }

    @Override
    public void setFontSize(int fontSize,int fontHeight) {

    }

    @Override
    public void setLineSpace(int lineSpaceHeight) {

    }

    @Override
    public void characterTypesetting(int chapterId, String key) {

    }

}
