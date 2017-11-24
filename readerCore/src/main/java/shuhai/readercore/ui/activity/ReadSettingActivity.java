package shuhai.readercore.ui.activity;

import android.view.View;
import android.widget.Button;

import butterknife.InjectView;
import shuhai.readercore.common.Constants;
import shuhai.readercore.R;
import shuhai.readercore.base.BaseActivity;
import shuhai.readercore.ui.sharedp.ReaderSP;

/**
 *
 * 阅读设置页面
 * @author 55345364
 * @date 2017/10/24.
 */

public class ReadSettingActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.level_cover_flip)
    public Button level_cover_flip;

    @InjectView(R.id.level_real_flip)
    public Button level_real_flip;

    @InjectView(R.id.level_scroller_flip)
    public Button level_scroller_flip;

    @InjectView(R.id.no_effect_flip)
    public Button no_effect_flip;


    @Override
    public int getLayoutId() {
        return R.layout.activity_reader_setting;
    }

    @Override
    public void initData() {
        level_cover_flip.setOnClickListener(this);
        level_real_flip.setOnClickListener(this);
        level_scroller_flip.setOnClickListener(this);
        no_effect_flip.setOnClickListener(this);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void configViews() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.level_real_flip:
                ReaderSP.getInstance().setFlipModel(Constants.FLIP_CONFIG.LEVEL_REAL_FLIP);
                break;
            case R.id.level_scroller_flip:
                ReaderSP.getInstance().setFlipModel(Constants.FLIP_CONFIG.LEVEL_SCROLLER_FLIP);
                break;
            case R.id.level_cover_flip:
                ReaderSP.getInstance().setFlipModel(Constants.FLIP_CONFIG.LEVEL_COVER_FLIP);
                break;
            case R.id.no_effect_flip:
                ReaderSP.getInstance().setFlipModel(Constants.FLIP_CONFIG.LEVEL_NO_FLIP);
                break;
        }
    }
}
