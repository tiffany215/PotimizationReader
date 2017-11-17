package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    public static void main(String[] args){

        Schema schema = new Schema(1,"shuhai.readercore.dao");


        Entity bookInfoEntity = schema.addEntity("BookInfoEntity");
        bookInfoEntity.addIntProperty("articleId").unique();
        bookInfoEntity.addStringProperty("articleName");
        bookInfoEntity.addStringProperty("author");
        bookInfoEntity.addStringProperty("bkUrl");
        bookInfoEntity.addIntProperty("endType");
        bookInfoEntity.addStringProperty("newChpName");
        bookInfoEntity.addStringProperty("owner");
        bookInfoEntity.addIntProperty("readTime");
        bookInfoEntity.addIntProperty("lastReadChpOrder");
        bookInfoEntity.addIntProperty("bkType");
        bookInfoEntity.addIntProperty("newChpOrder");
        

        Entity chapterEntity = schema.addEntity("ChapterEntity");
        chapterEntity.addIntProperty("articleId");
        chapterEntity.addIntProperty("chpId");
        chapterEntity.addStringProperty("chpName");
        chapterEntity.addIntProperty("chpType");
        chapterEntity.addIntProperty("chpOrder");



        Entity markEntity = schema.addEntity("MarkEntity");
        markEntity.addIntProperty("articleId");
        markEntity.addIntProperty("chpId");
        markEntity.addIntProperty("begin");
        markEntity.addStringProperty("word");
        markEntity.addIntProperty("time");
        markEntity.addIntProperty("chpOrder");
        markEntity.addIntProperty("isFree");
        markEntity.addStringProperty("owner");
        markEntity.addStringProperty("chpName");
        

        

        try {
            new DaoGenerator().generateAll(schema,"readerCore/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
