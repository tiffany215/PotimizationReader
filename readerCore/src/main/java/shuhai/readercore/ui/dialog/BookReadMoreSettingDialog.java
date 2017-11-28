package shuhai.readercore.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import shuhai.readercore.R;
import shuhai.readercore.common.Constants;
import shuhai.readercore.manager.ThemeManager;
import shuhai.readercore.ui.activity.AppSettingsActivity;
import shuhai.readercore.ui.sharedp.ReaderSP;
import shuhai.readercore.utils.ToastUtils;
import shuhai.readercore.view.readview.factory.PageFactory;

/**
 * @author 55345364
 * @date 2017/11/21.
 */

public class BookReadMoreSettingDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private PageFactory mPageFactory;

    private ImageView[] readThemeViews;
    private int[] readThemeIds;
    private int[] readThemeParams;

    private ImageView[] lineSpaceView;
    private int[] lineSpaceId;
    private float[] lineSpaceParams;

    private int size = 32; // 字体大小

    public BookReadMoreSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private void init() {
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

    protected BookReadMoreSettingDialog(Context context,PageFactory pageFactory) {
        this(context, R.style.dialog_from_bottom);
        this.mPageFactory = pageFactory;
        size = ReaderSP.getInstance().getTextSize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_more_setting);
        initValues();
        initView();
    }



    private void initValues() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dms = mContext.getResources().getDisplayMetrics();
        lp.width = dms.widthPixels;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }


    private void initView() {
        findViewById(R.id.theme_more).setOnClickListener(this);
        findViewById(R.id.read_size_decrease).setOnClickListener(this);
        findViewById(R.id.read_size_add).setOnClickListener(this);
        findViewById(R.id.set_more).setOnClickListener(this);

        //初始化主题模式、、日见夜间护眼等模式
        readThemeIds = new int[] { R.id.read_style_day_text,
                R.id.read_style_blue_text, R.id.read_style_eye_text,
                R.id.read_style_parchment_text};
        readThemeParams = new int[]{ThemeManager.READ_STYLE_THEME_01,ThemeManager.READ_STYLE_THEME_04,ThemeManager.READ_STYLE_THEME_06,ThemeManager.READ_STYLE_THEME_09};
        readThemeViews = new ImageView[readThemeIds.length];

        for (int i = 0; i < readThemeIds.length; i++) {
            readThemeViews[i] = findViewById(readThemeIds[i]);
            readThemeViews[i].setOnClickListener(new ReadStyleOnClickListener());
        }

        for (int i = 0; i < readThemeIds.length; i++) {
            if(ReaderSP.getInstance().getReaderTheme() == i){
                readThemeViews[i].setSelected(true);
            }
        }

        //初始换行间距
        lineSpaceId = new int[] {R.id.line_spacing_big,R.id.line_spacing_among,R.id.line_spacing_small};
        lineSpaceView = new ImageView[lineSpaceId.length];
        lineSpaceParams = new float[]{Constants.LINE_SPACE.LINE_SPACE_SMALL, Constants.LINE_SPACE.COMMON_LINE_AMONG, Constants.LINE_SPACE.LOOSE_LINE_BIG};
        for (int i = 0; i < lineSpaceId.length; i++) {
            lineSpaceView[i] = findViewById(lineSpaceId[i]);
            lineSpaceView[i].setOnClickListener(new LineSpaceOnClickListener());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_size_decrease:
                setFontSize(false);
                break;
            case R.id.read_size_add:
                setFontSize(true);
                break;
            case R.id.theme_more:
                new BookReadThemeSettingDialog(mContext,mPageFactory).show();
                dismiss();
                break;
            case R.id.gl_real_flip:
                ReaderSP.getInstance().setFlipModel(Constants.FLIP_CONFIG.LEVEL_REAL_FLIP);
                dismiss();
                break;
            case R.id.hor_scroller_flip:
                ReaderSP.getInstance().setFlipModel(Constants.FLIP_CONFIG.LEVEL_SCROLLER_FLIP);
                dismiss();
                break;
            case R.id.cover_scroller_flip:
                ReaderSP.getInstance().setFlipModel(Constants.FLIP_CONFIG.LEVEL_COVER_FLIP);
                dismiss();
                break;
            case R.id.no_effect_flip:
                ReaderSP.getInstance().setFlipModel(Constants.FLIP_CONFIG.LEVEL_NO_FLIP);
                dismiss();
                break;
            case R.id.set_more:
                mContext.startActivity(new Intent(mContext, AppSettingsActivity.class));
                dismiss();
                break;


        }
    }


    /**
     * 设置字体大小
     * @param isAdd
     */
    private void setFontSize(boolean isAdd) {
        if(isAdd){
            if (size > mContext.getResources().getDimension(
                    R.dimen.chapter_font_max_size)) {
                Toast.makeText(mContext,R.string.max_imun, Toast.LENGTH_SHORT);
                return;
            }
            size += 2;
        }else{
            if (size < mContext.getResources().getDimension(R.dimen.chapter_font_mix_size)) {
                Toast.makeText(mContext,R.string.mix_imun, Toast.LENGTH_SHORT);
                return;
            }
            size -= 2;
        }
        ReaderSP.getInstance().setTextSize(size);
        mPageFactory.setTextSize(size);
    }


    /**
     * 行间距按钮点击监听事件
     * @author Wangxu
     *
     */
    private class LineSpaceOnClickListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < lineSpaceId.length; i++) {
                if(v == lineSpaceView[i]){
                    lineSpaceView[i].setSelected(true);
                    ReaderSP.getInstance().setLineSpace(lineSpaceParams[i]);
                }else{
                    lineSpaceView[i].setSelected(false);
                }
            }
            mPageFactory.setLineSpace(ReaderSP.getInstance().getLineSpace());
        }
    }


    /**
     * 主题设置按钮点击监听事件
     * @author Wangxu
     *
     */
    private class ReadStyleOnClickListener implements android.view.View.OnClickListener {

        @Override
        public void onClick(View v) {
            for (int i = 0; i < readThemeIds.length; i++) {
                if (v == readThemeViews[i]) {
                    readThemeViews[i].setSelected(true);
                    ReaderSP.getInstance().setReaderTheme(readThemeParams[i]);
                } else {
                    readThemeViews[i].setSelected(false);
                }
            }
            mPageFactory.setTheme(ReaderSP.getInstance().getReaderTheme());
        }
    }



}
