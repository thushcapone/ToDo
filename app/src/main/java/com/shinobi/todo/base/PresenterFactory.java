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
 * Mother of the presenter factory that all the presenter factory inherit from.
 * Creates the implementation of the {@code <P>} presenter
 */
public interface PresenterFactory<P extends BasePresenter> {
    
    /**
     * Factory use to create the implementation of the presenter
     */
    P create();
}
