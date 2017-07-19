package shuhai.readercore.view.readview.strategy;

import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import shuhai.readercore.manager.ChapterLoader;

/**
 * @author 55345364
 * @date 2017/7/13.
 *
 * 横向滑动页面 文字排版具体实现
 *
 */

public class HorizontalComposing implements ComposingStrategy{

    private static final String TAG = "HorizontalComposing";
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





    public HorizontalComposing(int width, int height, int fontSize,Paint paint,Paint titlePaint){
        mWidth = width;
        mHeight = height;
        mFontSize = fontSize;
        mVisibleHeight = mWidth - marginHeight * 2 - mNumFontSize * 2 - mLineSpace * 2;
        mVisibleWidth = mWidth - marginWidth * 2;
        mLineSpace = mFontSize / 5 * 2;
        mPagelineCount = mVisibleHeight / (mFontSize + mLineSpace);
        mPaint = paint;
        mTitlePaint = titlePaint;
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
    private Map<Integer,Vector<String>> autoSplitPage(String str){
        String[] paragraphArr = autoSplitParagraph(str);
        Vector<String> lines = new Vector<>();
        int paraSpace = 0;
        int pageSize = 0;
        //计算当前屏幕最多放置多少行文字
        mPagelineCount = mVisibleHeight / (mFontSize + mLineSpace);
        for (int i = 0; i < paragraphArr.length; i++)
        {
            String strParagraph = paragraphArr[i];
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
                    paraSpace += (mLineSpace + mFontSize);
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
//                    for (int j = 0; j < lines.size(); j++) {
//                        Log.e(TAG, "autoSplitPage: ---------------->>"+lines.get(j));
//                    }
                    lines.removeAllElements();
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
    public Vector<String> pageUp(int page, String key) {
        Map<Integer,Vector<String>> pageContent = autoSplitPage(ChapterLoader.getChapter(key));
        if(null != pageContent && pageContent.size() > 0){
            return pageContent.get(page);
        }
        return null;
    }

    @Override
    public Vector<String> pageDown(int page, String key) {
        Map<Integer,Vector<String>> pageContent = autoSplitPage(ChapterLoader.getChapter(key));
        if(null != pageContent && pageContent.size() > 0){
            return pageContent.get(page);
        }
        return null;
    }


    @Override
    public Vector<String> pageCurr(int page, String key) {
        String str = "    第19章：面试遇故友\n" +
                "\n" +
                "    与慕溪颜一起跑步的第二天，李倾城就去一家公司接受了面试，然而看着那巍峨的建筑物，进到里面，看着里面的员工一派商业精英的派头，李倾城是真的有些露怯。\n" +
                "\n" +
                "    这样的大公司，真的会招自己这个什么都不会的人吗？\n" +
                "\n" +
                "    李倾城已经重新打印了一份简历，简历上面，曾经是舞者的信息被李倾城给掩盖了下去，而缺失了这一个正经的工作经验的李倾城的工作简历，基本上就是毫无亮点了。\n" +
                "\n" +
                "    李倾城进去面试的时候，人事部的经理看了看李倾城的简历，顿觉索然无味，只能象征性的问了几个问题，就让李倾城离开了。\n" +
                "\n" +
                "    李倾城自己也觉得根本没希望了，一点点的往外走着。\n" +
                "\n" +
                "    大公司面试的人多，排队面试都要排很久，有些人条件好一些，负责面试的人会尽量挖掘这些人的才能，也会多聊几句。有些人进去，十分钟不出来，像李倾城这种进去没二分钟就出来了，高下立见。\n" +
                "\n" +
                "    看着外面依然还有不少的面试者排队，李倾城背着自己的包，慢慢的往公司外面走着。\n" +
                "\n" +
                "    没找到工作，李倾城自然觉得失望，可是，慕溪颜给介绍的机会，自己却没把握住，李倾城觉得愧对慕溪颜的指点。\n" +
                "\n" +
                "    “倾城？”李倾城快走到楼下门口的时候，身后却传来一个女人的声音，李倾城觉得自己在这里也没有认识的人，应该是重名，也没理会，继续头也不回的往前走。\n" +
                "\n" +
                "    “倾城？李倾城？”女人继续喊道。\n" +
                "\n" +
                "    李倾城这才回头看了一眼，这一回头，就看到了一个熟悉的身影：“楚柠？”\n" +
                "\n" +
                "    名为楚柠的女人一身干练西装，长发也被盘了起来，前凸后翘，看模样是格外顺眼又格外好相处的类型。看到李倾城回头，楚柠快速的走了过来，抓住了李倾城的手腕：“真的是你啊，你怎么在这里？”\n" +
                "\n" +
                "    李倾城有些尴尬的开口：“我和宇辰离婚了，离婚后，我也得自力更生，所以出来找工作，我今天是来面试的，只是，面试结果，差强人意……”\n" +
                "\n" +
                "    “离婚？！”楚柠震惊的看着李倾城：“好端端的干嘛离婚？你和范学长的感情不是一直很好吗？”\n" +
                "\n" +
                "    楚柠是李倾城大学四年的室友，大学的时候，两个人因为脾气相近性格相投，所以感情很好，楚柠一直是个直来直去的姑娘，不喜欢拐弯抹角，而那时的李倾城也是如此，只是经历过婚姻之后，明显的，李倾城比之前沉静了很多：“初期感情还不错，只是结了婚，有了家庭，有了孩子，一切就都变了，我们两个人的生活，真真切切的体现了什么叫贫贱夫妻百事衰……前阵子儿子也死了，再也过不下去了，就离婚了……”\n" +
                "\n" +
                "    “宝宝死了？！”楚柠更震惊了，楚柠一直觉得，李倾城是她们宿舍里过得最幸福的一个了吧？大学时就是媲美校花的存在，追求者众多，大学毕业没多久，就跟相爱了很多年的学长结了婚，楚柠一直觉得她应该是幸福的不要不要的。楚柠有些心疼的看着李倾城，转移话题道：“你刚才说来我们公司面试？”\n" +
                "\n" +
                "    “嗯！”李倾城回答道。\n" +
                "\n" +
                "    楚柠很快想起公司内部的人事调动，新公司要开，经理要调走，招人很正常，楚柠好奇的问道：“你应聘的哪个部门啊？”\n" +
                "\n" +
                "    “现在的我……”李倾城看看自己的手：“在家待了两年，以前会的东西基本全忘了，所以，我应聘的是后勤处，干些杂活，想一点点的来。”\n" +
                "\n" +
                "    楚柠想想以前的李倾城长得好，性格也好，更是能跳的一身的好舞蹈，据说从小就是练舞蹈的，当时还是学校舞蹈社团的骨干呢，“倾城，你最擅长的应该不是这些吧，为什么不去找一份适合自己的工作呢？比如教人跳舞？现在的特长班那么多，你教人跳舞比在这里打杂强啊。”\n" +
                "\n" +
                "    李倾城苦笑着看着楚柠：“你看我肚子上的这一圈肉，哪家的舞蹈老师能跟我似的那么胖，身条都没恢复好，毫无说服力。”\n" +
                "\n" +
                "    楚柠这才仔细看了看李倾城，确实以前的小蛮腰已经不见了，取而代之的是有些松垮的腰部赘肉，皮肤也黯淡松弛不少，一看就是已经好久没有好好的保养了，楚柠觉得自己又揭了别人的伤疤，很抱歉的开口道：“对不起啊，我这个人，说话总是这么没脑子。”\n" +
                "\n" +
                "    “没关系的，我知道你是为了我好。”李倾城依然保持着曾经的善良与宽容，只是想起这些日子的境遇，李倾城虽然极力压制悲伤的情绪，可眼眶还是有些泛红。\n" +
                "\n" +
                "    “倾城……”楚柠担忧的开口，赶忙在自己的包里掏出纸巾递给李倾城，“你没事吧？”\n" +
                "\n" +
                "    “我没事，只是……想起了死去的孩子……”李倾城擦着眼泪：“不知道他在另外一个世界，过得好不好……”\n" +
                "\n" +
                "    李倾城的眼里满是泪水，楚柠看着她悲伤地表情，有种感同身受的感觉，伸手拍了拍李倾城的肩膀：“都会过去的，真的，对了，倾城，你手机号没变吧？等我晚上下班，我请你喝酒。我知道一家酒很好喝的酒吧。”\n" +
                "\n" +
                "    楚柠向来不会安慰人，没有过的人生经历，楚柠也不敢贸然开口劝说，李倾城现在的神经已经很敏感了，楚柠不知道自己的粗神经那句话会刺激到她，只能跟她说一些无关紧要的话。\n" +
                "\n" +
                "    “喝酒？”李倾城是不会喝酒的，以前在学校时，偶尔也会喝一些酒，但是李倾城的酒量极差，想想这阵子过得也很压抑，李倾城点点头：“好，那你告诉我地址，等你快下班的时候，我直接过去。”\n" +
                "\n" +
                "    “等会儿啊。”楚柠快速的用自己怀里的纸笔在纸上写下一个地址和一串号码，撕下一片纸，递给李倾城：“这个是地址，我五点半就下班，我大概半小时就能到。”\n" +
                "\n" +
                "    “好。”李倾城点点头，把这张纸条收了起来：“那你去上班，我们晚上见。”\n" +
                "\n" +
                "    从楚柠工作的大厦离开，李倾城没有急着回家，而是拿出自己早前做的笔记，按照地址，继续去找工作了。";

        Map<Integer,Vector<String>> pageContent = autoSplitPage(str);

        for (int i = 1; i <pageContent.size() ; i++) {

            Vector<String> list = pageContent.get(i);

            for (int j = 0; j < list.size(); j++) {

                Log.e(TAG, "pageCurr: "+list.get(i));
            }


        }


//        Map<Integer,Vector<String>> pageContent = autoSplitPage(ChapterLoader.getChapter(key));
        if(null != pageContent && pageContent.size() > 0){
            return pageContent.get(page);
        }
        return null;
    }
}
