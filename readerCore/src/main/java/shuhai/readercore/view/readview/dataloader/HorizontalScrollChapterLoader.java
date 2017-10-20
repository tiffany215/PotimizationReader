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
    public int getCountPage(int chapterId) {
        return mComposingStrategy.getCountPage(chapterId);
    }

    @Override
    public void clearPageCache() {
        mComposingStrategy.clearPageCache();
    }

    @Override
    public void characterTypesetting(int chapterId,String key, FlipStatus status) {
        mComposingStrategy.characterTypesetting(chapterId,key);
    }

    @Override
    public void setComposingStrategy(ComposingStrategy composingStrategy) {
        this.mComposingStrategy = composingStrategy;
    }

    @Override
    public Vector<String> obtainPageContent(int chapterId,int page, String key) {
        return mComposingStrategy.obtainPageContent(chapterId,page,key);
    }

}
