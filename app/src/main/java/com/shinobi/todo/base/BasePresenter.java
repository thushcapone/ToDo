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

import android.support.annotation.NonNull;

/**
 * Mother Presenter that all the presenter inherit from.
 * Contains all the presenter operations allowed to the view common to all the presenter
 * in our MVP architecture.
 */
public interface BasePresenter {
    
    /**
     * Attaches the view to the presenter using a special weak reference and setup the view
     * If {@code isNew} is false, retrieve the state by calling {@link BaseView#retrieveState()}
     * NB: For the view that are not being attached to any view, I override the call to just have
     * the setting of the view and removing the call of setupView as they are not setting any views
     * nor retrieving any
     */
    void onViewAttached(@NonNull BaseView view, boolean isNew);
    
    /**
     * Saves the state of the view before it goes on background by calling {@link
     * BaseView#saveState()}
     */
    void onViewDetached();
    
    /**
     * Removes the reference of the model before the presenter is destroy
     */
    void onDestroyed();
    
    /**
     * Requests view to go home, it's drunk
     */
    void onHomeClicked();
    
    
}
