package com.shinobi.todo.features.commons;

import com.shinobi.todo.api.ApiRetrofitGenerator;
import com.shinobi.todo.api.ApiUrls;
import com.shinobi.todo.entity.ToDo;
import com.shinobi.todo.features.list.TodoListResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public class TodoAPI
{
    public static TodoEdnPoints getTodoEdnPoints()
    {
        return ApiRetrofitGenerator.createService(TodoEdnPoints.class);
    }

    public interface TodoEdnPoints
    {
        @GET(ApiUrls.TO_DOS)
        Observable<Response<TodoListResponse>> getTodoList();

        @POST(ApiUrls.TO_DOS)
        Observable<Response<ToDo>> sendTodoList(
                @Body AddTodoPayload payload
        );
    }
}
