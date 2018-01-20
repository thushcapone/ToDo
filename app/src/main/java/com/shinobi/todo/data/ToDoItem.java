package com.shinobi.todo.data;

import com.shinobi.todo.app.AppController;

/**
 * Created by thiependa on 20/01/2018.
 */

public class ToDoItem {
    
    /**
     * id : 1
     * uuid : pfaoiehrfdslfnc
     * text : Faire les courses
     */
    
    private int id;
    private String uuid;
    private String text;
    private Boolean isOnline = true;
    
    public int getId() { return id;}
    
    public void setId(int id) { this.id = id;}
    
    public String getUuid() { return uuid;}
    
    public void setUuid(String uuid) { this.uuid = uuid;}
    
    public String getText() { return text;}
    
    public void setText(String text) { this.text = text;}
    
    public Boolean isOnline() {
        return isOnline;
    }
    
    public void setOnline(Boolean online) {
        isOnline = online;
    }
    
    @Override
    public String toString() {
        return AppController.getInstance().getGson().toJson(this);
    }
    
}
