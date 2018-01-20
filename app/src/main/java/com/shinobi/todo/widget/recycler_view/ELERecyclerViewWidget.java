/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.widget.recycler_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tc.senevideos.R;
import com.tc.senevideos.base.BaseWidget;
import com.tc.senevideos.utils.ClickGuard;
import com.tc.senevideos.utils.ViewUtils;

import java.util.concurrent.Callable;

import butterknife.BindView;

/**
 * Created by thiependa on 30/07/2017.
 */

public class ELERecyclerViewWidget extends BaseWidget {
    
    
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    
    @BindView(R.id.recycler_ele)
    protected ELERecyclerView recyclerView;
    @Nullable
    @BindView(R.id.swipe_refresh_ele)
    protected SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.image_empty)
    protected ImageView imageEmpty;
    @BindView(R.id.text_empty_title)
    protected TextView textEmptyTitle;
    @BindView(R.id.text_empty_desc)
    protected TextView textEmptyDesc;
    @BindView(R.id.button_empty)
    protected Button buttonEmpty;
    @BindView(R.id.loading_view)
    protected ProgressBar loadingView;
    @BindView(R.id.layout_empty)
    protected View layoutEmpty;
    @BindView(R.id.layout_error)
    protected View layoutError;
    
    protected View headerView;
    protected HeaderDecoration headerDecoration;
    protected GestureDetectorCompat detector;
    private RecyclerViewOnGestureListener headerListener;
    
    private Boolean mIsRefreshable;
    
    public ELERecyclerViewWidget(Context context) {
        super(context);
    }
    
    public ELERecyclerViewWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ELERecyclerViewWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void retrieveStyle(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ELERecyclerViewWidget,
                                                      0, 0);
        mIsRefreshable = a.getBoolean(R.styleable.ELERecyclerViewWidget_isRefreshable, true);
        a.recycle();
    }
    
    @Override
    protected Integer getLayoutId() {
        return mIsRefreshable ? R.layout.compound_recycler_view : R.layout
                .compound_recycler_view_no_refresh;
    }
    
    @Override
    public void setupView() {
        super.setupView();
        
        if(headerView == null) {
            headerView = LayoutInflater
                    .from(this.getContext())
                    .inflate(R.layout.partial_layout_info_header_view, null, false);
    
            headerDecoration = HeaderDecoration.with(recyclerView)
                                               .setView(headerView)
                                               .parallax(0.2f)
                                               .dropShadowDp(4)
                                               .build();
    
            headerListener = new RecyclerViewOnGestureListener(recyclerView, headerView);
    
            detector = new GestureDetectorCompat(this.getContext(), headerListener);
    
            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    detector.onTouchEvent(e);
                    return false;
                }
        
                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
        
                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
            });
            
        }
        
        recyclerView.setEmptyView(layoutEmpty);
        recyclerView.setLoadingView(loadingView);
        recyclerView.setErrorView(layoutError);
        
        if (swipeRefresh != null) {
            swipeRefresh.setColorSchemeResources(
                    R.color.colorAccentDark,
                    R.color.colorAccent,
                    R.color.colorAccentLight
            );
        }
    }
    
    public ELERecyclerView getRecyclerView() {
        return recyclerView;
    }
    
    public void setEmptyTitle(Integer text) {
        textEmptyTitle.setText(text);
    }
    
    public void setEmptyTitle(String text) {
        textEmptyTitle.setText(text);
    }
    
    public void setEmptyDesc(Integer text) {
        textEmptyDesc.setText(text);
    }
    
    public void setEmptyDesc(String text) {
        textEmptyDesc.setText(text);
    }
    
    public void showHeader(Drawable drawable, String title, String description){
        ImageView imgInfo = headerView.findViewById(R.id.img_info);
        TextView textInfoTitle = headerView.findViewById(R.id.text_info_title);
        TextView textInfoDescription = headerView.findViewById(R.id.text_info_description);
    
        imgInfo.setImageDrawable(drawable);
        textInfoTitle.setText(title);
        textInfoDescription.setText(description);
        
        recyclerView.addItemDecoration(headerDecoration);
    }
    
    public void hideHeader(){
        recyclerView.removeItemDecoration(headerDecoration);
    }
    
    public void setRefreshListener(Callable func) {
        swipeRefresh.setOnRefreshListener(() -> {
            try {
                func.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    public void setEmptyListener(Callable func) {
        buttonEmpty.setOnClickListener(view -> {
            try {
                func.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ClickGuard.guard(buttonEmpty);
    }
    
    public void setErrorListener(Callable func) {
        layoutError.findViewById(R.id.button_error).setOnClickListener(view -> {
            try {
                func.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ClickGuard.guard(layoutError.findViewById(R.id.button_error));
    }
    
    public void setHideInfoHeaderListener(Callable func) {
        headerListener.setViewId(R.id.img_info_close_button);
        headerListener.setFunc(() -> {
            try {
                func.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }
    
    public void showLoading() {
        recyclerView.showLoading();
        if (swipeRefresh != null) {
            swipeRefresh.setEnabled(false);
        }
    }
    
    public void stopLoading() {
        recyclerView.stopLoading();
        if (swipeRefresh != null) {
            swipeRefresh.setEnabled(true);
        }
    }
    
    public void showRefreshLoading() {
        swipeRefresh.setRefreshing(true);
    }
    
    public void stopRefreshLoading() {
        swipeRefresh.setRefreshing(false);
    }
    
    public void showError(String message) {
        recyclerView.showError(message);
    }
    
    public void hideRetryButton() {
        ViewUtils.runOnLayoutDone(buttonEmpty, () -> ViewUtils.setVisibility(View.GONE,
                                                                             buttonEmpty));
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        
        mSavedInstanceState.putInt("emptyView", recyclerView.emptyView.getVisibility());
        mSavedInstanceState.putInt("loadingView", recyclerView.loadingView.getVisibility());
        mSavedInstanceState.putInt("errorView", recyclerView.errorView.getVisibility());
        mSavedInstanceState.putInt("buttonEmpty", buttonEmpty.getVisibility());
        if (recyclerView.getLayoutManager() != null) {
            mSavedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView
                    .getLayoutManager().onSaveInstanceState());
        }
        
        return mSavedInstanceState;
    }
    
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (mSavedInstanceState != null) {
            recyclerView.emptyView.setVisibility(
                    mSavedInstanceState.getInt("emptyView") == View.VISIBLE ? View.VISIBLE : View
                            .GONE
            );
            recyclerView.loadingView.setVisibility(
                    mSavedInstanceState.getInt("loadingView") == View.VISIBLE ? View.VISIBLE :
                            View.GONE
            );
            recyclerView.errorView.setVisibility(
                    mSavedInstanceState.getInt("errorView") == View.VISIBLE ? View.VISIBLE : View
                            .GONE
            );
            buttonEmpty.setVisibility(
                    mSavedInstanceState.getInt("buttonEmpty") == View.VISIBLE ? View.VISIBLE :
                            View.GONE
            );
            
            Parcelable savedRecyclerLayoutState = mSavedInstanceState.getParcelable
                    (BUNDLE_RECYCLER_LAYOUT);
            if (recyclerView.getLayoutManager() != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            }
        }
    }
}
