package shuhai.readercore;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import shuhai.readercore.api.BookApis;
import shuhai.readercore.bean.BookChapter;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.id_tv_result);



        BookApis.getInstance().obtainChapter(new ApiCallback<BookChapter>() {

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




    Handler handler = new Handler(){

        @Override
        public void dispatchMessage(Message msg) {
            tv.setText((CharSequence) msg.obj);
        }
    };






}




















