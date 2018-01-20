/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 11/04/2017.
 * Wizall,
 * thiependa.seye@wizall.com
 */

public abstract class BaseWidget extends LinearLayout {
    
    /**
     * Identifier for the state of the super class.
     */
    private static final String STATE_SUPER_CLASS = "SuperClass";
    
    protected Bundle mSavedInstanceState;
    
    public BaseWidget(Context context) {
        super(context);
        initializeViews(context);
    }
    
    public BaseWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        retrieveStyle(context, attrs);
        initializeViews(context);
    }
    
    public BaseWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        retrieveStyle(context, attrs);
        initializeViews(context);
    }
    
    /**
     * Inflates the views in the layout.
     *
     * @param context the current context for the view.
     */
    protected void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutId(), this);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        setupView();
    }
    
    public void setupView() {
        ButterKnife.bind(this);
    }
    
    @Override
    protected Parcelable onSaveInstanceState() {
        if (mSavedInstanceState == null) {
            this.mSavedInstanceState = new Bundle();
        }
        
        mSavedInstanceState.putParcelable(STATE_SUPER_CLASS, super.onSaveInstanceState());
        
        return mSavedInstanceState;
    }
    
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            mSavedInstanceState = (Bundle) state;
    
            super.onRestoreInstanceState(mSavedInstanceState.getParcelable(STATE_SUPER_CLASS));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
    
    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        // Makes sure that the state of the child views are not saved since we handle the state
        // in the
        // onSaveInstanceState.
        super.dispatchFreezeSelfOnly(container);
    }
    
    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        // Makes sure that the state of the child views are not restored since we handle the
        // state in the
        // onSaveInstanceState.
        super.dispatchThawSelfOnly(container);
    }
    
    /**
     * Retrieve the attributes sent to the widget and use them
     *
     * @param attrs
     */
    protected abstract void retrieveStyle(Context context, @Nullable AttributeSet attrs);
    
    /**
     * Retrieve the layout to load
     *
     * @return layoutId
     */
    protected abstract Integer getLayoutId();
    
}
