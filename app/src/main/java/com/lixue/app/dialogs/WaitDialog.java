package com.lixue.app.dialogs;

import android.app.ProgressDialog;
import android.content.Context;


/**
 * Created by enlong on 2017/2/13.
 */

public class WaitDialog extends ProgressDialog {
    public WaitDialog(Context context) {
        super(context);
    }

    public WaitDialog(Context context, int theme) {
        super(context, theme);
    }
}
