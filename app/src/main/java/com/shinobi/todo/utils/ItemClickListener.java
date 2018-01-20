/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 28/03/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

/**
 * Utils use as a callback for the recyclerview adapter to the parent activity
 */
public interface ItemClickListener {
    
    void onItemClick(View view, int position, ImageView sharedImage);
    
}
