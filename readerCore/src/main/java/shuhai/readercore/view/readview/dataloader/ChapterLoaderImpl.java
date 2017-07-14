package shuhai.readercore.view.readview.dataloader;

import java.util.Vector;

import shuhai.readercore.view.readview.strategy.ComposingStrategy;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public interface ChapterLoaderImpl {

    Vector<String> pageDown();

    Vector<String> pageUp();

    void setComposingStrategy(ComposingStrategy composingStrategy);



}
