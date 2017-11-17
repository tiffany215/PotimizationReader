package shuhai.readercore.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author 55345364
 * @date 2017/11/15.
 */

@Entity
public class BookEntity {

    @Id
    private Long id;

    @Unique
    private int articleId;//书Id

    private String articleName;//书名

    private String author;//作者

    @Transient
    private String intro;//书籍简介

    @Transient
    private String sort;//书籍分类

    private String cover;//书籍封面

    private int bookType;//书籍类型 本地;网络；

    private String owner;//拥有者

    private int fullFlag;//是否完结 1:完结 2:连载

    private int lastReadTime;//最后阅读时间

    private int lastReadChapterOrder;//最后阅读章节排序Id

    @Transient
    private int lastUpdateTime;//最后更新时间

    @Transient
    private int sizeC;//书籍字数

    @Transient
    private float sizeW;//以万为单位的书籍字数

    @Transient
    private int vote;

    @Transient
    private int visit;

    @Transient
    private int goodnum;//

    @Transient
    private int chapterCount;//总章节数

    @Transient
    private int latestChapterId;//最新章节Id

    private int latestChapterOrder;//最新章节排序号

    private String latestChatperName;//最新章节名称

    @Transient
    private String latestChapterName;//最新章节名

    @Generated(hash = 1123402938)
    public BookEntity(Long id, int articleId, String articleName, String author,
            String cover, int bookType, String owner, int fullFlag,
            int lastReadTime, int lastReadChapterOrder, int latestChapterOrder,
            String latestChatperName) {
        this.id = id;
        this.articleId = articleId;
        this.articleName = articleName;
        this.author = author;
        this.cover = cover;
        this.bookType = bookType;
        this.owner = owner;
        this.fullFlag = fullFlag;
        this.lastReadTime = lastReadTime;
        this.lastReadChapterOrder = lastReadChapterOrder;
        this.latestChapterOrder = latestChapterOrder;
        this.latestChatperName = latestChatperName;
    }

    @Generated(hash = 1373651409)
    public BookEntity() {
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

    public String getArticleName() {
        return this.articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getBookType() {
        return this.bookType;
    }

    public void setBookType(int bookType) {
        this.bookType = bookType;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getFullFlag() {
        return this.fullFlag;
    }

    public void setFullFlag(int fullFlag) {
        this.fullFlag = fullFlag;
    }

    public int getLastReadTime() {
        return this.lastReadTime;
    }

    public void setLastReadTime(int lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public int getLastReadChapterOrder() {
        return this.lastReadChapterOrder;
    }

    public void setLastReadChapterOrder(int lastReadChapterOrder) {
        this.lastReadChapterOrder = lastReadChapterOrder;
    }

    public int getLatestChapterOrder() {
        return this.latestChapterOrder;
    }

    public void setLatestChapterOrder(int latestChapterOrder) {
        this.latestChapterOrder = latestChapterOrder;
    }

    public String getLatestChatperName() {
        return this.latestChatperName;
    }

    public void setLatestChatperName(String latestChatperName) {
        this.latestChatperName = latestChatperName;
    }






}
