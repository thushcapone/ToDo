package com.shinobi.todo.errors;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rygelouv on 9/22/17.
 * <p>
 * MakebaMoney
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public class APIErrorsStatus
{
    public static final int TODO_ALREADY_EXIST = 1000;
    public static final int EXECUTION_ERROR = 90;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TODO_ALREADY_EXIST, EXECUTION_ERROR
    })
    public @interface APIErrors{}
}
