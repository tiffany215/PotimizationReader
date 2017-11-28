package shuhai.readercore.ui.dialog;

import android.app.Dialog;
import android.content.Context;

import com.dyhdyh.widget.loading.factory.DialogFactory;
import shuhai.readercore.R;

/**
 * @author 55345364
 * @date 2017/9/28.
 */

public class LoadingCallback implements DialogFactory {

    @Override
    public Dialog onCreateDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.dialog_no_title);
        dialog.setContentView(R.layout.layout_loading);
        return dialog;
    }

    @Override
    public void setMessage(Dialog dialog, CharSequence message) {

    }

    @Override
    public int getAnimateStyleId() {
        return R.style.dialog_fade_away;
    }
}
