package shuhai.readercore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import shuhai.readercore.R;

/**
 * @author 55345364
 * @date 2017/7/26.
 */

public class SplashActivity extends AppCompatActivity {


    @Bind(R.id.tvSkip)
    TextView tvSkip;

    private boolean flag = false;
    private Runnable runnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        runnable = new Runnable() {
            @Override
            public void run() {
                goHome();
            }
        };

        tvSkip.postDelayed(runnable,2000);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
    }


    private synchronized void goHome(){
        if(!flag){
            flag = true;
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = true;
        tvSkip.removeCallbacks(runnable);
        ButterKnife.unbind(this);
    }
}
