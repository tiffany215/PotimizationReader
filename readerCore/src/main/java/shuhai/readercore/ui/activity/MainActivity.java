package shuhai.readercore.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import butterknife.OnClick;
import rx.Subscriber;
import shuhai.readercore.R;
import shuhai.readercore.api.BookApis;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.bean.BookChapter;
import shuhai.readercore.bean.ChapterEntity;
import shuhai.readercore.net.callback.ApiCallback;
import shuhai.readercore.net.exception.ApiException;

public class MainActivity extends BaseActivity {

    private Context mContext;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        this.mContext = this;
//
//        BookApis.getInstance().obtainChapter(new ApiCallback<ChapterEntity>() {
//
//            @Override
//            public void onStart() {
//                Toast.makeText(mContext, "onStart--->", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onComplete() {
//                Toast.makeText(mContext, "onComplete--->", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(ApiException e) {
//                Toast.makeText(mContext, "ApiException--->"+e.getDisplayMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNext(ChapterEntity bookChapter) {
//                Toast.makeText(mContext, "Book--->"+bookChapter.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void submit(View view){
        mContext.startActivity(new Intent(mContext,ReadActivity.class));
    }

}




















