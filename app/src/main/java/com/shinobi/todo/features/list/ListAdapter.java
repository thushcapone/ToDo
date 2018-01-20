package com.shinobi.todo.features.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shinobi.todo.R;
import com.shinobi.todo.base.BaseRecyclerViewAdapter;
import com.shinobi.todo.data.ToDoItem;
import com.shinobi.todo.utils.ColorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thiependa on 20/01/2018.
 */

class ListAdapter extends BaseRecyclerViewAdapter<ToDoItem> {
    
    public ListAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.item_to_do, parent, false);
        
        vh = new ItemViewHolder(v);
        return vh;
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ToDoItem toDo = mDataset.get(position);
        ((ItemViewHolder) holder).setToDo(toDo.getText());
        if(toDo.isOnline()){
            ((ItemViewHolder) holder).setOnlineColor();
        }else{
            ((ItemViewHolder) holder).setOfflineColor();
        }
    }
    
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        
        @BindView(R.id.layout_to_do)
        View layoutToDo;
        @BindView(R.id.text_to_do)
        TextView textToDo;
        
        ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
        
        void setToDo(String toDo) {
            textToDo.setText(toDo);
        }
        
        void setOnlineColor() {
            layoutToDo.setBackgroundColor(ColorUtils.getColor(mContext, R.color.white));
            textToDo.setTextColor(ColorUtils.getColor(mContext, R.color.black));
        }
        
        void setOfflineColor() {
            layoutToDo.setBackgroundColor(ColorUtils.getColor(mContext, R.color.colorAccent));
            textToDo.setTextColor(ColorUtils.getColor(mContext, R.color.white));
        }
        
    }
    
}
