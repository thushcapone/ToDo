/*
 * Copyright (c) 2017. SeneVideo
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;
import com.tc.senevideos.data.CategoryItem;
import com.tc.senevideos.data.TvShowItem;
import com.tc.senevideos.data.YoutubeVideoItem;
import com.tc.senevideos.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */
public class PreferencesHelper {
    
    // Sharedpref file name
    public static final String PREF_SESSION_NAME = "session";
    // End Sharedpref file name
    
    // Shared pref mode
    private static final int PRIVATE_MODE = 0;
    // End Shared pref mode
    
    //Session
    private static final String KEY_URL_CALLED = "apiCalled";
    private static final String KEY_LOGGED_IN = "checkLogin";
    private static final String KEY_FAVORITES = "favorites";
    private static final String KEY_WATCH_LATER = "watch later";
    private static final String KEY_WATCHED = "watched";
    private static final String KEY_CATEGORIES = "categories";
    private static final String KEY_DOWNLOAD = "download";
    private static final String KEY_DOWNLOAD_QUALITY = "download quality";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_BROWSE_LAYOUT = "browse layout";
    //End Session
    
    // Shared Preferences
    private final SharedPreferences mPrefSession;
    // Editor for Shared preferences
    private final SharedPreferences.Editor mEditorSession;
    
    PreferencesHelper(Context context) {
        mPrefSession = context.getSharedPreferences(PREF_SESSION_NAME, PRIVATE_MODE);
        mEditorSession = mPrefSession.edit();
    }
    
    private Boolean isUrlCalled(String url) {
        return mPrefSession.getString(KEY_URL_CALLED, "").contains(url);
    }
    
    public Boolean isUrlNotCalled(String url) {
        return !isUrlCalled(url);
    }
    
    public void saveUrl(String url) {
        mEditorSession.putString(
                KEY_URL_CALLED,
                mPrefSession.getString(KEY_URL_CALLED, "") + "|" + url
        );
        mEditorSession.apply();
    }
    
    public Boolean isLoggedIn() {
        return mPrefSession.contains(KEY_LOGGED_IN);
    }
    
    public void setLogin() {
        mEditorSession.putBoolean(KEY_LOGGED_IN, true);
        mEditorSession.apply();
    }
    
    public void saveToken(String token) {
        mEditorSession.putString(KEY_TOKEN, token);
        mEditorSession.apply();
    }
    
    public String getToken() {
        return mPrefSession.getString(KEY_TOKEN, "");
    }
    
    /////////////////////////////// WATCH LATER ////////////////////////////////////
    
    public List<YoutubeVideoItem> getWatchLater() {
        String watchLater = mPrefSession.getString(KEY_WATCH_LATER, "");
        List<YoutubeVideoItem> list = StringUtils.stringToList(watchLater, new
                TypeToken<ArrayList<YoutubeVideoItem>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    
    public Boolean isWatchLater(YoutubeVideoItem video) {
        String watchLater = mPrefSession.getString(KEY_WATCH_LATER, "");
        List<YoutubeVideoItem> list = StringUtils.stringToList(watchLater, new
                TypeToken<ArrayList<YoutubeVideoItem>>() {
        }.getType());
        return list != null && list.contains(video);
    }
    
    public void addWatchLater(YoutubeVideoItem video) {
        List<YoutubeVideoItem> list = getWatchLater();
        list.add(video);
        addListWatchLater(list);
    }
    
    private void addListWatchLater(List<YoutubeVideoItem> videos) {
        mEditorSession.putString(KEY_WATCH_LATER, StringUtils.listToString(videos));
        mEditorSession.apply();
    }
    
    public void removeWatchLater(YoutubeVideoItem video) {
        List<YoutubeVideoItem> list = getWatchLater();
        list.remove(video);
        addListWatchLater(list);
    }
    
    //////////////////////////// END OF WATCH LATER ////////////////////////////////
    
    ///////////////////////////////// WATCHED //////////////////////////////////////
    
    private List<YoutubeVideoItem> getWatched() {
        String watched = mPrefSession.getString(KEY_WATCHED, "");
        List<YoutubeVideoItem> list = StringUtils.stringToList(watched, new
                TypeToken<ArrayList<YoutubeVideoItem>>() {
                }.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    
    public YoutubeVideoItem getWatched(YoutubeVideoItem video) {
        List<YoutubeVideoItem> list = getWatched();
        YoutubeVideoItem videoItem = null;
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).equals(video)){
                videoItem = list.get(i);
            }
        }
        return videoItem;
    }
    
    public Boolean isWatched(YoutubeVideoItem video) {
        String watched = mPrefSession.getString(KEY_WATCHED, "");
        List<YoutubeVideoItem> list = StringUtils.stringToList(watched, new
                TypeToken<ArrayList<YoutubeVideoItem>>() {
                }.getType());
        return list != null && list.contains(video);
    }
    
    public void addWatched(YoutubeVideoItem video) {
        List<YoutubeVideoItem> list = getWatched();
        list.add(video);
        addListWatched(list);
    }
    
    private void addListWatched(List<YoutubeVideoItem> videos) {
        mEditorSession.putString(KEY_WATCHED, StringUtils.listToString(videos));
        mEditorSession.apply();
    }
    
    /////////////////////////////// END OF WATCHED /////////////////////////////////
    
    /////////////////////////////// FAVORITES //////////////////////////////////////
    
    public Boolean isFavorites(TvShowItem tvShow) {
        String favorites = mPrefSession.getString(KEY_FAVORITES, "");
        List<TvShowItem> list = StringUtils.stringToList(favorites, new
                TypeToken<ArrayList<TvShowItem>>() {
        }.getType());
        return list != null && list.contains(tvShow);
    }
    
    public List<TvShowItem> getFavorites() {
        String favorites = mPrefSession.getString(KEY_FAVORITES, "");
        List<TvShowItem> list = StringUtils.stringToList(favorites, new
                TypeToken<ArrayList<TvShowItem>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    
    public void addFavorites(TvShowItem tvShow) {
        List<TvShowItem> list = getFavorites();
        list.add(tvShow);
        addListFavorites(list);
    }
    
    private void addListFavorites(List<TvShowItem> tvShows) {
        mEditorSession.putString(KEY_FAVORITES, StringUtils.listToString(tvShows));
        mEditorSession.apply();
    }
    
    public void removeFavorites(TvShowItem tvShow) {
        List<TvShowItem> list = getFavorites();
        list.remove(tvShow);
        addListFavorites(list);
    }
    
    //////////////////////////// END OF FAVORITES //////////////////////////////////
    
    /////////////////////////////// CATEGORIES /////////////////////////////////////
    
    public void saveUserCategories(List<CategoryItem> favorites) {
        mEditorSession.putString(KEY_CATEGORIES, StringUtils.listToString(favorites));
        mEditorSession.apply();
    }
    
    public List<CategoryItem> getUserCategories() {
        String favorites = mPrefSession.getString(KEY_CATEGORIES, "");
        List<CategoryItem> list = StringUtils.stringToList(favorites, new
                TypeToken<ArrayList<CategoryItem>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    
    //////////////////////////// END OF CATEGORIES /////////////////////////////////
    
    /////////////////////////////// DOWNLOADED /////////////////////////////////////
    
    public Boolean isDownloaded(YoutubeVideoItem video) {
        String downloaded = mPrefSession.getString(KEY_DOWNLOAD, "");
        List<YoutubeVideoItem> list = StringUtils.stringToList(downloaded, new
                TypeToken<ArrayList<YoutubeVideoItem>>() {
        }.getType());
        return list != null && list.contains(video);
    }
    
    public void saveDownloaded(YoutubeVideoItem download) {
        List<YoutubeVideoItem> list = getDownloaded();
        list.add(download);
        saveDownloaded(list);
    }
    
    private void saveDownloaded(List<YoutubeVideoItem> download) {
        mEditorSession.putString(KEY_DOWNLOAD, StringUtils.listToString(download));
        mEditorSession.apply();
    }
    
    public List<YoutubeVideoItem> getDownloaded() {
        String downloaded = mPrefSession.getString(KEY_DOWNLOAD, "");
        List<YoutubeVideoItem> list = StringUtils.stringToList(downloaded, new
                TypeToken<ArrayList<YoutubeVideoItem>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    
    public void removeDownloaded(YoutubeVideoItem video) {
        List<YoutubeVideoItem> list = getDownloaded();
        list.remove(video);
        saveDownloaded(list);
    }
    
    public void updatedDownloaded(YoutubeVideoItem video) {
        List<YoutubeVideoItem> list = getDownloaded();
        int index = list.indexOf(video);
        list.remove(video);
        list.add(index, video);
        saveDownloaded(list);
    }
    
    public Integer getDownloadQuality() {
        return mPrefSession.getInt(KEY_DOWNLOAD_QUALITY, 360);
    }
    
    public void setDownloadQuality(Integer quality) {
        mEditorSession.putInt(KEY_DOWNLOAD_QUALITY, quality);
        mEditorSession.apply();
    }
    
    //////////////////////////// END OF DOWNLOADED /////////////////////////////////
    
    public Boolean isBrowseLayoutList() {
        return mPrefSession.getBoolean(KEY_BROWSE_LAYOUT, true);
    }
    
    public void saveBrowseLayout(Boolean isList) {
        mEditorSession.putBoolean(KEY_BROWSE_LAYOUT, isList);
        mEditorSession.apply();
    }
    
    public Boolean isListHeaderInfoVisible(String list) {
        return mPrefSession.getBoolean(list, true);
    }
    
    public void saveListHeaderInfoVisibilityStatus(String list, Boolean isVisible) {
        mEditorSession.putBoolean(list, isVisible);
        mEditorSession.apply();
    }
    
}
