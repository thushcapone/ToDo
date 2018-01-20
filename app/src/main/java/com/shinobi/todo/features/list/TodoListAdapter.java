package com.shinobi.todo.features.list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shinobi.todo.R;
import com.shinobi.todo.databinding.TodoItemBinding;
import com.shinobi.todo.entity.ToDo;

import java.util.List;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder>
{
    private Context mContext;
    private List<ToDo> mDataSet;

    public TodoListAdapter(Context mContext, List<ToDo> mDataSet)
    {
        this.mContext = mContext;
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        ToDo toDo = mDataSet.get(position);
        holder.binding.setTodo(toDo);
    }

    @Override
    public int getItemCount()
    {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void setData(List<ToDo> toDoList)
    {
        this.mDataSet = toDoList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TodoItemBinding binding;

        public ViewHolder(View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
