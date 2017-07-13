package shuhai.readercore.view.readview.strategy;

import android.graphics.Paint;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author 55345364
 * @date 2017/7/13.
 *
 * 横向滑动页面 文字排版具体实现
 *
 */

public class HorizantalComposing implements ComposingStrategy{

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
    private int marginHeight,marginWidth;

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


    private Map<Integer,Vector<String>> pageList = new HashMap<>();



    public HorizantalComposing(int width,int heigth,int fontSize ){

        mVisibleHeight = width - marginHeight * 2 - mNumFontSize * 2 - mLineSpace * 2;
        mVisibleWidth = width - marginWidth * 2;
        mLineSpace = mFontSize / 5 * 2;
        mPagelineCount = mVisibleHeight / (mFontSize + mLineSpace);


    }








    @Override
    public String[] autoSplitParagraph(String str) {

        if(TextUtils.isEmpty(str.trim())){
            return null;
        }
        return str.split("\r\t");
    }


    /**
     * 将段落内容分页
     */
    public void autoSplitPage(String str){
        String[] paragraphArr = autoSplitParagraph(str);
        Vector<String> lines = new Vector<>();
        int paraSpace = 0;
        //计算当前屏幕最多放置多少行文字
        mPagelineCount = mVisibleHeight / (mFontSize + mLineSpace);
        for (int i = 0; i <= paragraphArr.length; i++)
        {
            String strParagraph = paragraphArr[i];
            int lineCount = autoSplitLine(strParagraph).size();
            Vector<String> pageArr = autoSplitLine(strParagraph);
            while(lineCount < mPagelineCount && lineCount > 0){
                lineCount--;
                lines.add(pageArr.get(0));
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
            if(lineCount > mPagelineCount){
                pageList.put(1,lines);
                lines.clear();
                mPagelineCount = mVisibleHeight / (mFontSize + mLineSpace);
            }else{
                mPagelineCount = (mVisibleHeight - paraSpace - mParagraphSpace) / (mFontSize + mLineSpace);
            }
        }
    }


    /**
     * 将段落分成行
     * @param strParagraph
     * @return
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




    @Override
    public void pageUp() {





    }

    @Override
    public void pageDown() {

    }

    @Override
    public void prePage() {

    }

    @Override
    public void nextPage() {

    }
}
