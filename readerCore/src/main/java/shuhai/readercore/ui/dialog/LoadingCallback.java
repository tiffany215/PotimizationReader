package shuhai.readercore.ui.dialog;

import com.kingja.loadsir.callback.Callback;

import shuhai.readercore.R;

/**
 * @author 55345364
 * @date 2017/9/28.
 */

public class LoadingCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }
}
