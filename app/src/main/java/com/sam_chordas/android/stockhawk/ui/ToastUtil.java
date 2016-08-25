package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by bundee on 8/19/16.
 */
public final class ToastUtil {

    public static final Toast ShowCentral(Context ctxt,
                                          @StringRes int stringRes){
        Toast toast = Toast.makeText(ctxt, stringRes, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        return toast;
    }

    public static final Toast ShowBottom(Context ctxt,
                                          @StringRes int stringRes){
        return Toast.makeText(ctxt, stringRes, Toast.LENGTH_SHORT);
    }

    public static final Toast ShowBottom(Context ctxt,
                                         String message){
        return Toast.makeText(ctxt, message, Toast.LENGTH_SHORT);
    }



}
