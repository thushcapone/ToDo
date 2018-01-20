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
     * Shows a progress dialog
     * E.g. {@link com.wizall.wizallclient.widgets.progressdialog.ProgressDialog#show
     * (FragmentManager,
     * String)}
     */
    void showLoading();
    
    /**
     * Dismisses the progress dialog previously shown
     * E.g. {@link com.wizall.wizallclient.widgets.progressdialog.ProgressDialog#dismiss()}
     */
    void stopLoading();
    
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
     * Shows a dialog with the message stating there is a problem with the connection
     * E.g. Using {@link com.wizall.wizallclient.widgets.messagedialog.MessageDialog}
     */
    void noConnection();
    
    /**
     * Hides the keyboard
     */
    void hideKeyboard();
    
    /**
     * Closes the current view
     */
    void close();
    
    void showError(Object msg);
    
}
