package com.shinobi.todo.features.list;

import com.shinobi.todo.api.ApiResponseHandler;
import com.shinobi.todo.api.ApiRetrofitGenerator;
import com.shinobi.todo.api.ApiUrls;
import com.shinobi.todo.base.BaseModelImpl;
import com.shinobi.todo.cache.CacheUtils;
import com.shinobi.todo.data.ToDo;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by thiependa on 20/01/2018.
 */

class ListModelImpl extends BaseModelImpl<ListModelInteractPresenter> implements ListModel {
    
    ListModelImpl(ListModelInteractPresenter mInteractPresenter) {
        super(mInteractPresenter);
    }
    
    @Override
    public void getToDos() {
        ListModelClient client = ApiRetrofitGenerator.createService(ListModelClient.class);
    
        Call<ToDo> call = client.getToDos();
    
        ToDo cachedValue = CacheUtils.getCachedValue(call.request().url().uri().toString(), ToDo.class);
        if (cachedValue != null) {
            mInteractPresenter.onGetCachedToDosSuccess(cachedValue.getResults());
        }
    
        call.enqueue(new ApiResponseHandler<ToDo>(mInteractPresenter) {
            @Override
            public void onSuccess(ToDo response) {
                CacheUtils.save(call.request().url().uri().toString(), response);
                mInteractPresenter.onGetToDosSuccess(response.getResults());
            }
        });
    }
    
    private interface ListModelClient {
        
        @GET(ApiUrls.TO_DO)
        Call<ToDo> getToDos();
        
    }
    
}
