/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.base;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 20/04/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

public class BaseModelImpl<IP extends BaseModelInteractPresenter> implements BaseModel {
    
    protected IP mInteractPresenter;
    
    public BaseModelImpl(IP mInteractPresenter) {
        this.mInteractPresenter = mInteractPresenter;
    }
    
}
