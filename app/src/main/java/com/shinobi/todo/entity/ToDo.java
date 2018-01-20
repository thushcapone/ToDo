package com.shinobi.todo.entity;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Rygel Louv All rights reserved.
 */
@Entity(tableName = "todos", primaryKeys = {"uuid"})
public class ToDo
{
    /**
     * id : 5
     * uuid : 2sdknfmfgqvsvqsv
     * text : Nager dans l'ocean
     */

    public int id;
    public @NonNull String uuid;
    public String text;
    public int status = Status.SENT;

    public interface Status {
        int STORED = 0;
        int SENT = 1;
        int FAILED = 2;
    }
}
