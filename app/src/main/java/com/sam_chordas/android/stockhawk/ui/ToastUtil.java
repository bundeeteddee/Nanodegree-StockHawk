package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by bundee on 8/19/16.
 */
public final class ToastUtil {

    public static final Toast ShowCentral(Context ctxt,
                                          @StringRes int stringRes){

        return Toast.makeText(ctxt, stringRes, Toast.LENGTH_SHORT);
    }



}
