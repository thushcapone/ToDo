/*
 * Copyright (c) 2017. SeneVideos
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

import android.content.Context;
import android.content.Loader;

/**
 * Behinds the magic helping the BasePresenter (and all his sons) survive configuration changes
 * Shamelessly copied from <a
 * href="https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders
 * -6da6d86ffbbf#.3hhdjax6u">https://medium
 * .com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.3hhdjax6u</a>
 */
class PresenterOldLoader<P extends BasePresenter> extends Loader<P> {
    
    private final PresenterFactory<P> factory;
    private P presenter;
    
    PresenterOldLoader(Context context, PresenterFactory<P> factory) {
        super(context);
        this.factory = factory;
    }
    
    @Override
    protected void onStartLoading() {
        
        // If we already own an instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        
        // Otherwise, force a load
        forceLoad();
    }
    
    @Override
    protected void onForceLoad() {
        // Create the Presenter using the Factory
        presenter = factory.create();
        
        // Deliver the result
        deliverResult(presenter);
    }
    
    @Override
    protected void onReset() {
        if (presenter != null) {
            presenter.onDestroyed();
            presenter = null;
        }
    }
}
