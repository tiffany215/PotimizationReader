package shuhai.readercore.views.readview.dataloader;

import java.util.Vector;

import shuhai.readercore.views.readview.BookStatus;
import shuhai.readercore.views.readview.strategy.ComposingStrategy;

/**
 * @author 55345364
 * @date 2017/7/6.
 *
 * 获取章节内容并进行排版缓存
 *
 */

public class HorizontalScrollChapterLoader implements ChapterLoaderImpl{
    private ComposingStrategy composingStrategy;

    public void setComposingStrategy(ComposingStrategy strategy){
        this.composingStrategy = strategy;
    }

    /**
     * 获取下一页内容
     */
    private Vector<String> pageDown(){
        return  composingStrategy.nextPage();
    }


    /**
     * 获取上一页内容
     * @return
     */
    private Vector<String> pageUp(){
        return composingStrategy.prePage();
    }


    @Override
    public BookStatus nextPage() {
        if(null != this.pageDown() && this.pageDown().size() > 0){
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_NEXT_PAGE;
    }

    @Override
    public BookStatus prePage() {
        if(null != this.pageUp() && this.pageUp().size() > 0){
            return BookStatus.LOAD_SUCCESS;
        }
        return BookStatus.NO_PRE_PAGE;
    }

    @Override
    public BookStatus curPage() {
        return null;
    }
}
