package com.shinobi.todo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.shinobi.todo.entity.ToDo;
import com.shinobi.todo.features.commons.ToDoDAO;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

@Database(entities = { ToDo.class}, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase
{
    public abstract ToDoDAO todoDAO();
}
