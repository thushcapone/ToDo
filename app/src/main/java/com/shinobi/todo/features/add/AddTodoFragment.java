package com.shinobi.todo.features.add;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shinobi.todo.R;
import com.shinobi.todo.databinding.FragmentAddTodoBinding;
import com.shinobi.todo.entity.Resource;
import com.shinobi.todo.entity.ToDo;
import com.shinobi.todo.features.commons.TodoViewModel;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTodoFragment extends DialogFragment
{
    private TodoViewModel mViewModel;
    private FragmentAddTodoBinding mBinding;
    public AddTodoFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity(), new TodoViewModel.Factory()).get(TodoViewModel.class);
        mViewModel.getProductAddedLiveData().observe(this, toDo -> {
            if (toDo != null)
            {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_todo, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mBinding.buttonAdd.setOnClickListener(view1 -> {
            if (!TextUtils.isEmpty(mBinding.todoName.getText()))
            {
                mViewModel.addTodo(mBinding.todoName.getText().toString());
                dismiss();
            }
        });
    }


    /**
     * Now when called I call the allowingstateloss version, hack for the IllegalStateException
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            if (manager.findFragmentByTag(tag) == null) {
                FragmentTransaction ft = manager.beginTransaction();
                ft.add(this, tag);
                ft.commitAllowingStateLoss();
            }
        } catch (IllegalStateException e) {
            Timber.e("illegal exception show: Activity has been destroyed");
        }
    }

    /**
     * Now when dismissing I call the allowing state loss one cuz .... Android is annoying on this
     * one
     * hack for the Illegal StateException
     */
    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
    }
}
