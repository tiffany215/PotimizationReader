package shuhai.readercore.view.readview.dataloader;
import java.util.Vector;

import shuhai.readercore.view.readview.FlipStatus;
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


    @Override
    public int getCountPage(FlipStatus flipStatus) {
        return mComposingStrategy.getCountPage(flipStatus);
    }

    @Override
    public void clearPageCache() {
        mComposingStrategy.clearPageCache();
    }

    @Override
    public void characterTypesetting(String key, FlipStatus status) {
        mComposingStrategy.characterTypesetting(key,status);
    }

    @Override
    public void chapterReplace(FlipStatus status) {
        mComposingStrategy.chapterReplace(status);
    }


    @Override
    public void setComposingStrategy(ComposingStrategy composingStrategy) {
        this.mComposingStrategy = composingStrategy;
    }

    @Override
    public Vector<String> obtainPageContent(int page, String key) {
        return mComposingStrategy.obtainPageContent(page,key);
    }

}
