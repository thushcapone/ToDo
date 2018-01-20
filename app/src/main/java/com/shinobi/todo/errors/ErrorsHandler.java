package com.shinobi.todo.errors;

/**
 * Created by rygelouv on 9/22/17.
 * <p>
 * MakebaMoney
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public class ErrorsHandler
{
    public static String handleApiError(int apiError)
    {
        switch (apiError)
        {
            case APIErrorsStatus.EXECUTION_ERROR:
                return AppErrors.ERROR_PERFOMING_REQUEST;
            case APIErrorsStatus.TODO_ALREADY_EXIST:
                return AppErrors.CANNOT_CREATE_TODO;
            default:
                return AppErrors.GENERIC_ERROR;
        }
    }
}
