package shuhai.readercore.view.readview.dataloader;

import java.util.Vector;

import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.FlipStatus;
import shuhai.readercore.view.readview.strategy.ComposingStrategy;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class VerticalScrollChapterLoader implements ChapterLoaderImpl{


    @Override
    public int getCountPage(int chapterId) {
        return 0;
    }

    @Override
    public void clearPageCache() {

    }

    @Override
    public void characterTypesetting(int chapterId, String key, FlipStatus status) {

    }

    @Override
    public void setComposingStrategy(ComposingStrategy composingStrategy) {

    }

    @Override
    public Vector<String> obtainPageContent(int chapterId, int page, String key) {
        return null;
    }
}
