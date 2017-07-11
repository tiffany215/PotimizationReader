package shuhai.readercore;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import shuhai.readercore.api.BookApis;
import shuhai.readercore.bean.base.MovieEntity;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;

public class MainActivity extends AppCompatActivity {


    private OkHttpClient okHttpClient;
    private final static String mBaseUrl = "http://appdata.shuhai.com/ishuhai/servlet/";
    //    private final static String mBaseUrl = "http://192.168.3.187:8089/ishuhai/servlet/";
    private TextView ResponseResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = new OkHttpClient();
        ResponseResult = (TextView) findViewById(R.id.id_tv_result);
        doPostForm(ResponseResult);
    }


    /**
     * get请求
     *
     * @param view
     */
    public void doGetRequest(View view) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "login?userName=wangxu&passWord=123").build();
        executeRequest(request);

    }


    /**
     * post请求
     *
     * @param view
     */
    public void doPostRequest(View view) {
        FormBody.Builder body = new FormBody.Builder();
        body.add("packagename", "com.shuhai.bookos");
        body.add("sign", "e98a726ae2fd27c229515428de425f59");
        body.add("source", "shuhai");
        body.add("username", "wangxu@qq.com");
        body.add("imei", "356261050497138");
        body.add("pass", "e10adc3949ba59abbe56e057f20f883e");
        body.add("version", "56");
        body.add("uuid", "00000000-35b8-f815-39da-21960033c587");
        body.add("ip", "192.168.1.185");
        body.add("siteid", "300");
        body.add("timestamp", "1483074883");
        body.add("uid", "202533");
        FormBody formBody = body.build();
        Request.Builder builder1 = new Request.Builder();
        Request request = builder1.url(mBaseUrl + "login").post(formBody).build();
        executeRequest(request);
    }


    /**
     * 向服务器发送字符串
     *
     * @param view
     */
    public void doPostString(View view) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), "{'userName':'tiffany','passWold':'123456'}");
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "requestString").post(requestBody).build();
        executeRequest(request);
    }


    /**
     * 向服务器发送文件。
     * @param view
     */
    public void doPostFile(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "temp_head_image.jpg");
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "requestString").post(requestBody).build();

        executeRequest(request);

    }


    public void doPostForm(View view){
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("packageame", "com.shuhai.bookos")
                .addFormDataPart("sign", "e98a726ae2fd27c229515428de425f59")
                .addFormDataPart("source", "shuhai")
                .addFormDataPart("username", "wangxu@qq.com")
                .addFormDataPart("imei", "356261050497138")
                .addFormDataPart("pass", "e10adc3949ba59abbe56e057f20f883e")
                .addFormDataPart("version", "56")
                .addFormDataPart("uuid", "00000000-35b8-f815-39da-21960033c587")
                .addFormDataPart("ip", "192.168.1.185")
                .addFormDataPart("siteid", "300")
                .addFormDataPart("timestamp", "1483074883")
                .addFormDataPart("uid", "202533")
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "login").post(requestBody).build();
        executeRequest(request);
    }



    /**
     * 执行请求
     *
     * @param request
     */


    private void executeRequest(Request request){
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });


    }



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ResponseResult.setText(msg.obj.toString());
        }
    };
}
