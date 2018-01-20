/*
 * Copyright (c) 2018. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.widget.recycler_view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.Callable;

/**
 * Created by thiependa on 09/01/2018.
 */

public class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    
    RecyclerView recyclerView;
    View headerView;
    Integer viewId;
    Callable func;
    
    public RecyclerViewOnGestureListener(RecyclerView recyclerView, View headerView) {
        this.recyclerView = recyclerView;
        this.headerView = headerView;
    }
    
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        float touchY = e.getY();
        
        if(touchY < headerView.getMeasuredHeight()) {
            View view = headerView.findViewById(viewId);
            Rect rectf = new Rect();
    
            //For coordinates location relative to the screen/display
            view.getGlobalVisibleRect(rectf);
            
            if(rectf.contains((int) e.getX(), (int) e.getY())){
                try {
                    func.call();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            //Do stuff here no you have the position of the item that's been clicked
            return true;
        }
        return super.onSingleTapConfirmed(e);
        
    }
    
    @Override
    public boolean onDown(MotionEvent e) {
        float touchY = e.getY();
        
        if(touchY < headerView.getMeasuredHeight()) {
            return true;
        }
        return super.onDown(e);
    }
    
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        float touchY = e.getY();
        
        if(touchY < headerView.getMeasuredHeight()) {
            return true;
        }
        return super.onSingleTapUp(e);
    }
    
    public void setViewId(Integer viewId) {
        this.viewId = viewId;
    }
    
    public void setFunc(Callable func) {
        this.func = func;
    }
    
}
