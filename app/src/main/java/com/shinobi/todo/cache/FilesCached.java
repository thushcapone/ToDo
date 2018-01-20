/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.cache;

import com.tc.senevideos.api.ApiUrls;

/**
 * Created by thiependa on 09/09/2017.
 */

public enum FilesCached {
    
    NO_NETWORK(ApiUrls.CATEGORIES, "data/categories.json"),
    END_OF_PAGE(ApiUrls.TV_SHOWS, "data/tv_shows.json");
    
    private String url;
    private String file;
    
    FilesCached(String url, String file) {
        this.url = url;
        this.file = file;
    }
    
    public static FilesCached fromUrl(String url) {
        if (url != null) {
            for (FilesCached filesCached : FilesCached.values()) {
                if (url.endsWith(filesCached.getUrl())) {
                    return filesCached;
                }
            }
        }
        return null;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getFile() {
        return file;
    }
}
