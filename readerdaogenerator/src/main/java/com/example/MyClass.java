package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    public static void main(String[] args){

        Schema schema = new Schema(1,"shuhai.readercore.dao");


        Entity bookInfoEntity = schema.addEntity("BookInfoEntity");
        bookInfoEntity.addIntProperty("articleid");
        bookInfoEntity.addStringProperty("articlename");
        bookInfoEntity.addStringProperty("author");
        bookInfoEntity.addStringProperty("bkbmurl");
        bookInfoEntity.addIntProperty("endtype");
        bookInfoEntity.addStringProperty("newchpname");
        bookInfoEntity.addStringProperty("owner");
        bookInfoEntity.addIntProperty("readtime");
        bookInfoEntity.addIntProperty("lastreadchporder");
        bookInfoEntity.addIntProperty("bktype");
        bookInfoEntity.addIntProperty("newchporder");
        

        Entity chapterEntity = schema.addEntity("ChapterEntity");
        chapterEntity.addIntProperty("articleid");
        chapterEntity.addIntProperty("chpid");
        chapterEntity.addStringProperty("chpnamme");
        chapterEntity.addIntProperty("chptype");
        chapterEntity.addIntProperty("chiporder");



        Entity markEntity = schema.addEntity("MarkEntity");
        markEntity.addIntProperty("articleid");
        markEntity.addIntProperty("chpid");
        markEntity.addIntProperty("begin");
        markEntity.addStringProperty("word");
        markEntity.addIntProperty("time");
        markEntity.addIntProperty("chporder");
        markEntity.addIntProperty("isfree");
        markEntity.addStringProperty("owner");
        markEntity.addStringProperty("chpname");
        

        

        try {
            new DaoGenerator().generateAll(schema,"readerCore/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
