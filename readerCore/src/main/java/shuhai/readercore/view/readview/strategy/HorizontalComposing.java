package shuhai.readercore.view.readview.strategy;

import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import shuhai.readercore.manager.ChapterLoader;

import static android.content.ContentValues.TAG;

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
    private int mPagelineCount;

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
    private Paint mTitlePaint,mPaint;


    private Map<Integer,Vector<String>> pageList;


    public HorizontalComposing(int width, int height,int marginWidth,int marginHeight,int fontSize,Paint paint,Paint titlePaint){
        mWidth = width;
        mHeight = height;
        mMarginWidth = marginWidth;
        mMarginHeight = marginHeight;
        mFontSize = fontSize;
        mVisibleHeight = mHeight - marginHeight * 2 - mNumFontSize * 2 - mLineSpace * 2;
        mVisibleWidth = mWidth - marginWidth * 2;
        mLineSpace = mFontSize / 5 * 2;
        mPagelineCount = mVisibleHeight / (mFontSize + mLineSpace);
        mPaint = paint;
        mTitlePaint = titlePaint;
        if(null == pageList){
            pageList = new HashMap<>();
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
        mPagelineCount = mVisibleHeight / (mFontSize + mLineSpace);


        for (int i = 0; i < paragraphArr.length; i++)
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
                while (mPagelineCount > 0 && lineSize > 0)
                {
                    lines.add(paraLines.get(lineCount));
                    lineCount++;
                    lineSize--;
                    paraSpace += mLineSpace + mFontSize;
                    mPagelineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
                }
                /**
                 * 如果条件为真，则证明当前段落超出绘制区域，所以当前页绘制完成存入集合，
                 * 并清理剩余可绘制段落数，从新开始绘制下一页。
                 *
                 * 如果条件为假，则证明当前段落绘制完成，但还有剩余绘制区域，此时加入段
                 * 间距，并从新计算剩余区域还可以绘制多少行。
                 */
                if(lineSize > mPagelineCount){
                    pageSize++;
                    pageList.put(pageSize,lines);
                    lines = new Vector<>();
                    paraSpace = 0;
                    mPagelineCount = mVisibleHeight / (mFontSize + mLineSpace);
                }else{
                    mPagelineCount = (mVisibleHeight - paraSpace - mParagraphSpace) / (mFontSize + mLineSpace);
                }
            }
        }
        return pageList;
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
     *  从缓存中后去章节内容后进行排版
     * @param page
     * @param key
     * @return
     */
    @Override
    public Vector<String> pageUp(int page, String key) {
        if(null == pageList || null == pageList.get(page) || pageList.get(page).size() == 0){
            if(characterTypesetting(key)){
                return pageList.get(page);
            }else{
                return null;
            }
        }
        return pageList.get(page);
    }


    /**
     * 获取下一页内容
     * @param page 页码数
     * @param key 从缓存中取出内容的依据  （由 书籍id 和章节id 组成）
     * @return
     */
    @Override
    public Vector<String> pageDown(int page, String key) {
        if(null == pageList || null == pageList.get(page) || pageList.get(page).size() == 0){
            if(characterTypesetting(key)){
                return pageList.get(page);
            }else{
                return null;
            }
        }
        return pageList.get(page);
    }


    /**
     *
     * @param page
     * @param key
     * @return
     */
    @Override
    public Vector<String> pageCur(int page, String key) {
        if(null == pageList || null == pageList.get(page) || pageList.get(page).size() == 0){
            if(characterTypesetting(key)){
                return pageList.get(page);
            }else{
                return null;
            }
        }
        return pageList.get(page);
    }


    /**
     *  对获取字符进行排版，并放入map中。
     * @param key
     * @return
     */
    @Override
    public boolean characterTypesetting(String key) {
        boolean isComplete = false;
        Map<Integer,Vector<String>> chapterMapData = autoSplitPage(ChapterLoader.getChapter(key));
        if(null != chapterMapData && chapterMapData.size() > 0){
            pageList.putAll(chapterMapData);
            isComplete = true;
        }
        return isComplete;
    }

    @Override
    public int getCountPage() {
        return pageList.size();
    }


}