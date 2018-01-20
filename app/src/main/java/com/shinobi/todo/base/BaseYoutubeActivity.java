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

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.tc.senevideos.R;
import com.tc.senevideos.app.AppController;
import com.tc.senevideos.app.Config;
import com.tc.senevideos.app.Tags;
import com.tc.senevideos.utils.KeyboardUtils;
import com.tc.senevideos.utils.LifecycleCallback;
import com.tc.senevideos.widgets.message_dialog.MessageCallback;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Mother Activity that all the activities using the MVP inherit from.
 * Implements the functions that are common to all the activities.
 */
public abstract class BaseYoutubeActivity<P extends BasePresenter> extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener,
        BaseView,
        MessageCallback, LifecycleCallback.ForegroundListener {
    
    private static final int LOADER_ID = 104;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    
    protected P mPresenter;
    protected Bundle mSavedInstanceState;
    //FYI 26/10/2017: Temporary loading dialog till I fine a better solution
    private android.app.ProgressDialog mLoadingDialog;
    private boolean isNewActivity;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        isNewActivity = savedInstanceState == null;
        
        if (savedInstanceState != null) {
            this.mSavedInstanceState = savedInstanceState.getBundle(Tags.BUNDLE_STATE);
        } else {
            this.mSavedInstanceState = new Bundle();
        }
        // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                return new PresenterOldLoader<>(BaseYoutubeActivity.this, getPresenterFactory());
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                BaseYoutubeActivity.this.mPresenter = presenter;
            }
            
            @Override
            public final void onLoaderReset(Loader<P> loader) {
                BaseYoutubeActivity.this.mPresenter = null;
            }
        });
        
        //Add foreground listener
        AppController.getInstance().getLifecycleCallback().addListener(this);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            mPresenter.onHomeClicked();
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    
    @Override
    public void setupView() {
        ButterKnife.bind(this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onViewAttached(this, isNewActivity);
        isNewActivity = false;
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onViewDetached();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Remove foreground listener
        AppController.getInstance().getLifecycleCallback().removeListener(this);
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
        View view = getCurrentFocus();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = ((ViewGroup) view);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setSaveFromParentEnabled(false);
            }
        }
    }
    
    @Override
    public void showLoading() {
        mLoadingDialog = new android.app.ProgressDialog(this);
        mLoadingDialog.setMessage(getResources().getString(R.string.loading));
        mLoadingDialog.show();
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void retrieveState() {
        //Nothing to do yet
        
    }
    
    @Override
    public void saveState() {
        //Nothing to do yet
    }
    
    @Override
    public void noConnection() {
        Toast.makeText(this, R.string.error_no_connection, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void hideKeyboard() {
        KeyboardUtils.hide(this);
    }
    
    @Override
    public void close() {
        finish();
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
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_youtube_player),
                                                errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
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
     * should not contain {@link BaseYoutubeActivity} context reference since it will be keep on
     * rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();
    
    protected abstract YouTubePlayer.Provider getYouTubePlayerProvider();
    
}