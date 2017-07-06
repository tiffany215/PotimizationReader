package shuhai.readercore.view.readview;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author 55345364
 * @date 2017/7/6.
 *
 * 获取章节内容并进行排版缓存
 *
 */

public class HorizontalScrollChapterLoader implements ChapterLoaderImpl{


    /**
     * 章节缓存集合
     */
    private HashMap<String,String> cacheChapterArray = new HashMap<String,String>();




    /**
     * 获取上一页内容
     */
    private Vector<String> pageDown(){

        String strParagraph = "";
        Vector<String> lines = new Vector<>();





        return null;
    }


    /**
     * 获取下一页内容
     */
    private void getNextPage(){

    }


    @Override
    public BookStatus nextPage() {
        return null;
    }

    @Override
    public BookStatus prePage() {
        return null;
    }

    @Override
    public BookStatus curPage() {
        return null;
    }
}
