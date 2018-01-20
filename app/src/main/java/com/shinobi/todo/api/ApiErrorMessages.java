/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.api;


import com.shinobi.todo.R;

import timber.log.Timber;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/03/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */
public enum ApiErrorMessages {
    
    CALL_CANCELLED("call cancelled", R.string.not_available),
    NO_NETWORK("reseau", R.string.error_no_connection),
    END_OF_PAGE("Invalid page.", R.string.error_end_of_page),
    NOT_FOUND("Not found.", R.string.error_not_found),
    UNKNOWN("New Answers That I know", R.string.error_api);
    
    private String value;
    private Integer resourceId;
    
    ApiErrorMessages(String value, Integer resourceId) {
        this.value = value;
        this.resourceId = resourceId;
    }
    
    public static ApiErrorMessages fromValue(String value) {
        if (value != null) {
            for (ApiErrorMessages apiErrorMessages : ApiErrorMessages.values()) {
                if (value.equalsIgnoreCase(apiErrorMessages.value)) {
                    return apiErrorMessages;
                }
            }
        }
        Timber.e("error unknown %s", value);
        return UNKNOWN;
    }
    
    public String getValue() {
        return value;
    }
    
    public Integer getResourceId() {
        return resourceId;
    }
    
}
