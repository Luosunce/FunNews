package com.yone.funnews.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Yoe on 2016/10/10.
 */

public class SnackbarUtil {

    public static void show(View view,String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_LONG).show();
    }

    public static void showShort(View view,String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
    }
}
