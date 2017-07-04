package shuhai.readercore.api;



import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import shuhai.readercore.bean.ChapterRead;


/**
 * Created by 55345364 on 2017/7/4.
 */

public interface BookApiService {


    @GET("http://chapter2.zhuishushenqi.com/chapter/{url}")
    Observable<ChapterRead> getChapterRead(@Path("url") String url);





}
