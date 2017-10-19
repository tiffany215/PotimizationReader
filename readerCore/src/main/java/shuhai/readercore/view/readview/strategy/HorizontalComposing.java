package shuhai.readercore.view.readview.strategy;

import android.graphics.Paint;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import shuhai.readercore.manager.ChapterLoader;
import shuhai.readercore.view.readview.FlipStatus;


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
     * 文字大小
     */
    private int mFontSize,mNumFontSize;

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


    private Map<Integer,Vector<String>> prePageList;
    private Map<Integer,Vector<String>> pageList;
    private Map<Integer,Vector<String>> nextPageList;
    private Map<Integer,Vector<String>> tempPageList;


    /**
     *  初始化绘制文本参数
     * @param width 手机屏幕宽度
     * @param height 手机屏幕高度
     * @param marginWidth 文字左右间距
     * @param marginHeight 文字上下间距
     * @param fontSize 文字大小
     * @param numFontSize 标题文字大小
     * @param lineSpace 行间距
     * @param paint 文字画笔
     */
    public HorizontalComposing(int width, int height,int marginWidth,int marginHeight,int fontSize,int numFontSize,int lineSpace,Paint paint){
        mWidth = width;
        mHeight = height;
        mMarginWidth = marginWidth;
        mMarginHeight = marginHeight;
        mFontSize = fontSize;
        mNumFontSize = numFontSize;
        mLineSpace = lineSpace;

        mVisibleHeight = mHeight - (mMarginHeight * 2 + mNumFontSize * 2 + mLineSpace * 2);
        mVisibleWidth = mWidth - mMarginWidth * 2;

        mPaint = paint;
        if(null == pageList){
            pageList = new HashMap<>();
        }
        if(null == prePageList){
            prePageList = new HashMap<>();
        }
        if(null == nextPageList){
            nextPageList = new HashMap<>();
        }
        if(null == tempPageList){
            tempPageList = new HashMap<>();
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
        int paraSpace = 0;
        int pageSize = 0;
        //计算当前屏幕最多放置多少行文字
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);

        int paragraphArrCount =  paragraphArr.length;

        for (int i = 0; i < paragraphArrCount; i++)
        {
            String strParagraph = paragraphArr[i];
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
                    paraSpace += mLineSpace + mFontSize;
                    mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
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
                    tempPageList.put(pageSize,lines);
                    lines = new Vector<>();
                    paraSpace = 0;
                    mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
                }else{
                    mPageLineCount = (mVisibleHeight - paraSpace - mParagraphSpace) / (mFontSize + mLineSpace);
                }
            }
        }
        return tempPageList;
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
    public Vector<String> obtainPageContent(int page, String key) {
        if(null == pageList || null == pageList.get(page) || pageList.get(page).size() == 0){
            if(characterTypesetting(key,FlipStatus.ON_FLIP_CUR)){
                return pageList.get(page);
            }else{
                return null;
            }
        }
        return pageList.get(page);
    }



    /**
     *  对获取字符进行排版，并放入map中。
     * @param key 章节缓存 key,由书籍id 和 章节id 拼接组成,(articleId + chapterId)
     * @return
     */
    @Override
    public boolean characterTypesetting(String key,FlipStatus status) {
        boolean isComplete = false;
        Map<Integer,Vector<String>> chapterMapData = autoSplitPage(ChapterLoader.getChapter(key));
        if(null != chapterMapData && chapterMapData.size() > 0){
            switch (status) {
                case ON_FLIP_PRE:
                    prePageList.putAll(chapterMapData);
                    break;

                case ON_FLIP_CUR:
                    pageList.putAll(chapterMapData);
                    break;

                case ON_FLIP_NEXT:
                    nextPageList.putAll(chapterMapData);
                    break;
            }

            isComplete = true;
        }
        return isComplete;
    }

    @Override
    public int getCountPage(FlipStatus status) {

        int pageCount;
        switch (status) {
            case ON_FLIP_PRE:
                pageCount =  prePageList.size();
                break;
            case ON_FLIP_CUR:
                pageCount =  pageList.size();
                break;
            case ON_FLIP_NEXT:
                pageCount =  nextPageList.size();
                break;
            default:
                pageCount = 1;
                break;
        }
        return pageCount;
    }

    @Override
    public void clearPageCache() {
        if(null != pageList){
            pageList.clear();
        }
    }

    @Override
    public void chapterReplace(FlipStatus flipStatus) {
        Map<Integer,Vector<String>> temp;
        switch (flipStatus) {
            case ON_FLIP_PRE:
                temp = pageList;
                pageList = prePageList;
                prePageList = nextPageList;
                nextPageList = temp;
                break;

            case ON_FLIP_NEXT:
                temp = pageList;
                pageList = nextPageList;
                nextPageList = prePageList;
                prePageList = temp;
                break;
        }
    }


}
