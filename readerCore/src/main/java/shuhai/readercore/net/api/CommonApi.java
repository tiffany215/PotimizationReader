package shuhai.readercore.net.api;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shuhai.readercore.Constants;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.func.ApiErrorFunc;
import shuhai.readercore.net.func.ApiFunc;
import shuhai.readercore.net.interceptor.FixedParameterInterceptor;
import shuhai.readercore.net.mode.ApiHost;
import shuhai.readercore.net.subscriber.ApiCallbackSubscriber;
import shuhai.readercore.utils.ClassUtils;

/**
 * Created by 55345364 on 2017/7/4.
 */

public class CommonApi {

    private static Context mContext;
    private static CommonApiService apiService;

    private static Retrofit retrofit;
    private static Retrofit.Builder retrofitBuild;

    private static OkHttpClient okHttpClient;
    private static OkHttpClient.Builder okHttpBuilder;

    private CommonApi(){

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
        return apiService.postForm(url,map).compose(this.norTransFormer(clz));
    }


    /**
     * 普通的支持Multipart格式的post请求
     * @param entrance
     * @param params
     * @param clz
     * @param <T>
     * @return
     */
    public <T> Observable<T> postMultipart(final String entrance, final Map<String, RequestBody> params, Class<T> clz){
        return apiService.postMultipart(entrance,params).compose(this.norTransFormer(clz));
    }



    /**
     *  普通的get请求，需要传入实体类
     * @param url
     * @param map
     * @param clz
     * @param <T>
     * @return
     */
    public <T> Observable<T> get(final String url,final Map<String,String> map,Class<T> clz){
        return apiService.get(url,map).compose(this.norTransFormer(clz));
    }

    /**
     *
     * @param url
     * @param map
     * @param callback
     * @param <T>
     * @return
     */
    public <T> Subscription get(final String url,final Map<String,String> map,ApiCallback<T> callback){
        return this.get(url,map,ClassUtils.getTClass(callback)).subscribe(new ApiCallbackSubscriber(mContext,callback));
    }


    /**
     * 普通POST方式请求，无需订阅，只需传入Callback回调
     * @param <T>
     * @param url
     * @param maps
     * @param callback
     * @return
     */
    public <T> Subscription post(final String url, final Map<String,String> maps, ApiCallback<T> callback){
        return this.post(url,maps, ClassUtils.getTClass(callback)).subscribe(new ApiCallbackSubscriber(mContext,callback));
    }


    /**
     * 支持Multipart类型的POST方式请求，无需订阅，只需传入Callback回调
     * @param entrance
     * @param params
     * @param callback
     * @param <T>
     * @return
     */
    public <T>Subscription postMultipart(final String entrance,final Map<String,RequestBody> params,ApiCallback<T> callback){
        return this.postMultipart(entrance,params,ClassUtils.getTClass(callback)).subscribe(new ApiCallbackSubscriber(mContext,callback));
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


    private <T> Observable.Transformer<ResponseBody,T> norTransFormer1(final Class<T> clz){
        return new Observable.Transformer<ResponseBody, T>() {
            @Override
            public Observable<T> call(Observable<ResponseBody> responseBodyObservable) {
                return responseBodyObservable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                        .mainThread()).map(new ApiFunc<T>(clz)).onErrorResumeNext(new ApiErrorFunc<T>());
            }
        };
    }




    private static <T> T checkNotNull(T t,String message){
        if(null == t){
            throw new NullPointerException(message);
        }
        return t;
    }

    /**
     * BookApi的所有配置通过构建者模式来创建。
     */

    public static final class Builder{

        private String baseUrl;

//        private GsonConverterFactory gsonConverterFactory;
        private FastJsonConverterFactory fastJsonConverterFactory;

        private RxJavaCallAdapterFactory rxJavaCallAdapterFactory;
        private HttpLoggingInterceptor httpLoggingInterceptor;
        private FixedParameterInterceptor fixedParameterInterceptor;




        public Builder(Context context){
            mContext = context;
            okHttpBuilder = new OkHttpClient.Builder();
            retrofitBuild = new Retrofit.Builder();
        }


        /**
         * 设置求情baseUrl
         * @param url
         * @return
         */
        public CommonApi.Builder baseUrl(String url){
            this.baseUrl = checkNotNull(url,"baseUrl == null");
            return this;
        }


        /**
         * 设置网络连接超时时间
         * @param timeout
         * @param unit
         * @return
         */
        public CommonApi.Builder connectionTimeout(int timeout, TimeUnit unit){
            if(timeout > -1){
                okHttpBuilder.connectTimeout(timeout,unit);
            }else{
                okHttpBuilder.connectTimeout(Constants.DEFAULT_TIMEOUT,TimeUnit.SECONDS);
            }
            return this;
        }

        /**
         * 设置读取超时时间
         * @param timeOut
         * @param unit
         * @return
         */
        public CommonApi.Builder readTimeOut(int timeOut, TimeUnit unit){
            if(timeOut > -1){
                okHttpBuilder.readTimeout(timeOut,unit);
            }else{
                okHttpBuilder.readTimeout(Constants.DEFAULT_TIMEOUT,TimeUnit.SECONDS);
            }
            return this;
        }


        /**
         *  设置写入超时时间
         * @param timeout
         * @param unit
         * @return
         */
        public CommonApi.Builder writeTimeOut(int timeout, TimeUnit unit){
            if(timeout > -1){
                okHttpBuilder.writeTimeout(timeout,unit);
            }else{
                okHttpBuilder.writeTimeout(Constants.DEFAULT_TIMEOUT,TimeUnit.SECONDS);
            }
            return this;
        }

        public CommonApi.Builder addLogInterceptor(boolean flag,HttpLoggingInterceptor.Level level){
            if(flag){
                httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(level);
            }else{
                httpLoggingInterceptor = null;
            }
            return this;

        }

        public CommonApi build(){
            if(null == baseUrl){
                baseUrl = ApiHost.getHost();
            }
            retrofitBuild.baseUrl(baseUrl);

            if(null != httpLoggingInterceptor){
                okHttpBuilder.addInterceptor(httpLoggingInterceptor);
            }

//            if(null == fixedParameterInterceptor){
//                fixedParameterInterceptor = new FixedParameterInterceptor();
//                okHttpBuilder.addInterceptor(fixedParameterInterceptor);
//            }

            if(null == fastJsonConverterFactory){
                fastJsonConverterFactory =  FastJsonConverterFactory.create();
            }
            retrofitBuild.addConverterFactory(fastJsonConverterFactory);

            if(null == rxJavaCallAdapterFactory){
                rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
            }
            retrofitBuild.addCallAdapterFactory(rxJavaCallAdapterFactory);

            okHttpClient = okHttpBuilder.build();
            retrofitBuild.client(okHttpClient);
            retrofit = retrofitBuild.build();
            apiService = retrofit.create(CommonApiService.class);
            return new CommonApi();
        }
    }
}
