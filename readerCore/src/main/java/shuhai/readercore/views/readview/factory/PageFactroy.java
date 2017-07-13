package shuhai.readercore.views.readview.factory;

import android.graphics.Canvas;

import java.util.Vector;

import shuhai.readercore.views.readview.dataloader.ChapterLoaderImpl;
import shuhai.readercore.views.readview.dataloader.HorizontalScrollChapterLoader;

/**
 * @author 55345364
 * @date 2017/7/6.
 */

public class PageFactroy extends Factory {

    private ChapterLoaderImpl chapterLoader;


    @Override
    public <T extends ChapterLoaderImpl> T createChapterLoader(Class<T> clz) {
        ChapterLoaderImpl chapterLoader = null;
        try {
            chapterLoader = (ChapterLoaderImpl) Class.forName(clz.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) chapterLoader;
    }



    public void setChapterLoader(){
        this.chapterLoader = createChapterLoader(HorizontalScrollChapterLoader.class);
    }




    /**
     * 绘制章节内容到具体 widget 的 bitmap上。
     * @param canvas  由具体的 widget (如仿真翻页组件) 传过来的 canvas。
     */
    public synchronized void onDraw(Canvas canvas){







    }




    private Vector<String> getPrePage(){


        return null;
    }




























}
