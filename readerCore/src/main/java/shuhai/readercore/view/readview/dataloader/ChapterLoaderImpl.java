package shuhai.readercore.view.readview.dataloader;

import java.util.Vector;

import shuhai.readercore.view.readview.strategy.ComposingStrategy;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public interface ChapterLoaderImpl {

    /**
     * 获取上一页内容
     * @param page 第几页
     * @param key 获取缓存内容的key
     * @return 返回上一页文字内容
     */
    Vector<String> pageDown(int page,String key);

    /**
     * 获取当前页内容
     * @param page 第几页
     * @param key 获取缓存内容的key
     * @return 返回当前页文字内容
     */
    Vector<String> pageUp(int page,String key);

    /**
     * 获取下一页内容
     * @param page 第几页
     * @param key 获取缓存内容的key
     * @return 返回上一页文字内容
     */
    Vector<String> pageCur(int page,String key);

    /**
     * 返回章节可排版的总页数
     * @return
     */
    int getCountPage();

    /**
     *
     */
    void clearPageCache();



    void characterTypesetting(String key);

    void setComposingStrategy(ComposingStrategy composingStrategy);



}
