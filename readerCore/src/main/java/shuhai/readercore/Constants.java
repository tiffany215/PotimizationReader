package shuhai.readercore;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class Constants {



//    public static final String API_BASE_URL = "http://192.168.3.187:8089/ishuhai/";
    public static final String API_BASE_URL = "http://appdata.shuhai.com/ishuhai/";

    public static final String BASE_WEB_URL = API_BASE_URL + "/app/";

    public static final long MAX_DISK_CACHE_SIZE = 10 * 1024 * 1024 * 50;

    public static final int MAX_MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 4);

    public static final int  DEFAULT_TIMEOUT = 60;


    public static final String PARAM = "";


    public static final int MARGIN_WIDTH = 40;
    public static final int MARGIN_HEIGHT = 25;

    public static final String SHUHAIREAD_KEY = "shuhaiCache";



}
