package com.shinobi.todo.features.add;

import com.shinobi.todo.api.ApiResponseHandler;
import com.shinobi.todo.api.ApiRetrofitGenerator;
import com.shinobi.todo.api.ApiUrls;
import com.shinobi.todo.base.BaseModelImpl;
import com.shinobi.todo.cache.CacheUtils;
import com.shinobi.todo.data.ToDo;
import com.shinobi.todo.data.ToDoItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by thiependa on 20/01/2018.
 */

class AddModelImpl extends BaseModelImpl<AddModelInteractPresenter> implements AddModel {
    
    public AddModelImpl(AddModelInteractPresenter mInteractPresenter) {
        super(mInteractPresenter);
    }
    
    @Override
    public void addToDo(ToDoItem toDo) {
        AddModelClient client = ApiRetrofitGenerator.createService(AddModelClient.class);
    
        Call<ToDo> callList = client.getToDos();
    
        ToDo cachedValue = CacheUtils.getCachedValue(callList.request().url().uri().toString(), ToDo.class);
        List<ToDoItem> toDos = new ArrayList<>();
        if (cachedValue == null) {
            cachedValue = new ToDo();
        }else{
            toDos = cachedValue.getResults();
        }
        toDos.add(0, toDo);
        cachedValue.setResults(toDos);
        CacheUtils.save(callList.request().url().uri().toString(), cachedValue);
    
        Call<Void> call = client.saveToDo(toDo);
    
        call.enqueue(new ApiResponseHandler<Void>(mInteractPresenter) {
            @Override
            public void onSuccess(Void response) {
                //Nothing to do
            }
        });
        
        mInteractPresenter.onAddToDoSuccess();
    }
    
    private interface AddModelClient {
        
        @POST(ApiUrls.TO_DO)
        Call<Void> saveToDo(@Body ToDoItem toDo);
    
        @GET(ApiUrls.TO_DO)
        Call<ToDo> getToDos();
        
    }
    
}
