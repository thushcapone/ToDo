package com.shinobi.todo.features.list;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shinobi.todo.R;
import com.shinobi.todo.databinding.FragmentTodoListBinding;
import com.shinobi.todo.entity.Resource;
import com.shinobi.todo.features.commons.TodoViewModel;

/**
 * Created by rygelouv on 1/20/18.
 * <p>
 * ToDo
 * Copyright (c) 2017 Makeba Inc All rights reserved.
 */

public class TodoListFragment extends Fragment
{
    private TodoViewModel mViewModel;
    private FragmentTodoListBinding mBinding;
    private TodoListAdapter mAdapter;
    
    public TodoListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity(), new TodoViewModel.Factory()).get(TodoViewModel.class);
        mViewModel.getTodoList().observe(this, listResource -> {
            if (listResource != null)
            {
                switch (listResource.status)
                {
                    case Resource.ERROR:
                        Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_SHORT).show();
                        break;
                    case Resource.LOADING:
                        mAdapter.setData(listResource.data);
                        break;
                    case Resource.SUCCESS:
                        mAdapter.setData(listResource.data);
                        break;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView()
    {
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);
        mAdapter = new TodoListAdapter(getActivity(), null);
        mBinding.todoList.setLayoutManager(layoutManager);
        mBinding.todoList.setAdapter(mAdapter);
    }
}
