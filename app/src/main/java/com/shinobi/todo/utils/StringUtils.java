/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;

import com.shinobi.todo.app.AppController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiependa on 11/06/2017.
 */

public class StringUtils {
    
    private StringUtils() {
    }
    
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }
    
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
    
    public static <T> String listToString(List<T> list) {
        return AppController.getInstance().getGson().toJson(list);
    }
    
    public static <T> List<T> stringToList(String value, Type type) {
        List<T> list = AppController.getInstance().getGson().fromJson(value, type);
        if (list != null) {
            return list;
        } else {
            return new ArrayList<T>();
        }
    }
    
    /**
     * Capitalize all the words of a String given.
     * <p>Examples:
     * <blockquote><pre>
     * capitalizeString( "Lundi 15 janvier 2015") returns "Lundi 15 Janvier 2015"
     * capitalizeString( "je suis fatigué") returns "Je Suis Fatigué"
     * </pre></blockquote>
     *
     * @param string
     * @return
     */
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
    
}
