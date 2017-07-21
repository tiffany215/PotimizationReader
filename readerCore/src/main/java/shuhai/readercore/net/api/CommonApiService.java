package shuhai.readercore.net.api;



import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;
import shuhai.readercore.bean.BookChapter;


/**
 * Created by 55345364 on 2017/7/4.
 */

public interface CommonApiService {


    /**
     * get 请求
     * @param url
     * @param maps
     * @return
     */
    @GET
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String,String> maps);


    /**
     * 支持Multipart 的 post 请求。
     * @param entrance 入口地址
     * @param params 访问参数 只能是字符串键值对的类型
     * @return
     */
    @Multipart
    @POST("servlet/{entrance}")
    Observable<ResponseBody> postMultipart(@Path("entrance") String entrance,@PartMap Map<String,Object> params);




    /**
     * 支持form表单的post请求
     * @param url 访问地址
     * @param map   访问参数  只能是字符串键值对的类型
     * @return
     */
    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> postForm(@Url String url, @FieldMap Map<String,String> map);



    /**
     * 图片上传服务器
     * @param url
     * @param requestBody
     * @return
     */
    @Multipart
    @POST
    Observable<ResponseBody> uploadImage(@Url String url, @Part("image\"; filename=\"image" + ".jpg") RequestBody requestBody);

    /**
     * 单文件上传服务器
     * @param url
     * @param description
     * @param part
     * @return
     */
    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Url String url, @Part RequestBody description, @Part MultipartBody.Part part);

    /**
     * 多文件上传服务器
     * @param url
     * @param maps
     * @return
     */
    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(@Url String url, @PartMap  Map<String, RequestBody> maps);


    /**
     * 多文件上传服务器
     * @param path
     * @param parts
     * @return
     */
    @Multipart
    @POST
    Observable<ResponseBody> uploadFiles(@Url String path, @Part List<MultipartBody.Part> parts);



    @Multipart
    @POST("servlet/onechapter")
    Call<BookChapter> loadChapter(@PartMap Map<String,String> params);



}
