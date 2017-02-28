package com.shuhai.bookos.bean;

import java.util.List;

/**
 * Created by 55345364 on 2017/2/28.
 */

public class NewsInfo {

    public String reason;

    public Result result;

    public String error_code;

    public class Result {

        public int stat;

        public List<News> data;

        @Override
        public String toString() {
            return "Result{" +
                    "stat=" + stat +
                    ", data=" + data +
                    '}';
        }
    }


    public class News {
        public String uniquekey;
        public String title;
        public String data;
        public String category;
        public String author_name;
        public String url;
        public String thumbnail_pic_s;
        public String thumbnail_pic_s02;
        public String thumbnail_pic_s03;

        @Override
        public String toString() {
            return "News{" +
                    "uniquekey='" + uniquekey + '\'' +
                    ", title='" + title + '\'' +
                    ", data='" + data + '\'' +
                    ", category='" + category + '\'' +
                    ", author_name='" + author_name + '\'' +
                    ", url='" + url + '\'' +
                    ", thumbnail_pic_s='" + thumbnail_pic_s + '\'' +
                    ", thumbnail_pic_s02='" + thumbnail_pic_s02 + '\'' +
                    ", thumbnail_pic_s03='" + thumbnail_pic_s03 + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "NewsInfo{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                ", error_code='" + error_code + '\'' +
                '}';
    }
}
