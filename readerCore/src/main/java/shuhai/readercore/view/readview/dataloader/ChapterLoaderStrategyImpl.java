package shuhai.readercore.view.readview.dataloader;

import java.util.Vector;

import shuhai.readercore.view.readview.strategy.ComposingStrategy;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public interface ChapterLoaderStrategyImpl {

    /**
     * 返回章节可排版的总页数
     * @return
     */
    int getCountPage(int chapterId);


    void setComposingStrategy(ComposingStrategy composingStrategy);

    /**
     * 获取下一页内容
     * @param page 第几页
     * @param key 获取缓存内容的key
     * @return 返回上一页文字内容
     */
    Vector<String> obtainPageContent(int chapterId, int page, String key);


    boolean hasChapter(int articleId, int chapterId);

    void setFontSize(int fontSize);

    void characterTypesetting(int chapterId,String key);


}
