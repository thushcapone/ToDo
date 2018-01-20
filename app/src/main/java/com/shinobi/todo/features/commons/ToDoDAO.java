package com.shinobi.todo.features.commons;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.shinobi.todo.entity.ToDo;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

@Dao
public interface ToDoDAO
{
    @Query("SELECT * FROM todos")
    Flowable<List<ToDo>> getProductListRx();

    @Query("SELECT * FROM todos")
    LiveData<List<ToDo>> getToDoList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToDoList(List<ToDo> ToDos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToDo(ToDo ToDos);

    @Query("SELECT * FROM ToDos WHERE id = :id")
    LiveData<ToDo> getToDo(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateToDo(ToDo ToDo);

}
