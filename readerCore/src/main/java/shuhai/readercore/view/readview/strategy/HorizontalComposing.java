package shuhai.readercore.view.readview.strategy;

import android.graphics.Paint;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import shuhai.readercore.manager.ChapterCacheManager;
import shuhai.readercore.utils.StringUtils;


/**
 * @author 55345364
 * @date 2017/7/13.
 *
 * 横向滑动页面 文字排版具体实现
 *
 */

public class HorizontalComposing implements ComposingStrategy{

    /**
     * 屏幕的高度和宽度
     */
    private int mHeight,mWidth;

    /**
     * 文字区域的高度和宽度
     */
    private int mVisibleHeight,mVisibleWidth;

    /**
     * 间距
     */
    private int mMarginHeight,mMarginWidth;

    /**
     * 文字高度
     */
    private int mFontHeight,mNumFontHeight;

    /**
     * 每页的行数
     */
    private int mPageLineCount;

    /**
     * 行间距
     */
    private int mLineSpace;

    /**
     * 段间距
     */
    private int mParagraphSpace;


    /**
     * 标题画笔和文字内容画笔
     */
    private Paint mPaint;

    private Map<Integer,Vector<String>> pageList;

    private Map<Integer,Map<Integer,Vector<String>>> chapterList;

    /**
     *  初始化绘制文本参数
     * @param width 手机屏幕宽度
     * @param height 手机屏幕高度
     * @param marginWidth 文字左右间距
     * @param marginHeight 文字上下间距
     * @param fontHeight 文字高度
     * @param titleFontHeight 标题文字大小
     * @param lineSpace 行间距
     * @param paint 文字画笔
     */
    public HorizontalComposing(int width, int height,int marginWidth,int marginHeight,int fontHeight,int titleFontHeight,int lineSpace,Paint paint){
        mWidth = width;
        mHeight = height;
        mMarginWidth = marginWidth;
        mMarginHeight = marginHeight;
        mFontHeight = fontHeight;
        mNumFontHeight = titleFontHeight;
        mLineSpace = lineSpace;

        mVisibleHeight = mHeight - (mMarginHeight * 4 + mNumFontHeight * 2);
        mVisibleWidth = mWidth - mMarginWidth * 2;

        mPaint = paint;
        if(null == pageList){
            pageList = new HashMap<>();
        }
       if(null == chapterList){
           chapterList = new HashMap<>();
       }

    }


    /**
     * 将文字分成段落
     * @param str 要分段的章节内容。
     * @return 段数组。
     */
    @Override
    public String[] autoSplitParagraph(String str) {

        if(TextUtils.isEmpty(str.trim())){
            return null;
        }
        return str.split("\r\n");
    }


    /**
     * 将段落内容分页
     * @param str 要进行分页的文字内容
     * @return
     */
    private Map<Integer,Vector<String>> autoSplitPage(String str){
        if(TextUtils.isEmpty(str)){
            return null;
        }

        String[] paragraphArr = autoSplitParagraph(str.trim());
        Vector<String> lines = new Vector<>();
        Map<Integer,Vector<String>> tempList = new HashMap<>();
        int paraSpace = 0;
        int pageSize = 0;
        //计算当前屏幕最多放置多少行文字
        mPageLineCount = mVisibleHeight / (mFontHeight + mLineSpace);

        int paragraphArrCount =  paragraphArr.length;

        for (int i = 0; i < paragraphArrCount; i++)
        {
            String strParagraph;
            if(TextUtils.isEmpty(paragraphArr[i])){
                 continue;
            }else{
                 strParagraph =  "\u0020\u0020" + paragraphArr[i];

            }
            if(TextUtils.isEmpty(strParagraph.trim())){
                continue;
            }
            Vector<String> paraLines = autoSplitLine(strParagraph);
            int lineSize = paraLines.size();
            int lineCount = 0;
            while (lineSize > 0 )
            {
                while (mPageLineCount > 0 && lineSize > 0)
                {

                    lines.add(paraLines.get(lineCount));
                    lineCount++;
                    lineSize--;
                    paraSpace += mLineSpace + mFontHeight;
                    mPageLineCount = (mVisibleHeight - paraSpace) / (mFontHeight + mLineSpace);
                }
                /**
                 * 如果条件为真，则证明当前段落超出绘制区域，所以当前页绘制完成存入集合，
                 * 并清理剩余可绘制段落数，从新开始绘制下一页。
                 *
                 * 如果条件为假，则证明当前段落绘制完成，但还有剩余绘制区域，此时加入段
                 * 间距，并从新计算剩余区域还可以绘制多少行。
                 */
                if(lineSize > mPageLineCount || paragraphArrCount - 1 == i){
                    pageSize++;
                    tempList.put(pageSize,lines);
                    lines = new Vector<>();
                    paraSpace = 0;
                    mPageLineCount = mVisibleHeight / (mFontHeight + mLineSpace);
                }else{
                    mPageLineCount = (mVisibleHeight - paraSpace - mParagraphSpace) / (mFontHeight + mLineSpace);
                }
            }
        }
        return tempList;
    }


    /**
     * 将段落分成行
     * @param strParagraph 要进行分行的段内容。
     * @return 当前页的内容  一个item存一行
     */
    private Vector<String> autoSplitLine(String strParagraph){
        Vector<String> paraLines = new Vector<>();
        if(TextUtils.isEmpty(strParagraph.trim())){
            return null;
        }
        while(strParagraph.length() > 0){
            int paintSize = mPaint.breakText(strParagraph,true,mVisibleWidth,null);
            paraLines.add(strParagraph.substring(0,paintSize));
            strParagraph = strParagraph.substring(paintSize);
        }
        return paraLines;
    }

    /**
     * 获取当前页内容
     * @param page
     * @param key
     * @return
     */
    @Override
    public Vector<String> obtainPageContent(int chapterId,int page, String key) {
        pageList = chapterList.get(chapterId);
        if(null == pageList || pageList.size() <= 0 ){
            if(characterTypesetting(chapterId,key)){
                return chapterList.get(chapterId).get(page);
            }
            return null;
        }
        return pageList.get(page);
}



    /**
     *  对获取字符进行排版，并放入map中。
     * @param key 章节缓存 key,由书籍id 和 章节id 拼接组成,(articleId + chapterId)
     * @return
     */
    @Override
    public boolean characterTypesetting(int chapterId,String key) {
        boolean isComplete = false;
        Map<Integer,Vector<String>> chapterMapData = autoSplitPage(ChapterCacheManager.getInstance().getChapter(key));
        if(null != chapterMapData && chapterMapData.size() > 0){
            chapterList.put(chapterId,chapterMapData);
            isComplete = true;
        }
        return isComplete;
    }

    @Override
    public int getCountPage(int chapterId) {
        Map<Integer,Vector<String>> pageList =  chapterList.get(chapterId);
        if(null != pageList && pageList.size() > 0){
            return pageList.size();
        }
        return 0;
    }

    @Override
    public void clearPageCache() {

    }

    @Override
    public boolean hasChapter(int articleId,int chapterId) {
        //判断Map中是否存在该章节
        if(null != chapterList && null != chapterList.get(chapterId) && chapterList.get(chapterId).size() > 0 ){
            return true;
        }

        //判断缓存中是否有该章节
        if(characterTypesetting(chapterId, StringUtils.cacheKeyCreate(articleId,chapterId))){
            return true;
        }

        return false;
    }

    @Override
    public void setTextSize(int textSize,int textHeight) {
        this.mFontHeight = textHeight;
        mPaint.setTextSize(textSize);

    }

    @Override
    public void setLineSpace(int space) {
        mLineSpace = space;
    }
}
