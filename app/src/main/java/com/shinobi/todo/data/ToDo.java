package com.shinobi.todo.data;

import com.shinobi.todo.app.AppController;

/**
 * Created by thiependa on 20/01/2018.
 */

public class ToDo {
    
    /**
     * id : 1
     * uuid : pfaoiehrfdslfnc
     * text : Faire les courses
     */
    
    private int id;
    private String uuid;
    private String text;
    
    public int getId() { return id;}
    
    public void setId(int id) { this.id = id;}
    
    public String getUuid() { return uuid;}
    
    public void setUuid(String uuid) { this.uuid = uuid;}
    
    public String getText() { return text;}
    
    public void setText(String text) { this.text = text;}
    
    @Override
    public String toString() {
        return AppController.getInstance().getGson().toJson(this);
    }
    
}
