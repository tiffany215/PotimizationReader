package shuhai.readercore.view.readview.dataloader;

import java.util.Vector;

/**
 * 章节数据结构
 * @author 55345364
 * @date 2017/11/13.
 */

public class ChapterLoaderModel {


    private int chapterId;

    private String chapterName;


    private int pageSize;

    private int pageCount;

    private Vector<String> pageContent;

    public ChapterLoaderModel(int chapterId, String chapterName,int pageSize, int pageCount,  Vector<String> pageContent) {
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.pageContent = pageContent;
    }




    public int getChapterId() {
        return chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public Vector<String> getPageContent() {
        return pageContent;
    }
}
