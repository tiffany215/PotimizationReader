package shuhai.readercore.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author 55345364
 * @date 2017/11/15.
 */
@Entity
public class BookMarkEntity {

    @Id
    private Long id;

    private int articleId;

    private int chapterId;

    private int begin;

    private String word;

    private int time;

    private int chapterOrder;

    private int isFree;

    private String owner;

    private String chapterName;

    @Generated(hash = 1956563056)
    public BookMarkEntity(Long id, int articleId, int chapterId, int begin,
            String word, int time, int chapterOrder, int isFree, String owner,
            String chapterName) {
        this.id = id;
        this.articleId = articleId;
        this.chapterId = chapterId;
        this.begin = begin;
        this.word = word;
        this.time = time;
        this.chapterOrder = chapterOrder;
        this.isFree = isFree;
        this.owner = owner;
        this.chapterName = chapterName;
    }

    @Generated(hash = 1463584955)
    public BookMarkEntity() {
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

    public int getBegin() {
        return this.begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getChapterOrder() {
        return this.chapterOrder;
    }

    public void setChapterOrder(int chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    public int getIsFree() {
        return this.isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getChapterName() {
        return this.chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }


}
