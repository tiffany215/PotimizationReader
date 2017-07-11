package shuhai.readercore;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class Constants {



    public static final String API_BASE_URL = "https://api.douban.com/v2/movie/top250/";

    public static final long MAX_DISK_CACHE_SIZE = 10 * 1024 * 1024;

    public static final int MAX_MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 4);

    public static final int  DEFAULT_TIMEOUT = 60;




}
