package shuhai.readercore.bean;

import java.io.Serializable;

import shuhai.readercore.bean.base.Base;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class ChapterRead  extends Base{

    public Chapter chapter;

    public static class Chapter implements Serializable {
        public String title;
        public String body;
        public String cpContent;

        public Chapter(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public Chapter(String title, String body, String cpContent) {
            this.title = title;
            this.body = body;
            this.cpContent = cpContent;
        }
    }

}
