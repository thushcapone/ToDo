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
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Contains all the view helpers
 */
public abstract class ViewUtils {
    
    private ViewUtils() {
    }
    
    public static void reset(TextView... views) {
        for (TextView view : views) {
            view.setText("");
        }
    }
    
    public static void setVisibility(Integer visibility, View... views) {
        ViewGroup parent = (ViewGroup) views[0].getRootView();
        TransitionManager.beginDelayedTransition(parent);
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }
    
    public static void underlineView(TextView... views) {
        for (TextView view : views) {
            view.setPaintFlags(view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
    
    public static void setEnabled(Boolean enabled, View... views) {
        for (View view : views) {
            view.setEnabled(enabled);
            view.setClickable(enabled);
        }
    }
    
    public static void runOnLayoutDone(final View view, final CallbackUtil callbackUtil) {
        ViewTreeObserver.OnGlobalLayoutListener l = new ViewTreeObserver.OnGlobalLayoutListener() {
            
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                callbackUtil.doAction();
            }
        };
        view.getViewTreeObserver().addOnGlobalLayoutListener(l);
    }
    
    /**
     * Schedules the shared element transition to be started immediately
     * after the shared element has been measured and laid out within the
     * activity's view hierarchy. Some common places where it might make
     * sense to call this method are:
     * <p>
     * (1) Inside a Fragment's onCreateView() method (if the shared element
     * lives inside a Fragment hosted by the called Activity).
     * <p>
     * (2) Inside a Glide Callback object (if you need to wait for Glide to
     * asynchronously load/scale a bitmap before the transition can begin).
     **/
    public static void scheduleStartPostponedTransition(final @NonNull Activity activity,
                                                        final @NonNull View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        ActivityCompat.startPostponedEnterTransition(activity);
                        return true;
                    }
                });
    }
    
}
