package shuhai.readercore.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author 55345364
 * @date 2017/7/19.
 */
@Entity
public class ChapterEntity {

    @Id
    private Long id;


    private int articleId;

    private int chapterId;

    @OrderBy
    private int chapterOrder;

    private String chapterName;

    private int chapterType;

    @Transient
    private int salePrice;

    @Transient
    private String sizeC;

    @Transient
    private int jsFree;

    @Transient
    private int payType;

    @Transient
    private String content;

    @Generated(hash = 696189237)
    public ChapterEntity(Long id, int articleId, int chapterId, int chapterOrder,
            String chapterName, int chapterType) {
        this.id = id;
        this.articleId = articleId;
        this.chapterId = chapterId;
        this.chapterOrder = chapterOrder;
        this.chapterName = chapterName;
        this.chapterType = chapterType;
    }

    @Generated(hash = 1142697545)
    public ChapterEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getArticleId() {
        return this.articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getChapterId() {
        return this.chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getChapterOrder() {
        return this.chapterOrder;
    }

    public void setChapterOrder(int chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    public String getChapterName() {
        return this.chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getChapterType() {
        return this.chapterType;
    }

    public void setChapterType(int chapterType) {
        this.chapterType = chapterType;
    }


    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public String getSizeC() {
        return sizeC;
    }

    public void setSizeC(String sizeC) {
        this.sizeC = sizeC;
    }

    public int getJsFree() {
        return jsFree;
    }

    public void setJsFree(int jsFree) {
        this.jsFree = jsFree;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
