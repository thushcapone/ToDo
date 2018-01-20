/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.cache;

import com.tc.senevideos.app.AppController;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by thiependa on 09/09/2017.
 */

public class CacheUtils {
    
    private CacheUtils() {
    }
    
    private static void checkingIsUrlFileCached(String url, Class clazz) {
        // First je check si cette url fait partie des url cache par fichier
        FilesCached filesCached = FilesCached.fromUrl(url);
        if (filesCached != null) {
            // Ensuite, je check si j’ai deja appelle cette url
            if (AppController.getInstance().getPreferences().isUrlNotCalled(url)) {
                // Si non je recupere la réponse provenant du fichier alors
                try {
                    InputStream is = AppController.getInstance().getAssets().open(filesCached
                                                                                          .getFile());
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    String json = new String(buffer, "UTF-8");
                    // je sauvegarde cette réponse dans le cache.
                    AppController.getInstance().getCache().save(
                            url,
                            AppController.getInstance().getGson().fromJson(json, clazz)
                    );
                    // Je sauvegarde que j’ai appele cette url
                    AppController.getInstance().getPreferences().saveUrl(url);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public static <T> T getCachedValue(String url, Class<T> clazz) {
        checkingIsUrlFileCached(url, clazz);
        // Je recupere la valeur du cache pour cette url
        return AppController.getInstance().getCache().get(url, clazz);
    }
    
    public static <T> T getCachedValue(String url, Type type) {
        // Je recupere la valeur du cache pour cette url
        return AppController.getInstance().getCache().get(url, type);
    }
    
    public static void save(String url, Object response) {
        AppController.getInstance().getCache().save(url, response);
    }
    
}
