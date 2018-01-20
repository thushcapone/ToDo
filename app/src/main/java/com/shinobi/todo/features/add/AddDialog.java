package com.shinobi.todo.features.add;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.shinobi.todo.R;
import com.shinobi.todo.base.BaseDialog;
import com.shinobi.todo.base.PresenterFactory;
import com.shinobi.todo.utils.ClickGuard;

import butterknife.BindView;

/**
 * Created by thiependa on 20/01/2018.
 */

public class AddDialog extends BaseDialog<AddPresenter, AddCallback> implements AddView {
    
    private AlertDialog.Builder mDialogBuilder;
    
    @BindView(R.id.edit_add_to_do)
    TextInputEditText editAddToDo;
    @BindView(R.id.button_dialog_validate)
    TextView buttonDialogValidate;
    @BindView(R.id.button_dialog_cancel)
    TextView buttonDialogCancel;
    
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LayoutInflater li = LayoutInflater.from(this.getContext());
        mCustomView = li.inflate(R.layout.dialog_add_to_do, null);
        
        mDialogBuilder = new AlertDialog.Builder(getActivity());
        mDialogBuilder.setView(mCustomView);
        mDialogBuilder.create();
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialogBuilder.create();
    }
    
    @NonNull
    @Override
    protected PresenterFactory<AddPresenter> getPresenterFactory() {
        return new AddPresenterFactory();
    }
    
    @Override
    public void setupView() {
        super.setupView();
    
        buttonDialogValidate.setOnClickListener(
                view -> mPresenter.onAddClicked(editAddToDo.getText().toString())
        );
        buttonDialogCancel.setOnClickListener(view -> mPresenter.onCancelClicked());
    
        ClickGuard.guard(buttonDialogValidate, buttonDialogCancel);
        
    }
    
    @Override
    public void confirmAdd() {
        mCallback.onAddConfirmed();
    }
    
    
}
