package shuhai.readercore.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import shuhai.readercore.Constants;
import shuhai.readercore.bean.ChapterRead;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class BookApi  {


    private BookApiService service;


    public BookApi(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(BookApiService.class);
    }


    public synchronized Observable<ChapterRead> getChapterRead(String url){
        return service.getChapterRead(url);
    }





}
