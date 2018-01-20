/*
 * Copyright (c) 2017. SeneVideos
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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.tc.senevideos.R;
import com.tc.senevideos.app.Config;
import com.tc.senevideos.app.Tags;
import com.tc.senevideos.utils.KeyboardUtils;
import com.tc.senevideos.utils.LifecycleCallback;
import com.tc.senevideos.widgets.message_dialog.MessageCallback;
import com.tc.senevideos.widgets.message_dialog.error.ErrorMessageDialog;
import com.tc.senevideos.widgets.progress_dialog.ProgressDialog;

/**
 * Mother Activity that all the activities using the MVP inherit from.
 * Implements the functions that are common to all the activities.
 */
public abstract class BaseYoutubeFragment<P extends BasePresenter> extends
        YouTubePlayerSupportFragment
        implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener,
        BaseView,
        MessageCallback, LifecycleCallback.ForegroundListener {
    
    private static final int LOADER_ID = 105;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    
    protected P mPresenter;
    protected Bundle mSavedInstanceState;
    protected ErrorMessageDialog mErrorMessageDialog;
    //FYI 26/10/2017: Temporary loading dialog till I fine a better solution
    private ProgressDialog mLoadingDialog;
    private boolean isNewFragment;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        isNewFragment = savedInstanceState == null;
        
        if (savedInstanceState != null) {
            this.mSavedInstanceState = savedInstanceState.getBundle(Tags.BUNDLE_STATE);
        } else {
            this.mSavedInstanceState = new Bundle();
        }
        // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                return new PresenterLoader<>(BaseYoutubeFragment.this.getContext(),
                                             getPresenterFactory());
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                BaseYoutubeFragment.this.mPresenter = presenter;
            }
            
            @Override
            public final void onLoaderReset(Loader<P> loader) {
                BaseYoutubeFragment.this.mPresenter = null;
            }
        });
        
        initialize(Config.YOUTUBE_API_KEY, this);
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void setupView() {
        //Nothing to do
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onViewAttached(this, isNewFragment);
        isNewFragment = false;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onViewDetached();
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBundle(Tags.BUNDLE_STATE, this.mSavedInstanceState);
        // Always call the superclass so it can save the mCustomView hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        
        // disable view state saving to prevent saving states from youtube apk which cannot be
        // restored.
        // This solution does not remove youtube player state from bundle. So youtube player will
        // be restored successfully.
        // This also fix issue #58 on Crashlytics
        View view = getView();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = ((ViewGroup) view);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setSaveFromParentEnabled(false);
            }
        }
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
        KeyboardUtils.hide(this.getContext(), getView());
    }
    
    @Override
    public void close() {
        this.getActivity().finish();
    }
    
    @Override
    public void onMessageOk(String tag) {
        //Nothing to do yet
    }
    
    @Override
    public void onMessageCancel(String tag) {
        //Nothing to do yet
    }
    
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this.getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_youtube_player),
                                                errorReason.toString());
            Toast.makeText(this.getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onLoading() {
        //Nothing to do
    }
    
    @Override
    public void onLoaded(String s) {
        //Nothing to do
    }
    
    @Override
    public void onAdStarted() {
        //Nothing to do
    }
    
    @Override
    public void onVideoStarted() {
        //Nothing to do
    }
    
    @Override
    public void onVideoEnded() {
        //Nothing to do
    }
    
    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
    
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            initialize(Config.YOUTUBE_API_KEY, this);
        }
    }
    
    @Override
    public void onBecameBackground() {
        //Nothing to do
    }
    
    @Override
    public void onBecameForeground(String activity) {
        //Nothing to do yet
    }
    
    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance
     * should not contain {@link BaseYoutubeFragment} context reference since it will be keep on
     * rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();
    
    /**
     * Could handle back press.
     *
     * @return true if back press was handled
     */
    public boolean onBackPressed() {
        return false;
    }
    
}