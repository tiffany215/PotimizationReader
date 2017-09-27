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
    public Vector<String> pageDown(int page,String key) {
        if(null == mComposingStrategy){
            return null;
        }
        return mComposingStrategy.pageDown(page,key);
    }

    /**
     * 获取上一页内容
     * @return
     */
    @Override
    public Vector<String> pageUp(int page,String key) {
        return mComposingStrategy.pageUp(page,key);
    }


    /**
     * 获取当前页内容
     * @param page
     * @param key
     * @return
     */
    @Override
    public Vector<String> pageCur(int page, String key) {
        return mComposingStrategy.pageCur(page,key);
    }

    @Override
    public int getCountPate() {
        return mComposingStrategy.getCountPage();
    }

    @Override
    public void clearPageCache() {
        mComposingStrategy.clearPageCache();
    }

    @Override
    public void characterTypesetting(String key) {
        mComposingStrategy.characterTypesetting(key);
    }


    @Override
    public void setComposingStrategy(ComposingStrategy composingStrategy) {
        this.mComposingStrategy = composingStrategy;
    }
}
