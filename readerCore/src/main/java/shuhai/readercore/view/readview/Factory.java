package shuhai.readercore.view.readview;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public abstract class Factory {
    public abstract <T extends ChapterLoaderImpl> T createChapterLoader(Class<T> clz);
}
