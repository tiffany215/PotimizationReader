package shuhai.readercore.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import butterknife.OnClick;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private Context mContext;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        this.mContext = this;
//
//
//
//
//
//        BookApis.getInstance().obtainChapter(new ApiCallback<BookChapter>() {
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//
//            @Override
//            public void onError(ApiException e) {
//
//            }
//
//            @Override
//            public void onNext(BookChapter bookChapter) {
//
//            }
//        });
//
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void configViews() {
        this.mContext = this;
    }

    @OnClick(R.id.button)
    public void submit(View view){
        Toast.makeText(mContext, "dakaoi", Toast.LENGTH_SHORT).show();

        mContext.startActivity(new Intent(mContext,ReadActivity.class));

    }


    Handler handler = new Handler(){

        @Override
        public void dispatchMessage(Message msg) {
        }
    };






}




















