/*
 * Copyright (c) 2017. SeneVideos
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.data;


import com.shinobi.todo.app.AppController;

/**
 * Created by thiependa on 05/06/2017.
 */

public class ApiError {
    
    private Integer code;
    private String error;
    private String message;
    private String detail;
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getError() {
        if (error != null) {
            return error;
        } else if (message != null) {
            return message;
        } else if (detail != null) {
            return detail;
        } else {
            return "";
        }
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    @Override
    public String toString() {
        return AppController.getInstance().getGson().toJson(this);
    }
    
}
