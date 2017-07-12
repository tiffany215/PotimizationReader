package shuhai.readercore;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class Constants {



    public static final String API_BASE_URL = "http://appdata.shuhai.com/ishuhai/";

    public static final String API_REST_URL = "servlet/onechapter";



    public static final long MAX_DISK_CACHE_SIZE = 10 * 1024 * 1024;

    public static final int MAX_MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 4);

    public static final int  DEFAULT_TIMEOUT = 60;




}
