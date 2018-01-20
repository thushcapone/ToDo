/*
 * Copyright (c) 2017. SeneVideo
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tc.senevideos.app.AppController;

/**
 * Contains all the keyboard helpers
 */
public abstract class KeyboardUtils {
    
    private KeyboardUtils() {
    }
    
    /**
     * Hides the keyboard for the view specified. Used by fragment
     *
     * @param context
     * @param view
     */
    public static void hide(Context context, View view) {
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    
    /**
     * Hides the keyboard. Used by the activities
     *
     * @param context
     */
    public static void hide(Activity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams
                                                     .SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hide(context, context.getCurrentFocus());
    }
    
    public static void showFor(EditText edit) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            edit.setFocusable(true);
            edit.setFocusableInTouchMode(true);
            edit.requestFocus();
            InputMethodManager imm = (InputMethodManager) AppController.getInstance()
                                                                       .getApplicationContext()
                                                                       .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }
            imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
        }, 100);
    }
    
    public static void showForThenHide(Activity activity, EditText edit) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            edit.setFocusable(true);
            edit.setFocusableInTouchMode(true);
            edit.requestFocus();
            InputMethodManager imm = (InputMethodManager) AppController.getInstance()
                                                                       .getApplicationContext()
                                                                       .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }
            imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
            hide(activity);
        }, 100);
    }
    
}
