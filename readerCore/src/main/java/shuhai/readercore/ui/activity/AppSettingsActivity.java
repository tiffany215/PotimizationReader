package shuhai.readercore.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.InjectView;
import shuhai.readercore.Constants;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.utils.AppUtils;

public class AppSettingsActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.action_settings_uf)
    public TextView action_settings_uf;

    @InjectView(R.id.action_settings_as)
    public TextView action_settings_as;

    @InjectView(R.id.action_settings_sp)
    public TextView action_settings_sp;

    @InjectView(R.id.action_settings_hc)
    public TextView action_settings_hc;

    @InjectView(R.id.action_settings_ctc)
    public  TextView action_settings_ctc;

    @InjectView(R.id.action_settings_rs)
    public TextView action_settings_rs;

    @InjectView(R.id.action_settings_sm)
    public TextView action_settings_sm;

    @InjectView(R.id.action_settings_vu)
    public TextView action_settings_vu;

    @InjectView(R.id.action_settings_dn)
    public  TextView action_settings_dn;

    @InjectView(R.id.action_settings_crc)
    public TextView action_settings_crc;

    @InjectView(R.id.action_settings_ts)
    public TextView action_settings_ts;


    @Override
    public int getLayoutId() {
        return R.layout.activity_app_setting;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void configViews() {
        action_settings_uf.setOnClickListener(this);
        action_settings_as.setOnClickListener(this);
        action_settings_sp.setOnClickListener(this);
        action_settings_hc.setOnClickListener(this);
        action_settings_ctc.setOnClickListener(this);
        action_settings_rs.setOnClickListener(this);
        action_settings_sm.setOnClickListener(this);
        action_settings_vu.setOnClickListener(this);
        action_settings_dn.setOnClickListener(this);
        action_settings_crc.setOnClickListener(this);
        action_settings_ts.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(mContext, BookAboutActivity.class);
                intent.putExtra(
                        "url",
                        AppUtils.makeUserUrl(Constants.BASE_WEB_URL
                                + "bookstore?op=search"));
                mContext.startActivity(intent);
                break;
            case R.id.action_home:

                break;

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.action_settings_uf:
                Toast.makeText(mContext, "1111", Toast.LENGTH_SHORT).show();
                
                break;
            case R.id.action_settings_as:
                break;
            case R.id.action_settings_sp:
                break;
            case R.id.action_settings_hc:
                break;
            case R.id. action_settings_crc:
                break;
            case R.id.action_settings_rs:
                break;
            case R.id.action_settings_sm:
                break;
            case R.id.action_settings_vu:
                break;
            case R.id. action_settings_dn:
                break;
            case R.id.action_settings_ctc:
                break;
            case R.id.action_settings_ts:
                break;


        }
    }
}
