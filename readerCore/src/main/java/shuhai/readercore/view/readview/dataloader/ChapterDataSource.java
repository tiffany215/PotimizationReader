package shuhai.readercore.view.readview.dataloader;

import java.util.Vector;

import shuhai.readercore.bean.ChapterEntity;
import shuhai.readercore.utils.StringUtils;

/**
 * 章节数据源
 * @author 55345364
 * @date 2017/11/15.
 */

public class ChapterDataSource {
    private ChapterLoaderStrategyImpl mLoaderStrategy;

    public ChapterDataSource(ChapterLoaderStrategyImpl strategy){
        this.mLoaderStrategy = strategy;
    }

    public ChapterLoaderModel getChapterInfo(ChapterEntity chapterEntity,int pageSize){
        if(null != mLoaderStrategy){
            if(!mLoaderStrategy.hasChapter(chapterEntity.getArticleId(),chapterEntity.getChapterId())){
                return null;
            }
            Vector<String> lines = mLoaderStrategy.obtainPageContent(chapterEntity.getChapterId(),pageSize, StringUtils.cacheKeyCreate(chapterEntity.getArticleId(),chapterEntity.getChapterId()));
            if(null != lines && lines.size() > 0){
                return new ChapterLoaderModel(chapterEntity.getChapterId(),chapterEntity.getChapterName(),pageSize,mLoaderStrategy.getCountPage(chapterEntity.getChapterId()),lines);
            }
        }
        return null;
    }
}
