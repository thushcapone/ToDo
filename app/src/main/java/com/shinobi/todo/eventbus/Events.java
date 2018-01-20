package com.shinobi.todo.eventbus;

import com.shinobi.todo.entity.ToDo;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public class Events
{
    public static class TodoUpdatedEvent
    {
        public ToDo toDo;

        public TodoUpdatedEvent(ToDo toDo)
        {
            this.toDo = toDo;
        }
    }
}
