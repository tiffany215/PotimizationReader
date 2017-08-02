package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    public static void main(String[] args){

        Schema schema = new Schema(1,"shuhai.readercore.dao");


        Entity bkbaseinfo = schema.addEntity("Bkbaseinfo");
        bkbaseinfo.addIntProperty("articleid").getProperty();
        bkbaseinfo.addStringProperty("articlename");
        bkbaseinfo.addStringProperty("author");
        bkbaseinfo.addStringProperty("bkbmurl");
        bkbaseinfo.addIntProperty("endtype");
        bkbaseinfo.addIntProperty("newchpname");
        bkbaseinfo.addStringProperty("owner");
        bkbaseinfo.addDateProperty("readtime");
        bkbaseinfo.addDateProperty("lastreadchporder");
        bkbaseinfo.addIntProperty("bktype");
        bkbaseinfo.addIntProperty("newchporder");
        

        Entity bkchpinfo = schema.addEntity("Bkchpinfo");
        bkchpinfo.addIntProperty("articleid");
        bkchpinfo.addIntProperty("chpid");
        bkchpinfo.addStringProperty("chpnamme");
        bkchpinfo.addIntProperty("chptype");
        bkchpinfo.addIntProperty("chiporder");



        Entity bkmark = schema.addEntity("Bkmark");
        bkmark.addIntProperty("articleid");
        bkmark.addIntProperty("chpid");
        bkmark.addIntProperty("begin");
        bkmark.addStringProperty("word");
        bkmark.addDateProperty("time");
        bkmark.addIntProperty("chporder");
        bkmark.addIntProperty("isfree");
        bkmark.addStringProperty("owner");
        bkmark.addStringProperty("chpname");
        

        

        try {
            new DaoGenerator().generateAll(schema,"readerCore/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
