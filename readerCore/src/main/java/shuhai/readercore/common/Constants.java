package shuhai.readercore.common;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class Constants {



    //    public static final String API_BASE_URL = "http://192.168.3.187:8089/ishuhai/";
    public static final String API_BASE_URL = "https://appdata.shuhai.com/ishuhai/";

    public static final String SHUHAIREAD_KEY = "shuhaiCache";

    public final static String KEY_SIGN = "yqP6SZMMRCchJAjPg0WtvOSxlpu0Hy"; // 系统秘钥

    public static final String BASE_WEB_URL = API_BASE_URL + "/app/";

    public static final long MAX_DISK_CACHE_SIZE = 10 * 1024 * 1024 * 50;

    public static final int MAX_MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 4);

    public static final int  DEFAULT_TIMEOUT = 60;


    public static final String PARAM = "";


    public static final int MARGIN_WIDTH = 40;
    public static final int MARGIN_HEIGHT = 25;




    public final static String SITEID  = "300";//ADNROID 300；单本（302）；winPhone 304；winphone单本 305



    public static final class FLIP_CONFIG{
        public final static int LEVEL_NO_FLIP = 0;
        public final static int LEVEL_SCROLLER_FLIP = 1;
        public final static int LEVEL_COVER_FLIP = 2;
        public final static int LEVEL_REAL_FLIP = 3;
    }

    /**
     * 章节类型 0代表章节 1代表分卷
     */
    public static final class CHAP_TYPE{
        public final static int CHAPTER = 0;
        public final static int VOLUME = 1;
    }


    public static final class RESPONSE_CODE{
        public final static String LOAD_SUCCESS = "0000";
        public final static String LOAD_FAILURE = "0001";
        public final static String NO_PRE_PAGE = "0017";
        public final static String NEED_BUY_CHAPTER = "0100";
    }


    public static final class LINE_SPACE{
        public final static float LINE_SPACE_SMALL = 0.8F;
        public final static float COMMON_LINE_AMONG = 1.0F;
        public final static float LOOSE_LINE_BIG = 1.2F;
    }




}
