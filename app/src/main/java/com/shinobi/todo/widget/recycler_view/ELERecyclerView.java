/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.widget.recycler_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tc.senevideos.R;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 15/04/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

/**
 * Handles Empty, error and loading states for the recycler view
 */
public class ELERecyclerView extends RecyclerView {
    View emptyView;
    // BUG: 31/10/2017 Sometimes the error and the content is shown simultaneously
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }
        
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }
        
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };
    View errorView;
    View loadingView;
    
    public ELERecyclerView(Context context) {
        super(context);
    }
    
    public ELERecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ELERecyclerView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
    }
    
    void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }
    
    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        
        checkIfEmpty();
    }
    
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }
    
    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
        loadingView.setVisibility(GONE);
    }
    
    public void setErrorView(View errorView) {
        this.errorView = errorView;
        errorView.setVisibility(GONE);
    }
    
    public void showLoading() {
        loadingView.setVisibility(VISIBLE);
        emptyView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        setVisibility(GONE);
    }
    
    public void stopLoading() {
        loadingView.setVisibility(GONE);
    }
    
    public void showError(String message) {
        TextView textView = errorView.findViewById(R.id.text_error);
        textView.setText(message);
        errorView.setVisibility(VISIBLE);
        emptyView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        setVisibility(GONE);
    }
    
}
