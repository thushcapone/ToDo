package com.shinobi.todo.features.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;

import com.shinobi.todo.R;
import com.shinobi.todo.base.BaseActivity;
import com.shinobi.todo.base.PresenterFactory;
import com.shinobi.todo.data.ToDoItem;
import com.shinobi.todo.features.add.AddCallback;
import com.shinobi.todo.features.add.AddDialog;
import com.shinobi.todo.utils.RecyclerViewUtils;
import com.shinobi.todo.widget.recycler_view.ELERecyclerViewWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by thiependa on 20/01/2018.
 */

public class ListActivity extends BaseActivity<ListPresenter> implements ListView, AddCallback {
    
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private static final String ADD_TO_DO = "add to do";
    
    @BindView(R.id.list_to_do)
    ELERecyclerViewWidget listToDo;
    @BindView(R.id.fab_add_to_do)
    FloatingActionButton fabAddToDo;
    
    private List<ToDoItem> mDataSet = new ArrayList<>();
    private ListAdapter mAdapter;
    private AddDialog mAddDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_to_do);
    }
    
    @NonNull
    @Override
    protected PresenterFactory<ListPresenter> getPresenterFactory() {
        return new ListPresenterFactory();
    }
    
    @Override
    public void setupView() {
        super.setupView();
    
        listToDo.setEmptyTitle(R.string.no_to_do);
        
        fabAddToDo.setOnClickListener(view -> mPresenter.onAddToDoClicked());
    }
    
    @Override
    public void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false);
        listToDo.getRecyclerView().setLayoutManager(layoutManager);
        listToDo.getRecyclerView().setHasFixedSize(true);
    
        mAdapter = new ListAdapter(this, listToDo.getRecyclerView());
        listToDo.getRecyclerView().setAdapter(mAdapter);
    }
    
    @Override
    public void showListLoading() {
        listToDo.showLoading();
    }
    
    @Override
    public void stopListLoading() {
        listToDo.stopLoading();
    }
    
    @Override
    public void setListToDos(List<ToDoItem> toDos) {
        mDataSet.clear();
        RecyclerViewUtils.clearObjects(mAdapter);
        RecyclerViewUtils.addObjects(mDataSet, mAdapter, toDos);
    }
    
    @Override
    public void showListError(Object msg) {
        String message = msg instanceof Integer ? getResources().getString((Integer) msg) :
                (String) msg;
        listToDo.showError(message);
    }
    
    @Override
    public void openAddToDo() {
        mAddDialog = new AddDialog();
        mAddDialog.show(getSupportFragmentManager(), ADD_TO_DO);
    }
    
    @Override
    public void onAddConfirmed() {
        mPresenter.onAddedToDo();
    }
    
    @Override
    public void saveState() {
        super.saveState();
    
        mSavedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT, listToDo.onSaveInstanceState());
    }
    
    @Override
    public void retrieveState() {
        super.retrieveState();
    
        listToDo.onRestoreInstanceState(mSavedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT));
    
        mAddDialog = (AddDialog) getSupportFragmentManager().findFragmentByTag(ADD_TO_DO);
    }
}
