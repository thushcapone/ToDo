/*
 * Copyright (c) 2017. SeneVideo
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.base;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tc.senevideos.R;
import com.tc.senevideos.app.Tags;
import com.tc.senevideos.utils.KeyboardUtils;
import com.tc.senevideos.utils.ReflectionUtils;
import com.tc.senevideos.widgets.message_dialog.MessageCallback;
import com.tc.senevideos.widgets.message_dialog.MessageDialog;
import com.tc.senevideos.widgets.message_dialog.error.ErrorMessageDialog;
import com.tc.senevideos.widgets.progress_dialog.ProgressDialog;

import java.util.List;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Mother Dialog that all the dialog using the MVP inherit from.
 * Implements the functions that are common to all the dialogs.
 */
public abstract class BaseDialog<P extends BasePresenter, C extends BaseCallback>
        extends DialogFragment
        implements BaseView, MessageCallback {
    
    private static final int LOADER_ID = 102;
    protected String mTag;
    protected P mPresenter;
    protected C mCallback;
    protected Bundle mSavedInstanceState;
    protected View mCustomView;
    protected ErrorMessageDialog mErrorMessageDialog;
    protected Button mPositiveButton;
    protected Button mNegativeButton;
    private boolean isNewDialog;
    private ProgressDialog mLoadingDialog;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        
        isNewDialog = savedInstanceState == null;
        
        setCancelable(false);
        
        if (savedInstanceState != null) {
            this.mSavedInstanceState = savedInstanceState.getBundle(Tags.BUNDLE_STATE);
        } else {
            this.mSavedInstanceState = new Bundle();
        }
        
        // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                return new PresenterLoader<>(BaseDialog.this.getContext(), getPresenterFactory());
            }
            
            @Override
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                BaseDialog.this.mPresenter = presenter;
            }
            
            @Override
            public final void onLoaderReset(Loader<P> loader) {
                BaseDialog.this.mPresenter = null;
            }
        });
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void setupView() {
        ButterKnife.bind(this, mCustomView);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onViewAttached(this, isNewDialog);
        isNewDialog = false;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onViewDetached();
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Class classOfC = ReflectionUtils.getTypeArguments(BaseDialog.class, getClass()).get(1);
        for (Fragment fragment : ((BaseActivity) context).getSupportFragmentManager()
                                                         .getFragments()) {
            if (classOfC.isInstance(fragment) && !MessageDialog.class.isAssignableFrom(fragment.getClass())) {
                mCallback = mCallback == null ? (C) fragment : mCallback;
                return;
            }
        }
        if (classOfC.isInstance(context)) {
            mCallback = mCallback == null ? (C) context : mCallback;
        }
    }
    
    /**
     * Used to attach the listener from the parent fragment
     */
    public void onAttachParentFragment(Fragment parentFragment) {
        Class classOfC = ReflectionUtils.getTypeArguments(BaseDialog.class, getClass()).get(1);
        if (mCallback == null && parentFragment != null && classOfC.isInstance(parentFragment)) {
            mCallback = mCallback == null ? (C) parentFragment : mCallback;
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        onAttachParentFragment(getParentFragment());
        Class classOfC = ReflectionUtils.getTypeArguments(BaseDialog.class, getClass()).get(1);
        if (mCallback == null) {
            throw new ClassCastException("parent must implement " + classOfC.getName());
        }
        if (getDialog() instanceof AlertDialog) {
            AlertDialog alertDialog = (AlertDialog) getDialog();
            if (alertDialog != null) {
                mPositiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
                mNegativeButton = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);
            }
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBundle(Tags.BUNDLE_STATE, this.mSavedInstanceState);
        // Always call the superclass so it can save the mCustomView hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    public void showLoading() {
        mLoadingDialog = ProgressDialog.newInstance(getResources().getString(R.string.loading));
        mLoadingDialog.show(getChildFragmentManager(), Tags.DIALOG_LOADING);
    }
    
    @Override
    public void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
    
    @Override
    public void showError(Object msg) {
        String message = msg instanceof Integer ? getResources().getString((Integer) msg) :
                (String) msg;
        mErrorMessageDialog = (ErrorMessageDialog) ErrorMessageDialog
                .builder()
                .withMessage(message)
                .build();
        mErrorMessageDialog.show(getChildFragmentManager(), Tags.DIALOG_ERROR);
    }
    
    @Override
    public void retrieveState() {
        mLoadingDialog =
                (ProgressDialog) getChildFragmentManager().findFragmentByTag(Tags.DIALOG_LOADING);
        mErrorMessageDialog =
                (ErrorMessageDialog) getChildFragmentManager().findFragmentByTag(Tags.DIALOG_ERROR);
    }
    
    @Override
    public void saveState() {
        //Nothing to do yet
    }
    
    @Override
    public void noConnection() {
        Toast.makeText(this.getContext(), R.string.error_no_connection, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void hideKeyboard() {
        KeyboardUtils.hide(this.getContext(), mCustomView);
    }
    
    @Override
    public void close() {
        this.dismiss();
    }
    
    @Override
    public void onMessageOk(String tag) {
        List<Fragment> fragmentChilds = getChildFragmentManager().getFragments();
        if (fragmentChilds != null) {
            for (Fragment f : fragmentChilds) {
                if (f instanceof BaseDialog) {
                    ((BaseDialog) f).onMessageOk(tag);
                }
            }
        }
    }
    
    @Override
    public void onMessageCancel(String tag) {
        List<Fragment> fragmentChilds = getChildFragmentManager().getFragments();
        if (fragmentChilds != null) {
            for (Fragment f : fragmentChilds) {
                if (f instanceof BaseDialog) {
                    ((BaseDialog) f).onMessageCancel(tag);
                }
            }
        }
    }
    
    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance
     * should not contain {@link BaseDialog} context reference since it will be keep on
     * rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();
    
    /**
     * Now when called I call the allowingstateloss version, hack for the IllegalStateException
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            if (manager.findFragmentByTag(tag) == null) {
                mTag = tag;
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
