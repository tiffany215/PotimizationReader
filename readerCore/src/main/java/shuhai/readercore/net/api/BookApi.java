package shuhai.readercore.net.api;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shuhai.readercore.Constants;
import shuhai.readercore.bean.ChapterRead;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.func.ApiErrorFunc;
import shuhai.readercore.net.func.ApiFunc;
import shuhai.readercore.net.subscriber.ApiCallbackSubscriber;
import shuhai.readercore.utils.ClassUtils;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class BookApi  {

    private static Context mContext;
    private static BookApiService apiService;
    private static Retrofit retrofit;
    private static Retrofit.Builder retrofitBuild;
    private static OkHttpClient okHttpClient;
    private static OkHttpClient.Builder okHttpBuilder;


    public BookApi(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(BookApiService.class);
    }

    private BookApi(){

    }


    /**
     * 普通的post请求，需要传入实体类。
     * @param url
     * @param map
     * @param clz
     * @param <T>
     * @return
     */
    public <T> Observable<T> post(final String url, final Map<String,String> map,Class<T> clz){
        return apiService.post(url,map).compose(this.norTransFormer(clz));
    }


    /**
     * 普通POST方式请求，无需订阅，只需传入Callback回调
     * @param url
     * @param maps
     * @param callback
     * @param <T>
     * @return
     */
    public <T> Subscription post(final String url, final Map<String,String> maps, ApiCallback<T> callback){
        return this.post(url,maps, ClassUtils.getTClass(callback)).subscribe(new ApiCallbackSubscriber(mContext,callback));
    }



    /**
     * Observable 可以利用传入的 Transformer 对象的 call 方法直接对自身进行处理，不用重复性写多余代码。
     * @param clz
     * @param <T>
     * @return
     */
    private <T> Observable.Transformer<ResponseBody,T> norTransFormer(final Class<T> clz){
        return new Observable.Transformer<ResponseBody, T>() {
            @Override
            public Observable<T> call(Observable<ResponseBody> responseBodyObservable) {
                return responseBodyObservable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                        .mainThread()).map(new ApiFunc<T>(clz)).onErrorResumeNext(new ApiErrorFunc<T>());
            }
        };
    }



    /**
     * BookApi的所有配置通过构建者模式来创建。
     */
    public static final class Builder{
        private String baseUrl;

        public Builder(Context context){
            mContext = context;
            okHttpBuilder = new OkHttpClient.Builder();
            retrofitBuild = new Retrofit.Builder();
        }
        /**
         * 设置连接超时时间
         * @param timeout
         * @param unit
         * @return
         */
        public BookApi.Builder connectTimeOut(int timeout, TimeUnit unit){
            if(timeout > 1){
                okHttpBuilder.connectTimeout(timeout,unit);
            }else{
                okHttpBuilder.connectTimeout(Constants.DEFAULT_TIMEOUT,TimeUnit.SECONDS);
            }
            return this;
        }


        /**
         *  设置访问链接
         * @param url
         * @return
         */
        public BookApi.Builder baseUrl(String url){
            this.baseUrl = url;
            return this;
        }


        public BookApi build(){
            okHttpClient = okHttpBuilder.build();
            retrofitBuild.client(okHttpClient);
            retrofit = retrofitBuild.build();
            return new BookApi();
        }
    }

}
