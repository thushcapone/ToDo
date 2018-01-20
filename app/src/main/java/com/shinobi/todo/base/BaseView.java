/*
 * Copyright (c) 2017.
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.base;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

/**
 * Mother View that all the view inherit from.
 * Contains all the views operations allowed to the presenter common to all the view
 * in our MVP architecture.
 */
public interface BaseView {
    
    /**
     * Setup the view
     * It is where you can bind the butterknife to the view, sets the listeners, passes infos to the
     * presenter, set the visibility of the view, ...
     */
    void setupView();
    
    /**
     * Saves the states of the view that can't be saved automatically by Android
     * E.g. the textviews, listviews, dialogfragments, ...
     */
    void saveState();
    
    /**
     * Retrieves the states of the view that were manually saved
     */
    void retrieveState();
    
    /**
     * Hides the keyboard
     */
    void hideKeyboard();
    
    /**
     * Closes the current view
     */
    void close();
    
}
