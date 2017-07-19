package shuhai.readercore.view.readview.dataloader;

import java.util.Vector;

import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.strategy.ComposingStrategy;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class VerticalScrollChapterLoader implements ChapterLoaderImpl{


    @Override
    public Vector<String> pageDown(int page, String key) {
        return null;
    }

    @Override
    public Vector<String> pageUp(int page, String key) {
        return null;
    }

    @Override
    public Vector<String> pageCur(int page, String key) {
        return null;
    }

    @Override
    public void setComposingStrategy(ComposingStrategy composingStrategy) {

    }
}
