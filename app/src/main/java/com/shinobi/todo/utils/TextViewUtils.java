/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;

import android.text.Layout;
import android.widget.TextView;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 03/08/2017.
 * Wizall,
 * thiependa.seye@wizall.com
 */

public class TextViewUtils {
    
    private TextViewUtils() {
    }
    
    public static Boolean isTextEllipsized(TextView textView) {
        Layout l = textView.getLayout();
        if (l != null) {
            int lines = l.getLineCount();
            for (int i = 0; i < lines; i++) {
                if (l.getEllipsisCount(i) > 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
