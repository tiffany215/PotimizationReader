package shuhai.readercore.view.readview.dataloader;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import shuhai.readercore.view.readview.BookStatus;
import shuhai.readercore.view.readview.strategy.ComposingStrategy;

/**
 * @author 55345364
 * @date 2017/7/6.
 *
 * 获取章节内容并进行排版缓存
 *
 */

public class HorizontalScrollChapterLoader implements ChapterLoaderImpl{

   private ComposingStrategy mComposingStrategy;

    /**
     * 获取下一页内容
     */
    @Override
    public Vector<String> pageDown() {
        if(null == mComposingStrategy){
            return null;
        }
        return mComposingStrategy.nextPage();
    }

    /**
     * 获取上一页内容
     * @return
     */
    @Override
    public Vector<String> pageUp() {
        return mComposingStrategy.prePage();
    }

    @Override
    public void setComposingStrategy(ComposingStrategy composingStrategy) {
        this.mComposingStrategy = composingStrategy;
    }
}
