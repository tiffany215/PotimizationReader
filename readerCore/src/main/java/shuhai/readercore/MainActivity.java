package shuhai.readercore;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shuhai.readercore.api.BookApis;
import shuhai.readercore.bean.BookChapter;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";

    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.id_tv_result);
//        postIpInformation();


        BookApis.getInstance().obtianChapter(new ApiCallback<BookChapter>() {


            @Override
            public void onStart() {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onNext(BookChapter bookChapter) {

            }
        });

    }

    /**
     * 普通get请求
     */
//    private void postIpInformation(){
//        String url = "http://appdata.shuhai.com/ishuhai/";
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
//        ChapterServiceForPost ipService = retrofit.create(ChapterServiceForPost.class);
//
//        Map<String,String> parms = new HashMap<>();
//        parms.put("packageame", "com.shuhai.bookos");
//        parms.put("order", "3");
//        parms.put("sign", "0e7dd35dac1196a3096ad3796652d8dc");
//        parms.put("source", "shuhai");
//        parms.put("uuid", "00000000-35b8-f815-39da-21960033c587");
//        parms.put("siteid", "300");
//        parms.put("timestamp", "1499765520");
//        parms.put("chapterorder", "6");
//        parms.put("username", "wangxu@qq.com");
//        parms.put("imei", "356261050497138");
//        parms.put("version", "60");
//        parms.put("articleid", "41366");
//        parms.put("ip", "192.168.1.190");
//        parms.put("uid", "0");
//        parms.put("chapterid", "2642855");
//
//        Call<BookChapter> call = ipService.getChapterMsg(parms);
//
//        call.enqueue(new Callback<BookChapter>() {
//            @Override
//            public void onResponse(Call<BookChapter> call, Response<BookChapter> response) {
//
//                Message message = new Message();
//                message.obj = response.body().getCode().toString();
//                handler.sendMessage(message);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<BookChapter> call, Throwable t) {
//
//            }
//        });
//
//    }



    Handler handler = new Handler(){

        @Override
        public void dispatchMessage(Message msg) {
            tv.setText((CharSequence) msg.obj);
        }
    };






}




















