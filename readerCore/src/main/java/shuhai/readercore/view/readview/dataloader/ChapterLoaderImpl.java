package shuhai.readercore.view.readview.dataloader;

import java.util.Vector;

import shuhai.readercore.view.readview.strategy.ComposingStrategy;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public interface ChapterLoaderImpl {

    Vector<String> pageDown(int page,String key);

    Vector<String> pageUp(int page,String key);

    Vector<String> pageCur(int page,String key);

    int getCountPate();


    void setComposingStrategy(ComposingStrategy composingStrategy);



}
