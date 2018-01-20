package com.shinobi.todo.data;

import java.util.List;

/**
 * Created by thiependa on 20/01/2018.
 */

public class ToDo {
    
    
    /**
     * count : 2
     * next : null
     * previous : null
     * results : [{"id":1,"uuid":"pfaoiehrfdslfnc","text":"Faire les courses"},{"id":2,
     * "uuid":"fepoirhgprenc","text":"Faire le con"}]
     */
    
    private int count;
    private Object next;
    private Object previous;
    private List<ToDoItem> results;
    
    public int getCount() { return count;}
    
    public void setCount(int count) { this.count = count;}
    
    public Object getNext() { return next;}
    
    public void setNext(Object next) { this.next = next;}
    
    public Object getPrevious() { return previous;}
    
    public void setPrevious(Object previous) { this.previous = previous;}
    
    public List<ToDoItem> getResults() { return results;}
    
    public void setResults(List<ToDoItem> results) { this.results = results;}
    
}
