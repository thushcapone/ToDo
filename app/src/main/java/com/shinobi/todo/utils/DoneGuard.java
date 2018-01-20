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

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Based on the ClickGuard to make a guard for the done button click
 */
public abstract class DoneGuard {
    
    /**
     * Default watch period in millis.
     */
    private static final long DEFAULT_WATCH_PERIOD_MILLIS = 1000L;
    
    private DoneGuard() {
    }
    
    // ---------------------------------------------------------------------------------------------
    //                                  Utility methods start
    // ---------------------------------------------------------------------------------------------
    
    /**
     * Utility method. Create a DoneGuard with specific watch period: {@code watchPeriodMillis}.
     *
     * @return The created DoneGuard instance.
     */
    public static DoneGuard newGuard(long watchPeriodMillis) {
        return new DoneGuard.DoneGuardImpl(watchPeriodMillis);
    }
    
    /**
     * Utility method. Use a new DoneGuard with default watch period {@link
     * #DEFAULT_WATCH_PERIOD_MILLIS}
     * to guard View(s).
     *
     * @param view   The View to be guarded.
     * @param others More views to be guarded.
     * @return The created ClickedGuard.
     */
    public static DoneGuard guard(TextView view, TextView... others) {
        return guard(DEFAULT_WATCH_PERIOD_MILLIS, view, others);
    }
    
    /**
     * Utility method. Use a new DoneGuard with specific guard period {@code watchPeriodMillis} to
     * guard View(s).
     *
     * @param watchPeriodMillis The specific watch period.
     * @param view              The View to be guarded.
     * @param others            More Views to be guarded.
     * @return The created ClickedGuard.
     */
    public static DoneGuard guard(long watchPeriodMillis, TextView view, TextView... others) {
        return guard(newGuard(watchPeriodMillis), view, others);
    }
    
    /**
     * Utility method. Use a specific DoneGuard {@code guard} to guard View(s).
     *
     * @param guard  The DoneGuard used to guard.
     * @param view   The View to be guarded.
     * @param others More Views to be guarded.
     * @return The given ClickedGuard itself.
     */
    public static DoneGuard guard(DoneGuard guard, TextView view, TextView... others) {
        return guard.addAll(view, others);
    }
    
    /**
     * Utility method. Get the DoneGuard from a guarded View.
     *
     * @param view A View guarded by DoneGuard.
     * @return The DoneGuard which guards this View.
     */
    public static DoneGuard get(TextView view) {
        TextView.OnEditorActionListener listener = retrieveOnEditorActionListener(view);
        if (listener instanceof DoneGuard.GuardedOnEditorActionListener) {
            return ((DoneGuard.GuardedOnEditorActionListener) listener).getDoneGuard();
        }
        throw new IllegalStateException(
                "The view (id: 0x" + view.getId() + ") isn't guarded by DoneGuard!");
    }
    
    /**
     * Utility method. Retrieve
     * {@linkplain TextView.OnEditorActionListener OnEditorActionListener} from
     * a View.
     *
     * @param view The View used to retrieve.
     * @return The retrieved {@linkplain TextView.OnEditorActionListener OnEditorActionListener}.
     */
    public static TextView.OnEditorActionListener retrieveOnEditorActionListener(TextView view) {
        if (view == null) {
            throw new NullPointerException("Given view is null!");
        }
        return DoneGuard.ListenerGetter.get(view);
    }
    
    // ---------------------------------------------------------------------------------------------
    //                                  Utility methods end
    // ---------------------------------------------------------------------------------------------
    
    /**
     * Let a view to be guarded by this DoneGuard.
     *
     * @param view The view to be guarded.
     * @return This DoneGuard instance.
     * @see #addAll(TextView, TextView...)
     */
    public DoneGuard add(TextView view) {
        if (view == null) {
            throw new IllegalArgumentException("View shouldn't be null!");
        }
        TextView.OnEditorActionListener listener = retrieveOnEditorActionListener(view);
        if (listener == null) {
            throw new IllegalStateException("Haven't set an OnEditorActionListener to View (id: 0x"
                                                    + Integer.toHexString(view.getId())
                                                    + ")!");
        }
        view.setOnEditorActionListener(wrapOnEditorActionListener(listener));
        return this;
    }
    
    /**
     * Like {@link #add(TextView)}. Let a series of views to be guarded by this DoneGuard.
     *
     * @param view   The view to be guarded.
     * @param others More views to be guarded.
     * @return This DoneGuard instance.
     * @see #add(TextView)
     */
    public DoneGuard addAll(TextView view, TextView... others) {
        add(view);
        for (TextView v : others) {
            add(v);
        }
        return this;
    }
    
    /**
     * Let the provided {@link TextView.OnEditorActionListener} to be a {@link
     * DoneGuard.GuardedOnEditorActionListener}
     * which will be guarded by this DoneGuard.
     *
     * @param onEditorActionListener onEditorActionListener
     * @return A GuardedOnEditorActionListener instance.
     */
    public DoneGuard.GuardedOnEditorActionListener wrapOnEditorActionListener(
            TextView.OnEditorActionListener onEditorActionListener) {
        if (onEditorActionListener == null) {
            throw new IllegalArgumentException("onEditorActionListener shouldn't be null!");
        }
        if (onEditorActionListener instanceof DoneGuard.GuardedOnEditorActionListener) {
            throw new IllegalArgumentException("Can't wrap GuardedOnEditorActionListener!");
        }
        return new DoneGuard.InnerGuardedOnEditorActionListener(onEditorActionListener, this);
    }
    
    /**
     * Let the Guard to start watching.
     */
    public abstract void watch();
    
    /**
     * Let the Guard to have a rest.
     */
    public abstract void rest();
    
    /**
     * Determine whether the Guard is on duty.
     *
     * @return Whether the Guard is watching.
     */
    public abstract boolean isWatching();
    
    private static class DoneGuardImpl extends DoneGuard {
        private static final int WATCHING = 0;
        private final Handler mHandler = new Handler(Looper.getMainLooper());
        private final long mWatchPeriodMillis;
        
        DoneGuardImpl(long watchPeriodMillis) {
            mWatchPeriodMillis = watchPeriodMillis;
        }
        
        @Override
        public void watch() {
            mHandler.sendEmptyMessageDelayed(WATCHING, mWatchPeriodMillis);
        }
        
        @Override
        public void rest() {
            mHandler.removeMessages(WATCHING);
        }
        
        @Override
        public boolean isWatching() {
            return mHandler.hasMessages(WATCHING);
        }
    }
    
    /**
     * OnEditorActionListener which can avoid multiple rapid clicks.
     */
    public static abstract class GuardedOnEditorActionListener
            implements TextView.OnEditorActionListener {
        private DoneGuard mGuard;
        private TextView.OnEditorActionListener mWrapped;
        
        public GuardedOnEditorActionListener(long watchPeriodMillis) {
            this(newGuard(watchPeriodMillis));
        }
        
        public GuardedOnEditorActionListener(DoneGuard guard) {
            this(null, guard);
        }
        
        GuardedOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener,
                                      DoneGuard guard) {
            mGuard = guard;
            mWrapped = onEditorActionListener;
        }
        
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (mGuard.isWatching()) {
                // Guard is guarding, can't do anything.
                onIgnored();
                return true;
            }
            // Guard is relaxing. Run!
            if (mWrapped != null) {
                mWrapped.onEditorAction(v, actionId, event);
            }
            if (onClicked()) {
                // Guard becomes vigilant.
                mGuard.watch();
            }
            return true;
        }
        
        /**
         * Called when a click is allowed.
         *
         * @return If {@code true} is returned, the host view will be guarded. All click events in
         * the upcoming watch period will be ignored. Otherwise, the next click will not be ignored.
         */
        public abstract boolean onClicked();
        
        /**
         * Called when a click is ignored.
         */
        public void onIgnored() {
        }
        
        public DoneGuard getDoneGuard() {
            return mGuard;
        }
    }
    
    // Inner GuardedOnEditorActionListener implementation.
    static class InnerGuardedOnEditorActionListener
            extends DoneGuard.GuardedOnEditorActionListener {
        InnerGuardedOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener,
                                           DoneGuard guard) {
            super(onEditorActionListener, guard);
        }
        
        public boolean onClicked() {
            return true;
        }
        
        public void onIgnored() {
        }
    }
    
    /**
     * Class used for retrieve OnEditorActionListener from a View.
     */
    static abstract class ListenerGetter {
        
        private static DoneGuard.ListenerGetter IMPL;
        
        static {
            IMPL = new ListenerGetterBase();
        }
        
        static TextView.OnEditorActionListener get(TextView view) {
            return IMPL.getOnEditorActionListener(view);
        }
        
        abstract TextView.OnEditorActionListener getOnEditorActionListener(TextView view);
        
        private static class ListenerGetterBase extends DoneGuard.ListenerGetter {
            private Field mEditorField;
            private Field mListenerInfoField;
            private Field mOnEditorActionListenerField;
            
            ListenerGetterBase() {
                mEditorField = ReflectionUtils.getField(TextView.class, "mEditor");
                mEditorField.setAccessible(true);
                mListenerInfoField = ReflectionUtils.getField("android.widget.Editor",
                                                              "mInputContentType");
                mListenerInfoField.setAccessible(true);
                mOnEditorActionListenerField = ReflectionUtils.getField("android.widget" +
                                                                                ".Editor$InputContentType", "onEditorActionListener");
                mOnEditorActionListenerField.setAccessible(true);
            }
            
            @Override
            public TextView.OnEditorActionListener getOnEditorActionListener(TextView view) {
                Object editorInfo = ReflectionUtils.getFieldValue(mEditorField, view);
                Object listenerInfo = ReflectionUtils.getFieldValue(mListenerInfoField, editorInfo);
                return listenerInfo != null ? ReflectionUtils.getFieldValue(mOnEditorActionListenerField, listenerInfo)
                        : null;
            }
        }
    }
    
    
}
