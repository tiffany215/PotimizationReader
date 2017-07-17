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
    public Vector<String> pageDown() {
        return null;
    }

    @Override
    public Vector<String> pageUp() {
        return null;
    }

    @Override
    public void setComposingStrategy(ComposingStrategy composingStrategy) {

    }
}
