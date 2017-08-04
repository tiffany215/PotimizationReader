package shuhai.readercore.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MARK_ENTITY".
 */
public class MarkEntity {

    private Integer articleid;
    private Integer chpid;
    private Integer begin;
    private String word;
    private Integer time;
    private Integer chporder;
    private Integer isfree;
    private String owner;
    private String chpname;

    public MarkEntity() {
    }

    public MarkEntity(Integer articleid, Integer chpid, Integer begin, String word, Integer time, Integer chporder, Integer isfree, String owner, String chpname) {
        this.articleid = articleid;
        this.chpid = chpid;
        this.begin = begin;
        this.word = word;
        this.time = time;
        this.chporder = chporder;
        this.isfree = isfree;
        this.owner = owner;
        this.chpname = chpname;
    }

    public Integer getArticleid() {
        return articleid;
    }

    public void setArticleid(Integer articleid) {
        this.articleid = articleid;
    }

    public Integer getChpid() {
        return chpid;
    }

    public void setChpid(Integer chpid) {
        this.chpid = chpid;
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getChporder() {
        return chporder;
    }

    public void setChporder(Integer chporder) {
        this.chporder = chporder;
    }

    public Integer getIsfree() {
        return isfree;
    }

    public void setIsfree(Integer isfree) {
        this.isfree = isfree;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getChpname() {
        return chpname;
    }

    public void setChpname(String chpname) {
        this.chpname = chpname;
    }

}