package com.shinobi.todo.features.list;

import com.shinobi.todo.api.BaseApiResponse;
import com.shinobi.todo.entity.ToDo;

import java.util.List;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public class TodoListResponse extends BaseApiResponse
{
    /**
     * count : 3
     * next : null
     * previous : null
     * results : [{"id":1,"uuid":"adxpxaxapnhzedxaodhafoi","text":"Faire les courses"},{"id":2,"uuid":"2sdknfq","text":"Manger sans mourrir"},{"id":3,"uuid":"sdcnalzcnjdncbxnaepzfzhba","text":"Danser sur la piste"}]
     */

    public int count;
    public String next;
    public String previous;
    public List<ToDo> results;
}
